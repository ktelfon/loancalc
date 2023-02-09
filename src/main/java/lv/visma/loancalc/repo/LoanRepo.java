package lv.visma.loancalc.repo;

import lv.visma.loancalc.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepo extends JpaRepository<Loan, Integer> {
    Optional<Loan> findByName(String loanName);
}
