package org.openpcm.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.FileEmptyException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.exceptions.OpenPCMServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;

public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(DataViolationException.class)
    public void dataViolationException(DataViolationException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public void notFoundException(NotFoundException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(OpenPCMServiceException.class)
    public void openPCMServiceException(OpenPCMServiceException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void dataIntegrityViolationException(DataIntegrityViolationException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    @ExceptionHandler(FileEmptyException.class)
    public void fileEmptyException(FileEmptyException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public void ioException(IOException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public void expiredJwtException(ExpiredJwtException e, HttpServletResponse response) throws IOException {
        fillResponse(response, e);
        response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    protected void fillResponse(HttpServletResponse response, Exception e) {
        LOGGER.warn(e.getMessage(), e);
        response.setHeader("ERROR_MESSAGE", e.getMessage());
        response.setHeader("ERROR_MESSAGE_CLASS", e.getClass().getName());
    }
}
