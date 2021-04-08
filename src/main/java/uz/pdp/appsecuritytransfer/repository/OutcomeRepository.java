package uz.pdp.appsecuritytransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appsecuritytransfer.entity.Outcome;

public interface OutcomeRepository extends JpaRepository<Outcome, Integer> {
}
