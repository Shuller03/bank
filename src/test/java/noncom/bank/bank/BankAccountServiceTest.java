package noncom.bank.bank;

import noncom.bank.bank.model.BankAccount;
import noncom.bank.bank.repository.BankAccountRepository;
import noncom.bank.bank.service.BankAccountService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateBankAccountValid() {

        BankAccount bankAccount = new BankAccount();
        bankAccount.setName("Mike Pavlovsky");
        bankAccount.setPin(1234);

        when(bankAccountRepository.createBankAccount(bankAccount)).thenReturn(bankAccount);

        BankAccount createdAccount = bankAccountService.createBankAccount(bankAccount);

        assertNotNull(createdAccount);
        assertEquals("Mike Vazovsky", createdAccount.getName());
        assertEquals(1234, createdAccount.getPin());
    }

    @Test
    public void testCreateBankAccountInvalidPin() {

        BankAccount bankAccount = new BankAccount();
        bankAccount.setName("Mike Vazovsky");
        bankAccount.setPin(123);

        assertThrows(IllegalArgumentException.class, () -> {
            bankAccountService.createBankAccount(bankAccount);
        });

//        verify(bankAccountRepository, never()).createBankAccount(any());
    }

    @Test
    public void testGetAllBankAccounts() {

        List<BankAccount> accounts = new ArrayList<>();

        BankAccount account1 = new BankAccount();
        account1.setName("Account First");
        account1.setPin(1111);
        account1.setBalance(1000.0);

        BankAccount account2 = new BankAccount();
        account2.setName("Account Second");
        account2.setPin(2222);
        account2.setBalance(2000.0);

        accounts.add(account1);
        accounts.add(account2);

        when(bankAccountRepository.findAll()).thenReturn(accounts);

        List<BankAccount> retrievedAccounts = bankAccountService.getAllBankAccounts();

        assertEquals(2, retrievedAccounts.size());
        assertEquals("Account First", retrievedAccounts.get(0).getName());
        assertEquals("Account Second", retrievedAccounts.get(1).getName());
    }

    @Test
    public void testIsValidNameValid() {
        String validName = "Mike Vazovsky";
        assertTrue(bankAccountService.isValidName(validName));
    }

    @Test
    public void testIsValidNameInvalid() {
        String invalidName = "MikeVazovsky228";
        assertFalse(bankAccountService.isValidName(invalidName));
    }

    @Test
    public void testCheckAccountExistsExists() {
        String existingName = "Mike Vazovsky";
        when(bankAccountRepository.checkAccountExists(existingName)).thenReturn(true);

        boolean accountExists = bankAccountRepository.checkAccountExists(existingName);

        assertTrue(accountExists);
    }

    @Test
    public void testCheckAccountExistsNotExists() {
        String nonExistingName = "Nonexistent User";
        when(bankAccountRepository.checkAccountExists(nonExistingName)).thenReturn(false);

        boolean accountExists = bankAccountRepository.checkAccountExists(nonExistingName);

        assertFalse(accountExists);
    }

}
