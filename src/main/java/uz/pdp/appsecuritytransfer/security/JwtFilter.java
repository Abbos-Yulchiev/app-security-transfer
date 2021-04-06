package uz.pdp.appsecuritytransfer.security;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.appsecuritytransfer.service.MyAuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    final JwtProvider jwtProvider;
    final MyAuthService myAuthService;

    public JwtFilter(JwtProvider jwtProvider, @Lazy MyAuthService myAuthService) {
        this.jwtProvider = jwtProvider;
        this.myAuthService = myAuthService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        /*Requestdan tokeni olish*/
        String token = httpServletRequest.getHeader("Authorization");
        System.out.println(token);

        /*Tokening borligini va tokenning boshlanishini Bearer mavjutligini tekshiryapmiz*/
        if (token != null && token.startsWith("Bearer")) {

            /*Aynan tokeni o'zini qirqib oldik*/
            token = token.substring(7);

            /*Tokeni validatsiyadan o'tlazdik(Token buzulmaganliginin, ishlash muddati yugamanganligini va h.k)*/
            boolean validationToken = jwtProvider.validationToken(token);
            if (validationToken) {

                /*Tokeni ichidan usernamini olamiz*/
                String username = jwtProvider.getUsernameFromToken(token);

                /*Usernamedan userDetails oldik*/
                UserDetails userDetails = myAuthService.loadUserByUsername(username);

                /*userDetails orqali  Authentication yaratib oldik*/
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                System.out.println(SecurityContextHolder.getContext().getAuthentication());

                /*Systemaga kim kirganligini o'rnatib qo'ydik*/
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
