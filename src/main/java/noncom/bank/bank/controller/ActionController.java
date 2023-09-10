package noncom.bank.bank.controller;

import noncom.bank.bank.model.dto.DepositDTO;
import noncom.bank.bank.model.dto.TransferDTO;
import noncom.bank.bank.model.dto.WithdrawDTO;
import noncom.bank.bank.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account/actions")
public class ActionController {

    @Autowired ActionService actionService;

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deposit(@RequestBody DepositDTO depositDTO) {
        try {
            actionService.deposit(depositDTO.getId(),depositDTO.getPin(), depositDTO.getAmount());

            return ResponseEntity.status(HttpStatus.OK).body("ok");

        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }

    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> withdraw(@RequestBody WithdrawDTO withdrawDTO) {
        try{
            actionService.withdraw(withdrawDTO.getId(), withdrawDTO.getPin(), withdrawDTO.getAmount());

            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> transfer(@RequestBody TransferDTO transferDTO) {
        try {
            actionService.transfer(transferDTO.getId(), transferDTO.getPin(), transferDTO.getTargetId(), transferDTO.getAmount());

            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}

