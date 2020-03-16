package com.springboot.api.controllers;

import java.time.format.DateTimeParseException;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.springboot.api.entities.dtos.ResponseDTO;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * ExceptionHandlerController
 */
@ControllerAdvice
@RestController
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(NoHandlerFoundException.class)
     public String handle(Exception ex) {

        return "404";//this is view name
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<?>> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO<>().addError(ex.getClass().getName() + " - "+ex.getMessage()));
    }
   
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ResponseDTO<?>> handleDateTimeParseException(DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseDTO<>().addError("A data informada está em um formato inválido."));
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        log.error(ex.getMessage(), ex);

        String message = ex.getMessage();

        if (message.contains("PESSOA(CPF)")) {
            message = "O CPF informado já está cadastrado.";
        }
        
        if (message.contains("PESSOA(EMAIL)")) {
            message = "O email informado já está cadastrado.";
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseDTO<>().addError(message));
    }
    
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ResponseDTO<?>> handleInvalidFormatException(InvalidFormatException ex) {

        if (ex.getCause() instanceof DateTimeParseException) {
            return handleDateTimeParseException((DateTimeParseException) ex.getCause());
        } else {
            return handleException(ex);
        }
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        if (ex.getCause() instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) ex.getCause());
        } else {
            return handleException(ex);
        }

    }
   
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO<?>> handleBadCredentialsException(BadCredentialsException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseDTO<>().addError("Credenciais inválidas."));
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDTO<?>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseDTO<>().addError("Acesso negado."));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);

        ResponseDTO<?> responseDTO = new ResponseDTO<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            responseDTO.addError(violation.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseDTO);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<ResponseDTO<?>> handleRollbackException(RollbackException ex) {
        
        if (ex.getCause() instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) ex.getCause());
        } else {
            return handleException(ex);
        }
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ResponseDTO<?>> handleTransactionSystemException(TransactionSystemException ex) {
        
        if (ex.getCause() instanceof RollbackException) {
            return handleRollbackException((RollbackException) ex.getCause());
        } else {
            return handleException(ex);
        }
    }

    

}
