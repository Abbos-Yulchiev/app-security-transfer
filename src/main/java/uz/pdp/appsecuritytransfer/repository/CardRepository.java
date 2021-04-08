package uz.pdp.appsecuritytransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appsecuritytransfer.entity.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {

    boolean existsByCardNumber(long cardNumber);
}
