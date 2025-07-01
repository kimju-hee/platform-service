package miniprojectjo.domain;

import javax.persistence.*;
import lombok.Data;
import miniprojectjo.PlatformApplication;

@Entity
@Table(name = "Book_table")
@Data
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
        return PlatformApplication.applicationContext.getBean(BookRepository.class);
    }

    public static void grantBadge(SubscriptionApplied subscriptionApplied) {
    if (subscriptionApplied.getBookId() == null) return;

    Long bookId = (Long) subscriptionApplied.getBookId();
    repository().findById(bookId).ifPresent(book -> {
        int cnt = book.getReadCount() == null ? 0 : book.getReadCount();
        book.setReadCount(cnt + 1);

        if (book.getReadCount() >= 3 && !Boolean.TRUE.equals(book.getIsBestSeller())) {
            book.setIsBestSeller(true);
            BadgeGranted badgeGranted = new BadgeGranted(book);
            badgeGranted.setSubscriptionCount(book.getReadCount());
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
    book.setIsBestSeller(false);

    repository().save(book);

    BookRegistered bookRegistered = new BookRegistered(book);
    bookRegistered.setBookContent("기준: " + subscriptionFeeCalculated.getCriteria());
    bookRegistered.publishAfterCommit();
    }

}