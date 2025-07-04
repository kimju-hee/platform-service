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

    @Lob
    private String content;
    @Lob
    private String summary;
    private String coverImageUrl;
    private Integer subscriptionFee;

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

    // 출간 등록 시 AI 결과 반영
    public static void registerBook(SubscriptionFeeCalculated subscriptionFeeCalculated) {
        if (subscriptionFeeCalculated.getManuscriptId() == null) return;

        Book book = new Book();
        book.setBookName(subscriptionFeeCalculated.getBookName());
        book.setCategory(subscriptionFeeCalculated.getCategory());
        book.setAuthorName(subscriptionFeeCalculated.getAuthorName());
        book.setIsBestSeller(false);
        book.setContent(subscriptionFeeCalculated.getContent());
        book.setSummary(subscriptionFeeCalculated.getSummary());
        book.setCoverImageUrl(subscriptionFeeCalculated.getCoverImageUrl());
        book.setSubscriptionFee(subscriptionFeeCalculated.getSubscriptionFee());

        repository().save(book);

        BookRegistered bookRegistered = new BookRegistered(book);
        bookRegistered.publishAfterCommit();
    }

}
