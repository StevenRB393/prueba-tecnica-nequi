package com.application.prueba.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<String>> handleNotFoundException(NotFoundException ex) {
        logger.error("NotFoundException: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public Mono<ResponseEntity<String>> handleDuplicateResourceException(DuplicateResourceException ex) {
        logger.error("DuplicateResourceException: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<String>> handleBadRequestException(BadRequestException ex) {
        logger.error("BadRequestException: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }

    @ExceptionHandler(InvalidDataException.class)
    public Mono<ResponseEntity<String>> handleInvalidDataException(InvalidDataException ex) {
        logger.error("InvalidDataException: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage()));
    }

    @ExceptionHandler(ResourceInUseException.class)
    public Mono<ResponseEntity<String>> handleResourceInUseException(ResourceInUseException ex) {
        logger.error("ResourceInUseException: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()));
    }

    @ExceptionHandler(NoDataFoundException.class)
    public Mono<ResponseEntity<String>> handleNoDataFoundException(NoDataFoundException ex) {
        logger.error("NoDataFoundException: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage()));
    }
}
