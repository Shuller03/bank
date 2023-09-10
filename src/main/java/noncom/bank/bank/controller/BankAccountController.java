package noncom.bank.bank.controller;

import noncom.bank.bank.model.dto.AccountDTO;
import noncom.bank.bank.repository.BankAccountRepository;
import noncom.bank.bank.service.BankAccountService;
import noncom.bank.bank.model.dto.CreateAccountDTO;
import noncom.bank.bank.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {


    @Autowired BankAccountRepository bankAccountRepository;
    @Autowired BankAccountService accountService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountDTO accountDTO) {
        try{
            BankAccount bankAccount = new BankAccount();
            bankAccount.setName(accountDTO.getName());
            bankAccount.setPin(accountDTO.getPin());

            accountService.createBankAccount(bankAccount);

            return ResponseEntity.status(HttpStatus.OK).body(bankAccount.getId().toString());
        }
        catch (IllegalArgumentException e){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }

    @GetMapping("/all")
    public List<AccountDTO> getAllAccounts() {
        List<BankAccount> accounts = bankAccountRepository.findAll();

        return accounts.stream()
                .map(account -> {
                    AccountDTO dto = new AccountDTO();
                    dto.setId(account.getId());
                    dto.setName(account.getName());
                    dto.setBalance(account.getBalance());
                    return dto;
                }).toList();
    }

}
