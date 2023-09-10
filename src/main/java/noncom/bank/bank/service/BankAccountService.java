package noncom.bank.bank.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import noncom.bank.bank.model.BankAccount;
import noncom.bank.bank.repository.BankAccountRepository;

import java.util.List;

@Service
public class BankAccountService {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountService.class);

    @Autowired
    private final BankAccountRepository accountRepository;

    @Autowired
    public BankAccountService(BankAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public BankAccount createBankAccount(BankAccount bankAccount) {
        if (!isValidPin(String.valueOf(bankAccount.getPin()))) {
            logger.error("Invalid PIN for account creation, PIN: {}", bankAccount.getPin());
            throw new IllegalArgumentException("Pin should consist of exactly 4 digits");
        } else if (!isValidName(bankAccount.getName())) {
            logger.error("Invalid name for account creation, Name: {}", bankAccount.getName());
            throw new IllegalArgumentException("Name cannot consist of digits");
        } else if (accountRepository.checkAccountExists(bankAccount.getName())) {
            logger.error("Account with the same name already exists, Name: {}", bankAccount.getName());
            throw new IllegalArgumentException("This name is already taken");
        }
        logger.info("Creating a new bank account: Name: {}, PIN: {}, Balance: {}",
                bankAccount.getName(), bankAccount.getPin(), bankAccount.getBalance());
        return accountRepository.createBankAccount(bankAccount);
    }

    public List<BankAccount> getAllBankAccounts() {
        logger.info("Fetching all bank accounts");
        return accountRepository.findAll();
    }

    public boolean isValidPin(String pin) {
        return pin != null && pin.matches("\\d{4}");
    }

    public boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s-]*$");
    }
}
