package com.poc.azureservicebuspoc.consumer;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import com.poc.azureservicebuspoc.model.Consignment;
import com.poc.azureservicebuspoc.model.ConsignmentData;
import org.apache.qpid.jms.message.JmsObjectMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private final static Logger LOG = Logger.getLogger(MessageConsumer.class.getName());

    private static final String TOPIC_NAME = "test-topic6";
    private static final String QUEUE_NAME = TOPIC_NAME + "/Subscriptions/test-sub/$deadletterqueue";
    private static final String SUBSCRIPTION_NAME = "test-sub";

    private static AtomicLong counter = new AtomicLong(0);
    private static AtomicLong duplicate = new AtomicLong(0);

    private Instant start = Instant.now();

    private static Map<UUID, Boolean> IDS = new ConcurrentHashMap();

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
        try {
            if(message instanceof JmsObjectMessage) {
                ConsignmentData consignmentData = (ConsignmentData) ((JmsObjectMessage) message).getObject();
                if(IDS.containsKey(consignmentData.getBatchId())) {
                    duplicate.getAndIncrement();
                    System.out.println("========== Duplicate key received========= times " + duplicate.get());
                    message.acknowledge();
                    return;
                }
                counter.getAndIncrement();
                process(consignmentData).thenAccept(o -> {
                    try {
                        IDS.put(consignmentData.getBatchId(), true);
                        message.acknowledge();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                });
                Instant end = Instant.now();
                long seconds = Duration.between(start, end).getSeconds();
                if(seconds >= 60) {
                    System.out.println("======= Messages received per minute => " + counter.get());
                    start = Instant.now();
                    counter.set(0);
                }
                //LOG.info("Received message: " + ((JmsObjectMessage) message).getObject().toString());
            } else if(message instanceof TextMessage)
                LOG.info("Received message: " + ((TextMessage) message).getText());
            else
                LOG.severe("Unknown Message Format:");

           // process();

            /*Thread.sleep(5000);
            System.out.println("============== Received message number => " + counter.get());*/
            /*process();
            message.acknowledge();*/
        } catch (InterruptedException ex) {
            System.out.println("=========== Interrupted ========== ");
        }
    }
    @Async("threadPoolTaskExecutor")
    CompletableFuture<Integer> process(ConsignmentData consignment) throws InterruptedException {
        System.out.println("============== Received message number => " + counter.get());
        Thread.sleep(5000);
        return CompletableFuture.completedFuture(5);
    }

    /*@JmsListener(destination = QUEUE_NAME, containerFactory = "customJmsListenerContainerFactory",
    subscription = "test-sub-dlq")
    public void processQueueMessage(Message message) throws JMSException {
        if(message instanceof JmsObjectMessage)
            LOG.info("Received message: " + ((JmsObjectMessage) message).getObject().toString());
        else if(message instanceof TextMessage)
            LOG.info("Received message: " + ((TextMessage) message).getText());
        else
            LOG.severe("Unknown Message Format:");
    }*/
}
