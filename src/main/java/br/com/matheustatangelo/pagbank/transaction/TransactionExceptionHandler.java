package br.com.matheustatangelo.pagbank.transaction;

import br.com.matheustatangelo.pagbank.exception.InvalidTransactionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TransactionExceptionHandler {
    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<Object> handle(InvalidTransactionException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
