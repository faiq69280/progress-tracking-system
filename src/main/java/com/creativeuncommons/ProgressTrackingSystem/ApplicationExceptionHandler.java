package com.creativeuncommons.ProgressTrackingSystem;

import com.creativeuncommons.ProgressTrackingSystem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class ApplicationExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
           ResourceNotFoundResponse errorResponse = new ResourceNotFoundResponse(ex.getMessage());
           return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SaveFailureException.class)
    public ResponseEntity<SaveFailureResponse> handleMessageNotSavedResponse(
            SaveFailureException ex
    ){
        SaveFailureResponse<?> response = new SaveFailureResponse<>(ex.getMetaData(),ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UnknownExceptionResponse> handleUnknownException(
            Exception ex
    ){
        UnknownExceptionResponse errorResponse = new UnknownExceptionResponse("Unhandled Exception",ex.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<UnknownExceptionResponse> handleSQLException(SQLException sqle){
        UnknownExceptionResponse errorResponse = new UnknownExceptionResponse("SQL error",sqle.getMessage());

        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
