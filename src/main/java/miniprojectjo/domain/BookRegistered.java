package miniprojectjo.domain;

import java.time.LocalDate;
import lombok.*;
import miniprojectjo.infra.AbstractEvent;

// <<< DDD / Domain Event
@Data
@ToString
public class BookRegistered extends AbstractEvent {

    private Long id;           
    private String bookName;
    private String category;
    private Boolean isBestSeller;
    private String bookContent;
    private String authorName;
    private String summary;
    private String coverImageUrl;
    private Integer subscriptionFee;

    //Book → Event 변환 생성자
    public BookRegistered(Book aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.bookName = aggregate.getBookName();
        this.category = aggregate.getCategory();
        this.isBestSeller = aggregate.getIsBestSeller();
        this.bookContent = aggregate.getContent();
        this.authorName = aggregate.getAuthorName();
        this.summary = aggregate.getSummary();
        this.coverImageUrl = aggregate.getCoverImageUrl();
        this.subscriptionFee = aggregate.getSubscriptionFee();
    }

    public BookRegistered() {
        super();
    }
}
// >>> DDD / Domain Event
