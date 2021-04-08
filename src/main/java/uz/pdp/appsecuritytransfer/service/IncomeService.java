package uz.pdp.appsecuritytransfer.service;

import org.springframework.stereotype.Service;
import uz.pdp.appsecuritytransfer.entity.Income;
import uz.pdp.appsecuritytransfer.payload.ApiResponse;
import uz.pdp.appsecuritytransfer.repository.CardRepository;
import uz.pdp.appsecuritytransfer.repository.IncomeRepository;
import uz.pdp.appsecuritytransfer.repository.OutcomeRepository;
import uz.pdp.appsecuritytransfer.security.JwtProvider;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    final OutcomeRepository outcomeRepository;
    final CardRepository cardRepository;
    final JwtProvider jwtProvider;
    final IncomeRepository incomeRepository;

    public IncomeService(OutcomeRepository outcomeRepository, CardRepository cardRepository, JwtProvider jwtProvider, IncomeRepository incomeRepository) {
        this.outcomeRepository = outcomeRepository;
        this.cardRepository = cardRepository;
        this.jwtProvider = jwtProvider;
        this.incomeRepository = incomeRepository;
    }


    public List<Income> getAll(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        List<Income> userIncome = new ArrayList<>();

        List<Income> all = incomeRepository.findAll();
        for (Income income : all) {
            if (income.getToCardId().getUsername().equals(userName))
                userIncome.add(income);
        }
        return userIncome;
    }

    public ApiResponse getOne(Integer id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Income> optionalIncome = incomeRepository.findById(id);

        if (!optionalIncome.isPresent()) return new ApiResponse("Invalid income ID code!", false);

        if (!userName.equals(optionalIncome.get().getToCardId().getUsername()))
            return new ApiResponse("Invalid income ID!", false);
        return new ApiResponse("", true, optionalIncome.get());
    }
}
