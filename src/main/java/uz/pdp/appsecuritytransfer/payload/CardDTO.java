package uz.pdp.appsecuritytransfer.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {

    private long cardNumber;
    private Date expiredDate;
}
