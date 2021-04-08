package uz.pdp.appsecuritytransfer.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appsecuritytransfer.payload.LoginDTO;


@RestController
@RequestMapping("/api/report")
public class ReportController {

    @GetMapping
    public HttpEntity<?> getReports(){
        return ResponseEntity.ok("Report sent!");
    }

    @PostMapping("/test")
    public HttpEntity<?> addTest(@RequestBody LoginDTO loginDTO){

        System.out.println(loginDTO);
        return ResponseEntity.ok(loginDTO);
    }
}
