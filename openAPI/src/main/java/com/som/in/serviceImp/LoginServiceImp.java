package com.som.in.serviceImp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.som.in.dto.AuthenticationRequest;
import com.som.in.dto.AuthenticationResponse;
import com.som.in.dto.ChangePasswordRequest;
import com.som.in.exceptions.CustomEntryException;
import com.som.in.model.User;
import com.som.in.repository.UserRepository;
import com.som.in.service.LoginService;
import com.som.in.utils.CloudeService;
import com.som.in.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;


@Service
@RequiredArgsConstructor
public class LoginServiceImp implements LoginService {
    private final CloudeService cloudeService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(()->new CustomEntryException("User Not Found"));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        try{
            authenticationManager.authenticate( usernamePasswordAuthenticationToken);
        }catch (Exception e){
            throw new CustomEntryException("invalid password");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        cloudeService.revokeAllUserTokens(user);
        cloudeService.saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new CustomEntryException("Please add token");
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = repository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                cloudeService.revokeAllUserTokens(user);
                cloudeService.saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomEntryException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new CustomEntryException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

}
