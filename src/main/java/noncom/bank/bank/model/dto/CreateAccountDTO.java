package noncom.bank.bank.model.dto;

import lombok.Data;

@Data
public class CreateAccountDTO {
    private String name;

//    @Pattern(regexp = "\\d{4}", message = "PIN must consist of exactly 4 digits")
    private int pin;

}

