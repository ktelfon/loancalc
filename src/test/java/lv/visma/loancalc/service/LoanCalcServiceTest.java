package lv.visma.loancalc.service;

import jakarta.persistence.EntityNotFoundException;
import lv.visma.loancalc.dto.LoanResultDto;
import lv.visma.loancalc.model.Loan;
import lv.visma.loancalc.repo.LoanRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = LoanCalcService.class)
class LoanCalcServiceTest {

    @MockBean
    LoanRepo loanRepo;

    @Autowired
    LoanCalcService loanCalcService;

    private static Stream<Arguments> monthlyData() {
        return Stream.of(
                Arguments.of(166.66, 2916.66, Boolean.TRUE),
                Arguments.of(166.66, 35000.00, Boolean.FALSE)
        );
    }

    @ParameterizedTest
    @MethodSource("monthlyData")
    void calculateLoanHappyPath(double monthlyPayment, double monthlyInterest, boolean isYearlyInterest) {
        String housingLoan = "HousingLoan";
        when(loanRepo.findByName(any()))
                .thenReturn(Optional.of(Loan.builder()
                        .id(1)
                        .interest(3.5)
                        .name(housingLoan)
                        .yearlyInterest(isYearlyInterest)
                        .build()));
        List<LoanResultDto> resultDtos = loanCalcService.calculateLoan(housingLoan, 10_000, 5);
        assertNotNull(resultDtos);
        assertTrue(resultDtos.size() > 0);
        assertEquals(monthlyPayment, resultDtos.get(0).getMonthlyPayment(), 0.01);
        assertEquals(monthlyInterest, resultDtos.get(0).getMonthlyInterest(), 0.01);

    }

    @Test
    void calculateLoanNoName() {
        when(loanRepo.findByName(any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> loanCalcService.calculateLoan("", 1, 1));
    }

}