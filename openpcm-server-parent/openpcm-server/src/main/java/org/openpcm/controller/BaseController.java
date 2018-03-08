package org.openpcm.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.exceptions.OpenPCMServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {

    @ExceptionHandler(DataViolationException.class)
    public void dataViolationException(DataViolationException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public void dataViolationException(NotFoundException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(OpenPCMServiceException.class)
    public void dataViolationException(OpenPCMServiceException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void constraintViolationException(DataIntegrityViolationException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    protected void fillResponse(HttpServletResponse response, Exception e) {
        response.setHeader("ERROR_MESSAGE", e.getMessage());
        response.setHeader("ERROR_MESSAGE_CLASS", e.getClass().getName());
    }
}
