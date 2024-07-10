package br.com.matheustatangelo.pagbank.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.matheustatangelo.pagbank.exception.UnauthorizedTransactionException;
import br.com.matheustatangelo.pagbank.transaction.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuthorizerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizerService.class);
    private RestClient restClient;

    public AuthorizerService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://run.mocky.io/v3/af18c7e5-8f52-4dc4-8c16-b144808ed113")
                .build();
    }

    public void authorize(Transaction transaction) {
        LOGGER.info("Authorizing transaction: {}........", transaction);
       var response = restClient.get()
                .retrieve()
                .toEntity(Authorization.class);

       if(response.getStatusCode().isError() || !response.getBody().isAuthorized())
           throw new UnauthorizedTransactionException("Unauthorized Transaction!");

       LOGGER.info("Transaction authorized: {}", transaction);
    }
}
