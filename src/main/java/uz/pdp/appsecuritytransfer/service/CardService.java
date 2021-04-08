package uz.pdp.appsecuritytransfer.service;

import org.springframework.stereotype.Service;
import uz.pdp.appsecuritytransfer.entity.Card;
import uz.pdp.appsecuritytransfer.payload.ApiResponse;
import uz.pdp.appsecuritytransfer.payload.CardDTO;
import uz.pdp.appsecuritytransfer.repository.CardRepository;
import uz.pdp.appsecuritytransfer.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {


    final CardRepository repository;
    final JwtProvider jwtProvider;

    public CardService(CardRepository repository, JwtProvider jwtProvider) {
        this.repository = repository;
        this.jwtProvider = jwtProvider;
    }

    public ApiResponse add(CardDTO cardDTO, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Card card = new Card();
        card.setUsername(userName);
        card.setCardNumber(cardDTO.getCardNumber());
        card.setExpiredDate(cardDTO.getExpiredDate());
        repository.save(card);
        return new ApiResponse("New Card added", true);
    }

    public ApiResponse edit(Integer id, CardDTO cardDTO, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Card> optionalCard = repository.findById(id);

        if (!optionalCard.isPresent()) return new ApiResponse("Invalid Card ID!", false);
        if (!userName.equals(optionalCard.get().getUsername()))
            return new ApiResponse("Invalid Card ID!", false);

        Card card = optionalCard.get();
        card.setCardNumber(cardDTO.getCardNumber());
        card.setExpiredDate(cardDTO.getExpiredDate());
        repository.save(card);
        return new ApiResponse("Card edited!", true);
    }

    public ApiResponse delete(Integer id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        try {
            Optional<Card> optionalCard = repository.findById(id);
            if (!optionalCard.isPresent()) return new ApiResponse("Invalid Card ID", false);
            if (!userName.equals(optionalCard.get().getUsername()))
                return new ApiResponse("Invalid Card Id!", false);
            repository.deleteById(id);
            return new ApiResponse("Card deleted.", true);
        } catch (Exception e) {
            return new ApiResponse("CArd not found!", false);
        }
    }

    public List<Card> getCard(HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        List<Card> userCards = new ArrayList<>();
        List<Card> all = repository.findAll();
        for (Card card : all) {
            if (card.getUsername().equals(userName))
                userCards.add(card);
        }
        return userCards;
    }


    public Card getOne(Integer id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Card> optionalCard = repository.findById(id);
        if (!optionalCard.isPresent()) return null;
        if (!userName.equals(optionalCard.get().getUsername()))
            return null;
        return optionalCard.orElse(null);
    }
}
