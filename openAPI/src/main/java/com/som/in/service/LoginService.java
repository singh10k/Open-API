package com.som.in.service;

import com.som.in.dto.AuthenticationRequest;
import com.som.in.dto.AuthenticationResponse;
import com.som.in.dto.ChangePasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.Principal;

public interface LoginService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
