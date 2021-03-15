package br.com.b2wchallenge.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.b2wchallenge.exceptions.PlanetAlreadyExistsException;
import br.com.b2wchallenge.exceptions.PlanetNotFoundException;
import br.com.b2wchallenge.handlers.utils.ApiErrorsView;
import br.com.b2wchallenge.handlers.utils.ApiFieldError;
import br.com.b2wchallenge.handlers.utils.ApiGlobalError;
import br.com.b2wchallenge.validators.PlanetValidator;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> defaultExceptionHandler(Exception ex, WebRequest request) {
        log.error("defaultExceptionHandler: {}", ex);

        List<ApiGlobalError> globalErrors = new ArrayList<>();
        globalErrors.add(ApiGlobalError.builder()
            .mensage(ex.getLocalizedMessage())
            .build()
        );

        return buildResponseEntity(globalErrors, null, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PlanetNotFoundException.class)
    public final ResponseEntity<Object> planetNotFoundExceptionHandler(PlanetNotFoundException ex, WebRequest request) {
        log.error("planetNotFoundException: {}", ex);

        List<ApiGlobalError> globalErrors = new ArrayList<>();
        globalErrors.add(ApiGlobalError.builder()
            .code(HttpStatus.NOT_FOUND.name())
            .mensage(ex.getMessage())
            .build()
        );

        return buildResponseEntity(globalErrors, null, request, HttpStatus.NOT_FOUND); 
    }

    @ExceptionHandler(PlanetAlreadyExistsException.class)
    public final ResponseEntity<Object> planetAlreadyExistsExceptionHandler(PlanetAlreadyExistsException ex, WebRequest request) {
        log.error("planetAlreadyExistsException: {}", ex);

        List<ApiFieldError> errors = new ArrayList<>();
         errors.add(ApiFieldError.builder()
            .code(PlanetValidator.FIELD_ALREADY_EXISTS)
            .field("planet")
            .rejectedValue(ex.getPlanet())
            .mensage(ex.getMessage())
            .build()
        );

        return buildResponseEntity(null, errors, request, HttpStatus.CONFLICT); 
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleMethodArgumentNotValid: {}", ex);

        BindingResult bindingResult = ex.getBindingResult();
        List<ApiFieldError> fieldErrors = bindingResult.getFieldErrors().stream()
            .map(fieldError -> ApiFieldError.builder()
                .field(fieldError.getField())
                .mensage(fieldError.getDefaultMessage())
                .code(fieldError.getCode())
                .rejectedValue(fieldError.getRejectedValue())
                .build()
            ).collect(Collectors.toList());

        List<ApiGlobalError> globalErrors = bindingResult.getGlobalErrors().stream()
            .map(globalError -> ApiGlobalError.builder()
                .code(globalError.getCode())
                .mensage(globalError.getDefaultMessage())
                .build()
            ).collect(Collectors.toList());

        return buildResponseEntity(globalErrors, fieldErrors, request, HttpStatus.UNPROCESSABLE_ENTITY); 
        
        
    }

    private ResponseEntity<Object> buildResponseEntity(List<ApiGlobalError> globalErrors,
            List<ApiFieldError> fieldErrors, WebRequest request, HttpStatus status) {

        return new ResponseEntity<>(ApiErrorsView.builder()
                .fieldErrors(fieldErrors)
                .globalErrors(globalErrors)
                .path(request.getDescription(false))
                .build(),
            status
        );
    }
}
