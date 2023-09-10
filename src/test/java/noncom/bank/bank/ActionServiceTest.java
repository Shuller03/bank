package noncom.bank.bank;

import noncom.bank.bank.repository.BankAccountRepository;
import noncom.bank.bank.service.ActionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class ActionServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private ActionService actionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDepositValid() {
        int accountId = 1;
        int pin = 1234;
        double amount = 100.0;

        when(bankAccountRepository.checkPinForAccount(accountId, pin)).thenReturn(true);

        actionService.deposit(accountId, pin, amount);

        verify(bankAccountRepository, times(1)).deposit(accountId, amount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDepositInvalidPin() {
        int accountId = 1;
        int pin = 1234;
        double amount = 100.0;

        when(bankAccountRepository.checkPinForAccount(accountId, pin)).thenReturn(false);

        actionService.deposit(accountId, pin, amount);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawInvalidPin() {
        int accountId = 1;
        int pin = 9999;
        double amount = 100.0;

        when(bankAccountRepository.checkPinForAccount(accountId, pin)).thenReturn(false);

        actionService.withdraw(accountId, pin, amount);
    }

    @Test
    public void testWithdrawValid() {
        int accountId = 1;
        int pin = 1234;
        double amount = 100.0;

        when(bankAccountRepository.checkPinForAccount(accountId, pin)).thenReturn(true);
        when(bankAccountRepository.checkBalance(accountId, amount)).thenReturn(true);

        actionService.withdraw(accountId, pin, amount);

        verify(bankAccountRepository, times(1)).withdraw(accountId, amount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawInsufficientBalance() {
        int accountId = 1;
        int pin = 1234;
        double amount = 1000.0;

        when(bankAccountRepository.checkPinForAccount(accountId, pin)).thenReturn(true);
        when(bankAccountRepository.checkBalance(accountId, amount)).thenReturn(false);

        actionService.withdraw(accountId, pin, amount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawValidInvalidBalance() {
        int accountId = 1;
        int pin = 1234;
        double amount = 100.0;

        when(bankAccountRepository.checkPinForAccount(accountId, pin)).thenReturn(true);
        when(bankAccountRepository.checkBalance(accountId, amount)).thenReturn(true);

        doThrow(IllegalArgumentException.class).when(bankAccountRepository).withdraw(accountId, amount);

        actionService.withdraw(accountId, pin, amount);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testTransferInvalidPin() {
        int sourceAccountId = 1;
        int sourcePin = 9999;
        int targetAccountId = 2;
        double amount = 100.0;

        when(bankAccountRepository.checkPinForAccount(sourceAccountId, sourcePin)).thenReturn(false);

        actionService.transfer(sourceAccountId, sourcePin, targetAccountId, amount);
    }

    @Test
    public void testTransferValid() {
        int sourceAccountId = 1;
        int sourcePin = 1234;
        int targetAccountId = 2;
        double amount = 100.0;

        when(bankAccountRepository.checkPinForAccount(sourceAccountId, sourcePin)).thenReturn(true);
        when(bankAccountRepository.checkBalance(sourceAccountId, amount)).thenReturn(true);
        when(bankAccountRepository.checkTargetId(targetAccountId)).thenReturn(true);


        actionService.transfer(sourceAccountId, sourcePin, targetAccountId, amount);

        verify(bankAccountRepository, times(1)).transfer(sourceAccountId, targetAccountId, amount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferInsufficientBalance() {
        int sourceAccountId = 1;
        int sourcePin = 1234;
        int targetAccountId = 2;
        double amount = 1000.0;

        when(bankAccountRepository.checkPinForAccount(sourceAccountId, sourcePin)).thenReturn(true);
        when(bankAccountRepository.checkBalance(sourceAccountId, amount)).thenReturn(false);

        actionService.transfer(sourceAccountId, sourcePin, targetAccountId, amount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferInvalidTargetAccount() {
        int sourceAccountId = 1;
        int sourcePin = 1234;
        int targetAccountId = 23;
        double amount = 100.0;

        when(bankAccountRepository.checkPinForAccount(sourceAccountId, sourcePin)).thenReturn(true);
        when(bankAccountRepository.checkBalance(sourceAccountId, amount)).thenReturn(true);
        when(bankAccountRepository.checkTargetId(targetAccountId)).thenReturn(false);

        actionService.transfer(sourceAccountId, sourcePin, targetAccountId, amount);
    }

}
