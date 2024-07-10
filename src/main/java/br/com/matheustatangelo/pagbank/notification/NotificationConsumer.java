package br.com.matheustatangelo.pagbank.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.matheustatangelo.pagbank.transaction.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);
    private final RestClient restClient;

    public NotificationConsumer(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://run.mocky.io/v3/1e877467-d832-48e4-92fa-eef2e4436326")
                .build();
    }

    @KafkaListener(topics = "transaction-notification", groupId = "pagbank-desafio-backend")
    public void receiceNotification(Transaction transaction) {
        LOGGER.info("Notifying transaction: {}.......", transaction);



        var response = restClient.get()
                .retrieve()
                .toEntity(Notification.class);

        if(response.getStatusCode().isError() || !response.getBody().message())
            throw new NotificationException("Unauthorized Transaction!");

        LOGGER.info("Notification has been sent: {}...", response.getBody());


    }
}
