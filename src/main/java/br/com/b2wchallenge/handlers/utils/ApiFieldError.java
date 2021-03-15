package br.com.b2wchallenge.handlers.utils;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiFieldError {
    private String field;
    private String code;
    private String mensage;
    private Object rejectedValue;
}
