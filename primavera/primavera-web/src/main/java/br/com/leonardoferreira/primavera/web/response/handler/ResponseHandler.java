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

    public void handleResponse(final HttpServletRequest request, final HttpServletResponse response, final Object object) {
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
                request,
                response,
                buildResponseEntity(request, HttpStatus.NOT_FOUND, "Resource Not Found")
        );
    }

    public void handleError(final HttpServletRequest request,
                            final HttpServletResponse response,
                            final Throwable error) {
        final Throwable cause = error.getCause();
        final ResponseError responseError = AnnotationFinder.retrieveAnnotation(cause.getClass(), ResponseError.class);
        if (responseError == null) {
            error.printStackTrace();
            handleResponse(
                    request,
                    response,
                    buildResponseEntity(request, HttpStatus.INTERNAL_SERVER_ERROR, cause.getMessage())
            );
        } else {
            handleResponse(
                    request,
                    response,
                    buildResponseEntity(request, responseError.status(), responseError.message())
            );
        }
    }

    private ResponseEntity<Object> buildResponseEntity(final HttpServletRequest request, final HttpStatus status, final String message) {
        return ResponseEntity.builder()
                .status(status)
                .body(ResponseErrorEntity.builder()
                        .message(message)
                        .status(status)
                        .path(request.getPathInfo())
                        .timestamp(System.currentTimeMillis())
                        .statusCode(status.getStatusCode())
                        .build())
                .build();
    }
}
