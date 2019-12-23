package br.com.leonardoferreira.primavera.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    OK(200),
    CREATED(201),
    ACCEPT(202),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    NOT_FOUND(404);

    private final int statusCode;

}
