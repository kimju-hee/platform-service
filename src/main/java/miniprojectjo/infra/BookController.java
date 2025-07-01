package miniprojectjo.infra;

import java.net.URI;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import miniprojectjo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;


//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        // 필요한 기본값을 설정
        book.setIsBestSeller(false); // 기본값 설정

        bookRepository.save(book);

        // 책이 저장되었으므로 201 Created와 함께 책 데이터를 반환
        return ResponseEntity.created(URI.create("/books/" + book.getId())).body(book);
    }

    // 예시로 GET 요청을 처리할 수 있는 방법도 추가
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
