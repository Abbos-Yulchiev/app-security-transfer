package uz.pdp.appsecuritytransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appsecuritytransfer.entity.Income;


public interface IncomeRepository extends JpaRepository<Income, Integer> {
}
