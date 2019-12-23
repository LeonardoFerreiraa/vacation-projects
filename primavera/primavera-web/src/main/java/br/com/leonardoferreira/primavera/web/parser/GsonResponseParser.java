package br.com.leonardoferreira.primavera.web.parser;

import br.com.leonardoferreira.primavera.primavera.stereotype.Component;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.http.HttpServletResponse;

@Component
public class GsonResponseParser implements ResponseParser {

    private final Gson gson;

    public GsonResponseParser() {
        this.gson = new Gson();
    }

    @Override
    public void parse(final Object object, final HttpServletResponse response) {
        response.setContentType("application");

        try (var writer = response.getWriter()) {
            writer.println(gson.toJson(
                    object == null ? Collections.emptyMap() : object
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
