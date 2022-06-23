package com.poc.azureservicebuspoc.publisher;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.jms.ObjectMessage;

import com.poc.azureservicebuspoc.model.Consignment;
import com.poc.azureservicebuspoc.model.ConsignmentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    //Queue/Topic name
    private static final String DESTINATION_NAME = "test-topic6";

    @Autowired
    private JmsTemplate jmsTemplate;

    //private static AtomicLong counter = new AtomicLong(0);

    //@PostConstruct
    public void postConstruct() {
        postMessage();
    }

    public void postMessage() {

    }

    @Scheduled(fixedDelay = 60000)
    public void sendMessage() {
        int count = 0;
        try {
            for(int i = 0; i < 100; i++) {
                jmsTemplate.convertAndSend(DESTINATION_NAME, createConsignmentData());
                count++;
            }
            System.exit(0);
            //counter.getAndIncrement();

        } catch (Exception ex) {

        }
        System.out.println("=========================== Finished sending messages Total messages sent in 1 run => " + count);
    }

    private ConsignmentData createConsignmentData() {
        ConsignmentData consignmentData = new ConsignmentData();
        consignmentData.setBatchId(UUID.randomUUID());
        consignmentData.setConsignments(Arrays.asList(createConsignment()));
        return consignmentData;
    }

    private Consignment createConsignment() {
        Consignment consignment = new Consignment();
        consignment.setUuid(UUID.randomUUID());
        return consignment;
    }
}
