package miniprojectjo.infra;

import java.net.URI;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import miniprojectjo.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookRepository bookRepository;

    // 책 등록
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        // 기본값 설정
        book.setIsBestSeller(false);  // 기본값 설정

        // 책 저장
        bookRepository.save(book);

        // 책 등록 후 201 Created와 함께 책 정보를 반환
        return ResponseEntity.created(URI.create("/books/" + book.getId())).body(book);
    }

    // 베스트셀러 상태 업데이트
    @PutMapping("/update-bestseller/{id}")
    public ResponseEntity<Book> updateBestSeller(@PathVariable Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();

            // 로그: 해당 책 정보
            logger.info("Updating book: {}", book);

            if (book.getReadCount() >= 3) {
                book.setIsBestSeller(true);
                bookRepository.save(book);

                // 베스트셀러 업데이트 후, 상태를 로그로 확인
                logger.info("Book updated as Best Seller: {}", book);
                return ResponseEntity.ok(book);
            } else {
                // 구독 횟수가 3 이상이 아니면 베스트셀러로 설정되지 않음
                logger.warn("Book read count is not enough for Best Seller: {}", book);
                return ResponseEntity.badRequest().body(book);  // 추가적인 오류 상태 반환
            }
        } else {
            logger.error("Book not found for id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // 책 조회
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 구독 처리 (SubscriptionApplied 이벤트)
    @PostMapping("/apply-subscription")
    public ResponseEntity<String> applySubscription(@RequestBody SubscriptionApplied subscriptionApplied) {
        try {
            logger.info("SubscriptionApplied received: {}", subscriptionApplied);

            Long bookId = null;
            if (subscriptionApplied.getBookId() instanceof Long) {
                bookId = (Long) subscriptionApplied.getBookId();
            }

            Optional<Book> bookOpt = bookRepository.findById(bookId);

            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();

                // 책 구독 카운트 증가
                book.setReadCount(book.getReadCount() + 1);

                // 구독이 3회 이상이면 베스트셀러 처리
                if (book.getReadCount() >= 3 && !book.getIsBestSeller()) {
                    book.setIsBestSeller(true);
                }

                // 책 정보 업데이트
                bookRepository.save(book);

                // 응답 반환
                return ResponseEntity.ok("Book subscription applied successfully. Read count: " + book.getReadCount());
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error processing subscription: ", e);  // 예외 상세 로깅
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
