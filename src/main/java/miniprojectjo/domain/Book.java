package miniprojectjo.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import miniprojectjo.PlatformApplication;
import miniprojectjo.domain.BadgeGranted;
import miniprojectjo.domain.BookRegistered;

@Entity
@Table(name = "Book_table")
@Data
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String bookName;

    private String category;

    private Boolean isBestSeller;

    private String authorName;

    private Integer readCount = 0;

    public static BookRepository repository() {
        BookRepository bookRepository = PlatformApplication.applicationContext.getBean(
            BookRepository.class
        );
        return bookRepository;
    }

    public static void grantBadge(SubscriptionApplied subscriptionApplied) {
    if (subscriptionApplied.getBookId() == null) return;

    Long bookId = Long.valueOf(subscriptionApplied.getBookId().toString());

    repository().findById(bookId).ifPresent(book -> {
        int currentReadCount = book.getReadCount() != null ? book.getReadCount() : 0;
        book.setReadCount(currentReadCount + 1);

        if (book.getReadCount() >= 3 && !Boolean.TRUE.equals(book.getIsBestSeller())) {
            book.setIsBestSeller(true);

            BadgeGranted badgeGranted = new BadgeGranted(book);
            badgeGranted.setSubscriptionCount(book.getReadCount()); // 실제로는 열람 수
            badgeGranted.publishAfterCommit();
        }

        repository().save(book);
    });
}
public static void registerBook(SubscriptionFeeCalculated subscriptionFeeCalculated) {
    if (subscriptionFeeCalculated.getManuscriptId() == null) return;

    Book book = new Book();

    book.setBookName("신규 도서_" + subscriptionFeeCalculated.getManuscriptId());
    book.setCategory("자동 등록");
    book.setAuthorName("AI Writer");
    book.setIsBestSeller(false); // 기본값

    repository().save(book);

    BookRegistered bookRegistered = new BookRegistered(book);
    bookRegistered.setBookContent("기준: " + subscriptionFeeCalculated.getCriteria());
    bookRegistered.publishAfterCommit();
}


}
//>>> DDD / Aggregate Root
