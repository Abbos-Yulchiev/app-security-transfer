package uz.pdp.appsecuritytransfer.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutcomeDTO {

    private Integer fromCardId;

    private Integer toCardId;

    private double amount;

    private double commissionPercent;
}
