package noncom.bank.bank.model.dto;

import lombok.Data;

@Data
public class WithdrawDTO {
    private int id;
    private int pin;
    private double amount;
}
