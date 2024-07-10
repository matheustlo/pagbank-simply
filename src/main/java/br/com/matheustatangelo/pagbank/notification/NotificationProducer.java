package br.com.matheustatangelo.pagbank.notification;

import br.com.matheustatangelo.pagbank.transaction.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(Transaction transaction){
        Notification notification = new Notification(true);
        kafkaTemplate.send("transaction-notification", transaction);
    }
}
