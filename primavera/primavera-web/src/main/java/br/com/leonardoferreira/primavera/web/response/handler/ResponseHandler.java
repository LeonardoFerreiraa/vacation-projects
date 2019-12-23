package br.com.leonardoferreira.primavera.web.response.handler;

import br.com.leonardoferreira.primavera.primavera.annotation.AnnotationFinder;
import br.com.leonardoferreira.primavera.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.web.error.handler.ResponseError;
import br.com.leonardoferreira.primavera.web.parser.json.JsonParser;
import br.com.leonardoferreira.primavera.web.response.HttpStatus;
import br.com.leonardoferreira.primavera.web.response.ResponseEntity;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ResponseHandler {

    private final JsonParser jsonParser;

    public ResponseHandler(final JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public void handleResponse(final Object object, final HttpServletResponse response) {
        response.setContentType("application/json");

        Object body = object;
        if (object instanceof ResponseEntity) {
            final ResponseEntity<?> responseEntity = (ResponseEntity<?>) object;

            if (responseEntity.getLocation() != null) {
                response.addHeader("location", responseEntity.getLocation().toString());
            }

            body = responseEntity.getBody();
            response.setStatus(responseEntity.getStatus().getStatusCode());
        }

        body = Objects.requireNonNullElse(body, Collections.emptyMap());

        try (var writer = response.getWriter()) {
            writer.println(jsonParser.toJson(body));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleNotFound(final HttpServletRequest request, final HttpServletResponse response) {
        handleResponse(
                ResponseEntity.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                                ResponseErrorEntity.builder()
                                        .message("Resource Not Found")
                                        .status(HttpStatus.NOT_FOUND)
                                        .path(request.getPathInfo())
                                        .timestamp(System.currentTimeMillis())
                                        .statusCode(HttpStatus.NOT_FOUND.getStatusCode())
                                        .build()
                        )
                        .build(),
                response
        );
    }

    public void handleError(final HttpServletRequest request,
                            final HttpServletResponse response,
                            final Throwable error) {
        try {
            final ResponseError responseError = AnnotationFinder.retrieveAnnotation(error.getClass(), ResponseError.class);
            if (responseError == null) {
                error.printStackTrace();
                handleResponse(
                        ResponseEntity.builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                        ResponseErrorEntity.builder()
                                                .message(error.getMessage())
                                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .path(request.getPathInfo())
                                                .timestamp(System.currentTimeMillis())
                                                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode())
                                                .build()
                                )
                                .build(),
                        response
                );
            } else {
                handleResponse(
                        ResponseEntity.builder()
                                .status(responseError.status())
                                .body(
                                        ResponseErrorEntity.builder()
                                                .message(responseError.message())
                                                .status(responseError.status())
                                                .path(request.getPathInfo())
                                                .timestamp(System.currentTimeMillis())
                                                .statusCode(responseError.status().getStatusCode())
                                                .build()
                                )
                                .build(),
                        response
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
