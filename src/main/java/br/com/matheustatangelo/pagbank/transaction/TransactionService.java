package br.com.matheustatangelo.pagbank.transaction;

import br.com.matheustatangelo.pagbank.authorization.AuthorizerService;
import br.com.matheustatangelo.pagbank.exception.InvalidTransactionException;
import br.com.matheustatangelo.pagbank.notification.NotificationService;
import br.com.matheustatangelo.pagbank.wallet.Wallet;
import br.com.matheustatangelo.pagbank.wallet.WalletRepository;
import br.com.matheustatangelo.pagbank.wallet.WalletType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

        private final TransactionRepository transactionRepository;
        private final WalletRepository walletRepository;
        private final AuthorizerService authorizerService;
        private final NotificationService notificationService;

        public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizerService authorizerService, NotificationService notificationService) {
            this.transactionRepository = transactionRepository;
            this.walletRepository = walletRepository;
            this.authorizerService = authorizerService;
            this.notificationService = notificationService;
        }


        @Transactional
        public Transaction create(Transaction transaction) {
            // 1- validar transação com base nas regras de negócios
            validate(transaction);

            // 2- criar a transação
            var newTransaction = transactionRepository.save(transaction);

            // 3- debitar da carteira
            var walletPayer = walletRepository.findById(transaction.payer()).get();
            var walletPayee = walletRepository.findById(transaction.payee()).get();
            walletRepository.save(walletPayer.debit(transaction.value()));
            walletRepository.save(walletPayee.credit(transaction.value()));

            // 4- chamar serviços externos
            authorizerService.authorize(transaction);

            //notificação
            notificationService.notify(transaction);
            return newTransaction;

        }

        /**
         * - the payer has a common wallet
         * - the payer has enough balance
         * - the payer is not the payee
         */
    private void validate(Transaction transaction) {
        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                .map(payer -> isTansactionValid(transaction, payer) ? transaction : null)
                        .orElseThrow(() -> new InvalidTransactionException("Invalid Transaction - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalidTransactionException("Invalid Transaction - %s".formatted(transaction)));
    }

    private boolean isTansactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMUM.getValue() &&
                payer.balance().compareTo(transaction.value()) >= 0 &&
                !payer.id().equals(transaction.payee());
    }


    public List<Transaction> list() {
        return transactionRepository.findAll();
    }
}
