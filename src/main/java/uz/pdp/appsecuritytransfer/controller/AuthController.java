package uz.pdp.appsecuritytransfer.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.appsecuritytransfer.payload.LoginDTO;
import uz.pdp.appsecuritytransfer.security.JwtProvider;
import uz.pdp.appsecuritytransfer.service.MyAuthService;


@RequestMapping("/api")
@RestController
public class AuthController {

    final MyAuthService myAuthService;
    final JwtProvider jwtProvider;
    final PasswordEncoder passwordEncoder;
    final AuthenticationManager authenticationManager;

    public AuthController(MyAuthService myAuthService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.myAuthService = myAuthService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/login")
    public HttpEntity<?> logInToSystem(@RequestBody LoginDTO loginDTO) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            String token = jwtProvider.generateToken(loginDTO.getUsername());
            return ResponseEntity.ok(token);
        }catch (BadCredentialsException exception){
            return ResponseEntity.status(401).body("Invalid Login or password!");
        }
    }
}
