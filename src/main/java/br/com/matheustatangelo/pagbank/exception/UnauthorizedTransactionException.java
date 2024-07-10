package br.com.matheustatangelo.pagbank.exception;

public class UnauthorizedTransactionException extends RuntimeException{

    public UnauthorizedTransactionException(String message) {
        super(message);
    }
}
