package com.poc.azureservicebuspoc.config;

import javax.jms.ConnectionFactory;
import javax.jms.Session;


import com.azure.spring.cloud.autoconfigure.implementation.servicebus.properties.AzureServiceBusProperties;
import com.azure.spring.cloud.autoconfigure.jms.ServiceBusJmsConnectionFactory;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.policy.JmsDefaultPrefetchPolicy;
import org.apache.qpid.jms.policy.JmsPrefetchPolicy;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.QosSettings;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
@EnableJms
public class AzureBusConfig {

    private static final String AMQP_URI_FORMAT = "amqps://%s?amqp.idleTimeout=%d";


    /*@Bean
    public AzureServiceBusProperties azureServiceBusProperties() {
        return new AzureServiceBusProperties();
    }*/

    //@Bean("customJmsListenerContainerFactory1")
//    @Primary
    public JmsListenerContainerFactory customJmsListenerContainerFactory1(DefaultJmsListenerContainerFactory jmsListenerContainerFactory) {
        /*ServiceBusJmsConnectionFactory cachingConnectionFactory =
                (ServiceBusJmsConnectionFactory) ((CachingConnectionFactory) connectionFactory).getTargetConnectionFactory();*/
//        cachingConnectionFactory.setPrefetchPolicy(this.prefetchPolicy());
//        cachingConnectionFactory.setClientID("test-client2");

        jmsListenerContainerFactory.setReplyQosSettings(qosSettings());
        //jmsListenerContainerFactory.set

        //jmsListenerContainerFactory.set
//        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
//        listenerContainerFactory.setConnectionFactory(cachingConnectionFactory);

//        JmsPoolConnectionFactory cachingConnectionFactory = (JmsPoolConnectionFactory) connectionFactory;
        //((JmsConnectionFactory) cachingConnectionFactory).setPrefetchPolicy(this.prefetchPolicy());
//        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
//        listenerContainerFactory.setConnectionFactory(cachingConnectionFactory);
//        listenerContainerFactory.setSubscriptionDurable(true);
//        listenerContainerFactory.setSubscriptionShared(false);
//        listenerContainerFactory.setSessionTransacted(true);
//        listenerContainerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
//        listenerContainerFactory.setBackOff(new ExponentialBackOff());
//        listenerContainerFactory.setReplyQosSettings(qosSettings());
//        listenerContainerFactory.setAutoStartup(true);
//        listenerContainerFactory.setMaxMessagesPerTask(10);
//        listenerContainerFactory.setClientId("test-client2");
//        listenerContainerFactory.setSubscriptionDurable(true);
//        listenerContainerFactory.setTaskExecutor(threadPoolTaskExecutor());
//        listenerContainerFactory.setConcurrency("32");
//        listenerContainerFactory.setAutoStartup(true);
        return jmsListenerContainerFactory;
    }

    /*@Bean("customJmsListenerContainerFactory2")
//    @Primary
    public JmsListenerContainerFactory customJmsListenerContainerFactory2(ConnectionFactory connectionFactory) {
        ServiceBusJmsConnectionFactory cachingConnectionFactory =
                (ServiceBusJmsConnectionFactory) ((CachingConnectionFactory) connectionFactory).getTargetConnectionFactory();
        cachingConnectionFactory.setPrefetchPolicy(this.prefetchPolicy());
        cachingConnectionFactory.setClientID("test-client3");

        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
        listenerContainerFactory.setConnectionFactory(SerializationUtils.clone(cachingConnectionFactory));

//        JmsPoolConnectionFactory cachingConnectionFactory = (JmsPoolConnectionFactory) connectionFactory;
        //((JmsConnectionFactory) cachingConnectionFactory).setPrefetchPolicy(this.prefetchPolicy());
//        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
//        listenerContainerFactory.setConnectionFactory(cachingConnectionFactory);
        listenerContainerFactory.setSubscriptionDurable(true);
        listenerContainerFactory.setSubscriptionShared(false);
//        listenerContainerFactory.setSessionTransacted(true);
        listenerContainerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
//        listenerContainerFactory.setBackOff(new ExponentialBackOff());
//        listenerContainerFactory.setReplyQosSettings(qosSettings());
//        listenerContainerFactory.setAutoStartup(true);
//        listenerContainerFactory.setMaxMessagesPerTask(10);
//        listenerContainerFactory.setClientId("test-client2");
//        listenerContainerFactory.setSubscriptionDurable(true);
        listenerContainerFactory.setTaskExecutor(threadPoolTaskExecutor());
        listenerContainerFactory.setConcurrency("16");
        listenerContainerFactory.setAutoStartup(true);
        return listenerContainerFactory;
    }*/

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(40);
        executor.setThreadNamePrefix("jms_consumer_task_executor_thread");
        executor.initialize();
        return executor;
    }

//    @Bean

//    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
//        jmsTemplate.setTimeToLive(60000L);
//        jmsTemplate.setQosSettings(qosSettings());
        jmsTemplate.setConnectionFactory(connectionFactory);
//        jmsTemplate.setExplicitQosEnabled(true);
//        jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        jmsTemplate.setSessionTransacted(false);
        return jmsTemplate;
    }

    private JmsPrefetchPolicy prefetchPolicy() {
        // Adjust the prefetch size to a number of messages that can be processed before
        // the default message lock of 30 seconds is exceeded. The default prefetch size is 1000.
        var prefetchPolicy = new JmsDefaultPrefetchPolicy();
        prefetchPolicy.setAll(16);
        return prefetchPolicy;
    }

    private QosSettings qosSettings() {
        QosSettings settings = new QosSettings();
        settings.setTimeToLive(1000*60*60*24L);
        return settings;
    }

}
