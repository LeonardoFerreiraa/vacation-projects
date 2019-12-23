package br.com.leonardoferreira.primavera.web.response.handler;

import br.com.leonardoferreira.primavera.web.response.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseErrorEntity {

    private final String message;

    @Builder.Default
    private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    @Builder.Default
    private final int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode();

    @Builder.Default
    private final long timestamp = System.currentTimeMillis();

    private final String path;

}
