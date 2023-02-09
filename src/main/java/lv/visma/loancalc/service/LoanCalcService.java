package lv.visma.loancalc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.visma.loancalc.dto.LoanResultDto;
import lv.visma.loancalc.model.Loan;
import lv.visma.loancalc.repo.LoanRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoanCalcService {

    public static final int MONTHS_IN_YEAR = 12;

    private final LoanRepo loanRepo;

    public List<LoanResultDto> calculateLoan(String loanName, double amount, int years) {
        List<LoanResultDto> resultDtos = new ArrayList<>();
        Loan loan = getLoan(loanName);
        double monthlyPaybackAmount = amount / (years * MONTHS_IN_YEAR);
        double repaymentSum = amount;
        double interestSize = loan.isYearlyInterest() ? loan.getInterest() / MONTHS_IN_YEAR : loan.getInterest();
        for (int i = 1; i <= years; i++) {
            for (int j = 1; j <= MONTHS_IN_YEAR; j++) {
                double interest = repaymentSum * interestSize;
                resultDtos.add(LoanResultDto.builder()
                        .monthNumber(i * j)
                        .monthlyPayment(monthlyPaybackAmount)
                        .monthlyInterest(interest)
                        .leftToRePay(repaymentSum)
                        .build());
                repaymentSum -= monthlyPaybackAmount;
            }
        }
        return resultDtos;
    }

    private Loan getLoan(String loanName) {
        return loanRepo.findByName(loanName).orElseThrow(
                () -> {
                    log.error("No loan with name: {} found! ", loanName);
                    throw new EntityNotFoundException();
                }
        );
    }
}
