package com.som.in.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestResponseException {
    private int status;
    private String message;
}
