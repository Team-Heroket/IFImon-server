package ch.uzh.ifi.seal.soprafs20.exceptions;

import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameConflictException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameForbiddenException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.user.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.user.UserUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(annotations = RestController.class)
public class GameExceptionAdvice extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GameExceptionAdvice.class);

    @ExceptionHandler(GameConflictException.class)
    protected ResponseEntity<Object> handleUserGameConflictExceptionException(IFImonException ex, WebRequest request) {
        APIError error = new APIError();
        error.setError(ex.getTitle())
             .setMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(GameNotFoundException.class)
    protected ResponseEntity<Object> handleUserGameNotFoundExceptionException(IFImonException ex, WebRequest request) {
        APIError error = new APIError();
        error.setError(ex.getTitle())
                .setMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(GameForbiddenException.class)
    protected ResponseEntity<Object> handleUserGameForbiddenExceptionException(IFImonException ex, WebRequest request) {
        APIError error = new APIError();
        error.setError(ex.getTitle())
                .setMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(GameBadRequestException.class)
    protected ResponseEntity<Object> handleUserGameBadRequestExceptionException(IFImonException ex, WebRequest request) {
        APIError error = new APIError();
        error.setError(ex.getTitle())
                .setMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}