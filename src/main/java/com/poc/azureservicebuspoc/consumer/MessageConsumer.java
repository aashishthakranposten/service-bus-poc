package com.poc.azureservicebuspoc.consumer;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import com.azure.spring.cloud.autoconfigure.implementation.servicebus.properties.AzureServiceBusProperties;
import com.poc.azureservicebuspoc.model.Consignment;
import com.poc.azureservicebuspoc.model.ConsignmentData;
import com.poc.azureservicebuspoc.repository.ConsignmentDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.qpid.jms.message.JmsObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageConsumer {

    //private final static Logger LOG = Logger.getLogger(MessageConsumer.class.getName());

    private static final String TOPIC_NAME = "test-topic";
    private static final String QUEUE_NAME = TOPIC_NAME + "/Subscriptions/test-sub-d";
    private static final String SUBSCRIPTION_NAME = "test-sub-d";

    private static AtomicLong counter = new AtomicLong(0);
    private static AtomicLong duplicate = new AtomicLong(0);

    private Instant start = Instant.now();

    private static Map<UUID, Boolean> IDS = new ConcurrentHashMap();

    private final ConsignmentDataRepository consignmentDataRepository;

    /*@Autowired
    private AzureServiceBusProperties properties;*/

    /*@JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory",
            subscription = SUBSCRIPTION_NAME, id = "listener1")*/
    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory",
            subscription = SUBSCRIPTION_NAME, id = "listener1")
    public void processMessage1(Message message) throws JMSException {
        process1(message);
    }

   /* @JmsListener(destination = TOPIC_NAME, containerFactory = "customJmsListenerContainerFactory2",
    subscription = SUBSCRIPTION_NAME, id = "listener2")*/
    public void processMessage2(Message message) throws JMSException {
        process1(message);
    }

    private void process1(Message message) throws JMSException {
        UUID batchId = null;
        try {
            if(message instanceof JmsObjectMessage) {
                ConsignmentData consignmentData = (ConsignmentData) ((JmsObjectMessage) message).getObject();
                batchId = consignmentData.getBatchId();
                if(IDS.containsKey(batchId)) {
                    duplicate.getAndIncrement();
                    System.out.println("========== Duplicate ID => " + batchId);
                    message.acknowledge();
                    return;
                }
                counter.getAndAdd(1);
               System.out.println("============== Received message ID => " + batchId);
                IDS.put(batchId, true);
                process(consignmentData).thenAccept(o -> {
                    try {
                        message.acknowledge();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                });
                Instant end = Instant.now();
                long seconds = Duration.between(start, end).getSeconds();
                if(seconds >= 60) {
                    //System.out.println("======= Messages received per minute => " + counter.get());
                    start = Instant.now();
                    counter.set(0);
                }
                //LOG.info("Received message: " + ((JmsObjectMessage) message).getObject().toString());
            } /*else if(message instanceof TextMessage)
                LOG.info("Received message: " + ((TextMessage) message).getText());
            else
                LOG.severe("Unknown Message Format:");*/

           // process();

            /*Thread.sleep(5000);
            System.out.println("============== Received message number => " + counter.get());*/
            /*process();
            message.acknowledge();*/
        } catch (InterruptedException ex) {
            System.out.println("=========== Interrupted ========== ");
        } catch(Exception ex) {
            if(null != batchId)
                IDS.remove(batchId);
            throw new RuntimeException("Unable to process message -> " + ObjectUtils.defaultIfNull(batchId, "Unknow Batch Id"));
        }
    }
    @Async("threadPoolTaskExecutor")
    CompletableFuture<Integer> process(ConsignmentData consignment) throws InterruptedException {
        consignmentDataRepository.save(consignment);
        log.info(consignment.getBatchId().toString());
        Thread.sleep(1300000);
        return CompletableFuture.completedFuture(5);
    }

    //@JmsListener(destination = TOPIC_NAME, containerFactory = "jmsListenerContainerFactory", subscription = "test-sub")
    public void processQueueMessage(Message message) throws JMSException {
        if(message instanceof JmsObjectMessage)
            log.info("Received message: " + ((JmsObjectMessage) message).getObject().toString());
        else if(message instanceof TextMessage)
            log.info("Received message: " + ((TextMessage) message).getText());
        else
            log.info("Unknown Message Format:");
    }
}
