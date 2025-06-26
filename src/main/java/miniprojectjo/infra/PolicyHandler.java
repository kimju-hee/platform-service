package miniprojectjo.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import miniprojectjo.config.kafka.KafkaProcessor;
import miniprojectjo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    BookRepository bookRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionApplied'"
    )
    public void wheneverSubscriptionApplied_GrantBadge(
        @Payload SubscriptionApplied subscriptionApplied
    ) {
        SubscriptionApplied event = subscriptionApplied;
        System.out.println(
            "\n\n##### listener GrantBadge : " + subscriptionApplied + "\n\n"
        );

        // Sample Logic //
        Book.grantBadge(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionFeeCalculated'"
    )
    public void wheneverSubscriptionFeeCalculated_RegisterBook(
        @Payload SubscriptionFeeCalculated subscriptionFeeCalculated
    ) {
        SubscriptionFeeCalculated event = subscriptionFeeCalculated;
        System.out.println(
            "\n\n##### listener RegisterBook : " +
            subscriptionFeeCalculated +
            "\n\n"
        );

        // Sample Logic //
        Book.registerBook(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
