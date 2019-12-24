package br.com.leonardoferreira.primavera.web.exception;

import br.com.leonardoferreira.primavera.web.response.HttpStatus;
import lombok.Getter;

@Getter
public class HttpException extends RuntimeException {

    private final HttpStatus status;

    public HttpException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

}
