package noncom.bank.bank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import noncom.bank.bank.repository.BankAccountRepository;

@Service
public class ActionService {

    String PIN_EXC = "Invalid PIN";
    String MONEY_EXC = "Not enough money";
    String TARGET_EXC = "Invalid target ID";

    private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

    @Autowired
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public ActionService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Transactional
    public void deposit(int accountId, int pin, double amount) {
        if (!bankAccountRepository.checkPinForAccount(accountId, pin)) {
            logger.error("Invalid PIN for deposit, Account ID: {}, PIN: {}", accountId, pin);
            throw new IllegalArgumentException(PIN_EXC);
        } else {
            logger.info("Performing deposit for Account ID: {}, Amount: {}", accountId, amount);
            bankAccountRepository.deposit(accountId, amount);
        }
    }

    @Transactional
    public void withdraw(int accountId, int pin, double amount) {
        if (!bankAccountRepository.checkPinForAccount(accountId, pin)) {
            logger.error("Invalid PIN for withdrawal, Account ID: {}, PIN: {}", accountId, pin);
            throw new IllegalArgumentException(PIN_EXC);
        } else if (!bankAccountRepository.checkBalance(accountId, amount)) {
            logger.error("Insufficient balance for withdrawal, Account ID: {}, Amount: {}", accountId, amount);
            throw new IllegalArgumentException(MONEY_EXC);
        } else {
            logger.info("Performing withdrawal for Account ID: {}, Amount: {}", accountId, amount);
            bankAccountRepository.withdraw(accountId, amount);
        }
    }

    @Transactional
    public void transfer(int sourceAccountId, int pin, int targetAccountId, double amount) {
        if (!bankAccountRepository.checkPinForAccount(sourceAccountId, pin)) {
            logger.error("Invalid PIN for transfer, Source Account ID: {}, PIN: {}", sourceAccountId, pin);
            throw new IllegalArgumentException(PIN_EXC);
        } else if (!bankAccountRepository.checkBalance(sourceAccountId, amount)) {
            logger.error("Insufficient balance for transfer, Source Account ID: {}, Amount: {}", sourceAccountId, amount);
            throw new IllegalArgumentException(MONEY_EXC);
        } else if (!bankAccountRepository.checkTargetId(targetAccountId)) {
            logger.error("Invalid target ID for transfer, Target Account ID: {}", targetAccountId);
            throw new IllegalArgumentException(TARGET_EXC);
        } else {
            logger.info("Performing transfer from Account ID: {} to Account ID: {}, Amount: {}", sourceAccountId, targetAccountId, amount);
            bankAccountRepository.transfer(sourceAccountId,targetAccountId,amount);
        }
    }
}
