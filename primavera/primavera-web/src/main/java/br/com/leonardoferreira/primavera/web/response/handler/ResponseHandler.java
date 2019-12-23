package br.com.leonardoferreira.primavera.web.response.handler;

import br.com.leonardoferreira.primavera.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.web.parser.json.JsonParser;
import br.com.leonardoferreira.primavera.web.response.ResponseEntity;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;

@Component
public class ResponseHandler {

    private final JsonParser jsonParser;

    public ResponseHandler(final JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public void parse(final Object object, final HttpServletResponse response) {
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

    public void notFound(final HttpServletResponse response) {
        parse(ResponseEntity.notFound(), response);
    }
}
