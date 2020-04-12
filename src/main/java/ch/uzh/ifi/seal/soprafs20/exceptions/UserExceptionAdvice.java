package ch.uzh.ifi.seal.soprafs20.exceptions;

import ch.uzh.ifi.seal.soprafs20.exceptions.user.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.user.UserUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
public class UserExceptionAdvice extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(UserExceptionAdvice.class);

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundExceptionException(UserNotFoundException ex, WebRequest request) {
        APIError error = new APIError();
        error.setError(ex.getTitle())
             .setMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserUnauthorizedException.class)
    protected ResponseEntity<Object> handleUserUnauthorizedExceptionException(
            UserUnauthorizedException ex, WebRequest request) {
        APIError error = new APIError();
        error.setError(ex.getTitle())
             .setMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

}