package br.com.b2wchallenge.handlers.utils;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiGlobalError {
    private String code;
    private String mensage;
}
