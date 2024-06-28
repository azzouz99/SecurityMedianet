package com.example.securitymedianet.auth;

import com.example.securitymedianet.Entites.User;
import com.example.securitymedianet.Repositories.RoleRepository;
import com.example.securitymedianet.Repositories.UserRepository;
import com.example.securitymedianet.config.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;


    public AuthenticationResponse register(RegisterRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER").orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))


                .roles(List.of(userRole))
                .build();
       repository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
/*
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    } */
public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    var claims = new HashMap<String, Object>();
    var user = ((User) auth.getPrincipal());
    claims.put("id", user.getId());
    claims.put("firstName", user.getFirstname());
    claims.put("lastName", user.getLastname());
    claims.put("email", user.getEmail());
    claims.put("image", user.getImage());


    var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
}


    public List<User> getAllUser(){
        return repository.findAll();
    }


    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
