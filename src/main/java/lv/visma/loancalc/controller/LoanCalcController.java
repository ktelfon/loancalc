package lv.visma.loancalc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.visma.loancalc.dto.LoanResultDto;
import lv.visma.loancalc.service.LoanCalcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoanCalcController {

    private final LoanCalcService loanCalcService;

    @GetMapping
    public List<LoanResultDto> getLoanCalculation(String loanName, double amount, int years) {
        log.debug("Received a request for {} amount: {} years: {}", loanName, amount, years);
        return loanCalcService.calculateLoan(loanName, amount, years);
    }
}
