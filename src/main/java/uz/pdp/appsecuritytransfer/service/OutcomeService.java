package uz.pdp.appsecuritytransfer.service;

import org.springframework.stereotype.Service;
import uz.pdp.appsecuritytransfer.entity.Card;
import uz.pdp.appsecuritytransfer.entity.Income;
import uz.pdp.appsecuritytransfer.entity.Outcome;
import uz.pdp.appsecuritytransfer.payload.ApiResponse;
import uz.pdp.appsecuritytransfer.payload.OutcomeDTO;
import uz.pdp.appsecuritytransfer.repository.CardRepository;
import uz.pdp.appsecuritytransfer.repository.IncomeRepository;
import uz.pdp.appsecuritytransfer.repository.OutcomeRepository;
import uz.pdp.appsecuritytransfer.security.JwtProvider;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OutcomeService {

    final OutcomeRepository outcomeRepository;
    final CardRepository cardRepository;
    final JwtProvider jwtProvider;
    final IncomeRepository incomeRepository;

    public OutcomeService(OutcomeRepository outcomeRepository,
                          CardRepository cardRepository,
                          JwtProvider jwtProvider,
                          IncomeRepository incomeRepository) {
        this.outcomeRepository = outcomeRepository;
        this.cardRepository = cardRepository;
        this.jwtProvider = jwtProvider;
        this.incomeRepository = incomeRepository;
    }


    public ApiResponse add(OutcomeDTO outcomeDTO, HttpServletRequest httpServletRequest) {

        Optional<Card> optionalCardTo = cardRepository.findById(outcomeDTO.getToCardId());

        if (!optionalCardTo.isPresent()) return new ApiResponse("Sender not found!", false);

        Optional<Card> optionalCardFrom = cardRepository.findById(outcomeDTO.getFromCardId());

        if (!optionalCardFrom.isPresent()) return new ApiResponse("Receiver card not found!", false);

        Card cardFrom = optionalCardFrom.get();
        Card cardTo = optionalCardTo.get();

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        if (!userName.equals(cardFrom.getUsername())) return
                new ApiResponse("Invalid card ID", false);

        Double balance = cardFrom.getBalance();
        Double totalAmount = outcomeDTO.getAmount() + (outcomeDTO.getAmount() / 100 * outcomeDTO.getCommissionPercent());
        if (balance < totalAmount) return new ApiResponse("Balance is not sufficient", false);


        Outcome outcome = new Outcome();
        Income income = new Income();

        outcome.setAmount(outcomeDTO.getAmount());
        outcome.setCommissionPercent(outcomeDTO.getCommissionPercent());
        outcome.setFromCardId(cardFrom);
        outcome.setToCardId(cardTo);
        outcome.setDate(new Date());
        outcomeRepository.save(outcome);

        income.setAmount(outcomeDTO.getAmount());
        income.setDate(new Date());
        income.setFromCardId(cardFrom);
        income.setToCardId(cardTo);
        incomeRepository.save(income);

        cardFrom.setBalance(balance - totalAmount);
        cardTo.setBalance(cardTo.getBalance() + outcomeDTO.getAmount());
        cardRepository.save(cardFrom);
        cardRepository.save(cardTo);


        return new ApiResponse("Transaction successfully sent.", true);
    }


    public List<Outcome> getAll(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        List<Outcome> userOutcome = new ArrayList<>();

        List<Outcome> all = outcomeRepository.findAll();
        for (Outcome outcome : all) {
            if (outcome.getFromCardId().getUsername().equals(userName))
                userOutcome.add(outcome);
        }
        return userOutcome;
    }

    public ApiResponse getOne(Integer id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Outcome> optionalOutcome = outcomeRepository.findById(id);

        if (!optionalOutcome.isPresent()) return new ApiResponse("Invalid Outcome ID", false);

        if (!userName.equals(optionalOutcome.get().getFromCardId().getUsername()))
            return new ApiResponse("Invalid outcome ID!", false);
        return new ApiResponse("", true, optionalOutcome.get());
    }

}
