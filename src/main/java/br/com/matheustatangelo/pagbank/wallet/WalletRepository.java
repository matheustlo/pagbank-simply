package br.com.matheustatangelo.pagbank.wallet;

import org.springframework.data.repository.CrudRepository;

//Como não vou usar os métodos de retorno lista, posso usar CrudRepository
public interface WalletRepository extends CrudRepository<Wallet, Long>{
}
