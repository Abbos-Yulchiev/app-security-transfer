package uz.pdp.appsecuritytransfer.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appsecuritytransfer.entity.Card;
import uz.pdp.appsecuritytransfer.payload.ApiResponse;
import uz.pdp.appsecuritytransfer.payload.CardDTO;
import uz.pdp.appsecuritytransfer.security.JwtProvider;
import uz.pdp.appsecuritytransfer.service.CardService;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/card")
@RestController
public class CardController {

    final CardService service;
    final JwtProvider jwtProvider;

    public CardController(CardService service, JwtProvider jwtProvider) {
        this.service = service;
        this.jwtProvider = jwtProvider;
    }


    @PostMapping
    public HttpEntity<ApiResponse> add(@RequestBody CardDTO cardDTO, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.add(cardDTO, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 209).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(service.getCard(httpServletRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getOne(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(service.getOne(id, httpServletRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> edit(@RequestBody CardDTO cardDTO,
                                            @PathVariable Integer id, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.edit(id, cardDTO, httpServletRequest);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.status(200).body(apiResponse);
        } else {
            return ResponseEntity.status(409).body(apiResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        ApiResponse response = service.delete(id, httpServletRequest);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

}
