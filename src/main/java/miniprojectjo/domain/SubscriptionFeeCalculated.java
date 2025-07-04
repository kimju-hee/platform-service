package miniprojectjo.domain;

import java.util.*;
import lombok.*;
import miniprojectjo.infra.AbstractEvent;

@Data
@ToString
public class SubscriptionFeeCalculated extends AbstractEvent {

    private Long id;
    private Long manuscriptId;
    private Integer subscriptionFee;
    private String criteria;
    private Date calculatedAt;
    private String content;
    private String summary;
    private String coverImageUrl;
    private String bookName;
    private String category;
    private String authorName;
    
}
