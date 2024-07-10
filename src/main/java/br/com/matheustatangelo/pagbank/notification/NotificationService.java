package br.com.matheustatangelo.pagbank.notification;

import br.com.matheustatangelo.pagbank.transaction.Transaction;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    public void notify(Transaction transaction){
        notificationProducer.sendNotification(transaction);
    }
}
