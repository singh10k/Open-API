package com.som.in.service;


import com.som.in.dto.AuthenticationResponse;
import com.som.in.dto.RegisterRequest;

public interface RegisterService {
    AuthenticationResponse register(RegisterRequest request);
}
