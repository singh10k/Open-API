package com.som.in.serviceImp;

import com.som.in.dto.AuthenticationResponse;
import com.som.in.dto.RegisterRequest;
import com.som.in.exceptions.CustomEntryException;
import com.som.in.model.User;
import com.som.in.repository.UserRepository;
import com.som.in.service.RegisterService;
import com.som.in.utils.CloudeService;
import com.som.in.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImp implements RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final CloudeService cloudeService;
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        repository.findByUsername(request.getUsername())
                .ifPresent(userDetails -> {
                    throw new CustomEntryException("Username already exists: " + request.getUsername());
                });
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        cloudeService.saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


}
