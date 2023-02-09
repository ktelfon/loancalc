package lv.visma.loancalc.dto;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class LoanResultDto {
    int monthNumber;
    double monthlyPayment;
    double monthlyInterest;
    double leftToRePay;
}
