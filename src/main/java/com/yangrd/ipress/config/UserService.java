package com.yangrd.ipress.config;

import com.yangrd.ipress.config.security.jwt.JwtTokenProvider;
import com.yangrd.ipress.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * UserService
 *
 * @author yangrd
 * @date 2019/04/24
 */
@AllArgsConstructor

@Service
public class UserService {

    private JwtTokenProvider jwtTokenProvider;

    private UserDetailsService userDetailsService;

    private AuthenticationManager authenticationManager;

    public String signin(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return refresh(username);
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String refresh(String username) {
        return jwtTokenProvider.createToken(username, userDetailsService.loadUserByUsername(username).getAuthorities());
    }


}
