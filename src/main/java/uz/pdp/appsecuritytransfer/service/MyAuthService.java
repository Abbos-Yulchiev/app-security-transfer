package uz.pdp.appsecuritytransfer.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MyAuthService implements UserDetailsService {

    final PasswordEncoder passwordEncoder;

    public MyAuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> userList = new ArrayList<>(
                Arrays.asList(
                        new User("user1", passwordEncoder.encode("password1"),  new ArrayList<>()),
                        new User("user2", passwordEncoder.encode("password2"),  new ArrayList<>()),
                        new User("user3", passwordEncoder.encode("password3"),  new ArrayList<>())
                )
        );
        for (User user : userList) {
            if (user.getUsername().equals(username)) return user;
        }
        throw new UsernameNotFoundException("User not found!");
    }
}
