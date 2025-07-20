package com.safalifter.authservice.service;

import com.safalifter.authservice.client.UserServiceClient;
import com.safalifter.authservice.dto.RegisterDto;
import com.safalifter.authservice.dto.TokenDto;
import com.safalifter.authservice.exc.WrongCredentialsException;
import com.safalifter.authservice.request.LoginRequest;
import com.safalifter.authservice.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;

    public TokenDto login(LoginRequest request) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authenticate.isAuthenticated())
            return TokenDto
                    .builder()
                    .token(jwtService.generateToken(request.getUsername()))
                    .build();
        else throw new WrongCredentialsException("Wrong credentials");
    }

    public RegisterDto register(RegisterRequest request) {
        log.info("Registering user with username: {}", request.getUsername());
        return userServiceClient.save(request).getBody();
    }
}
