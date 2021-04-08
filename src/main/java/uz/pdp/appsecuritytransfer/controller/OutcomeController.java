package uz.pdp.appsecuritytransfer.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appsecuritytransfer.payload.ApiResponse;
import uz.pdp.appsecuritytransfer.payload.OutcomeDTO;
import uz.pdp.appsecuritytransfer.service.OutcomeService;


import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/outcome")
public class OutcomeController {

    final OutcomeService service;

    public OutcomeController(OutcomeService service) {
        this.service = service;
    }

    @PostMapping
    public HttpEntity<ApiResponse> add(@RequestBody OutcomeDTO cardDTO, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.add(cardDTO, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(service.getAll(httpServletRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(service.getOne(id, httpServletRequest));
    }
}
