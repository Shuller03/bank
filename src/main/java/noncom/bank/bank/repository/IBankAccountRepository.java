package noncom.bank.bank.repository;

import noncom.bank.bank.model.BankAccount;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IBankAccountRepository {
    BankAccount createBankAccount(@NotNull BankAccount bankAccount);
    List<BankAccount> findAll();
    int getId(String name, int pin);
    long count();
    void deposit(int accountId, double amount);
    void withdraw(int accountId, double amount);
    void transfer(int sourceAccountId, int targetAccountId, double amount);
    boolean checkAccountExists(String name);
    boolean checkPinForAccount(int accountId, int pin);
    boolean checkBalance(int accountId, double amount);
    boolean checkTargetId(int targetId);
}
