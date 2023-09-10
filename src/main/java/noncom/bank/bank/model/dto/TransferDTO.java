package noncom.bank.bank.model.dto;

import lombok.Data;

@Data
public class TransferDTO {
    private int id;
    private int pin;
    private int targetId;
    private double amount;
}
