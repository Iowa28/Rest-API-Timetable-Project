package ru.aminovniaz.testtask.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.aminovniaz.testtask.exception.EntityNotFoundException;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class, IllegalArgumentException.class})
    protected ResponseEntity<?> handleConflict(RuntimeException ex, WebRequest request) {
        System.err.println("Exception info: " + request.getDescription(true));

        String bodyOfResponse = "Ошибка! Сущность не найдена. Проверьте, правильно ли введен адрес: " +
                request.getDescription(false);
        ResponseEntity<String> response = ResponseEntity.badRequest().body(bodyOfResponse);
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
