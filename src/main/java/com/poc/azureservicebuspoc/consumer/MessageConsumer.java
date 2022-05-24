package com.poc.azureservicebuspoc.consumer;

import com.poc.azureservicebuspoc.model.ConsignmentData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageConsumer {

    private static final String TOPIC_NAME = "test-topic";
    private static final String SUBSCRIPTION_NAME = "test-sub";

    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory",
    subscription = SUBSCRIPTION_NAME)
    public void receiveMessage(ConsignmentData consignmentData) {
        log.info("Received message: {}", consignmentData.toString());
    }
}
