package com.poc.azureservicebuspoc.publisher;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import com.poc.azureservicebuspoc.model.Consignment;
import com.poc.azureservicebuspoc.model.ConsignmentData;
import com.poc.azureservicebuspoc.model.ConsignmentItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageSender {

    //Queue/Topic name
    private static final String DESTINATION_NAME = "test-topic";

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostConstruct
    public void postConstruct() {
        postMessage();
    }

    public void postMessage() {
        log.info("Sending message");
        jmsTemplate.convertAndSend(DESTINATION_NAME, createConsignmentData());
    }

    private ConsignmentData createConsignmentData() {
        return ConsignmentData.builder()
            .batchId(UUID.randomUUID())
            .consignments(List.of(createConsignment()))
            .build();
    }

    private Consignment createConsignment() {
        UUID consignmentId = UUID.randomUUID();
        ConsignmentItem item = ConsignmentItem.builder()
            .id(UUID.randomUUID())
            .consignmentId(consignmentId)
            .build();
        return Consignment.builder()
            .uuid(consignmentId)
            .items(List.of(item))
            .build();
    }
}
