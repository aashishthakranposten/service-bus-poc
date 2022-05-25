### Service Bus Properties

Namespace name - test-nm <br/>
Topic name - test-topic  <br/>
Subscription name - test-sub  <br/>
client id - test-client  <br/>

### Topic Properties:

Max. Topic Size - 1 GB  <br/>
Max. message size that can be sent to topic - 10 MB  <br/>
Message to live in topic - 14 days  <br/>
Auto-delete - never  <br/>
Duplicate Detection period - 7 days  <br/>


### Subscription Properties:

Max. Delivery count - 10 times <br/>
Message lock duration - 5 min. <br/>
Message time to live - 14 days (Reference link - https://docs.microsoft.com/en-us/azure/service-bus-messaging/message-expiration?WT.mc_id=Portal-Microsoft_Azure_ServiceBus) <br/>
Auto-delete - never  <br/>
Dead Letter Queue configuration enabled. Message will be moved to dead letter queue.

<br/>
Reference link for DLQ - https://docs.microsoft.com/en-us/azure/service-bus-messaging/service-bus-dead-letter-queues <br/>





