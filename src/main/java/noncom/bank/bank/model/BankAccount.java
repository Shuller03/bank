package noncom.bank.bank.model;

import lombok.Data;

@Data
public class BankAccount {

    //id is accountNumber
    private Integer id;
    private String name;
    private Integer pin;
    private double balance;

}

