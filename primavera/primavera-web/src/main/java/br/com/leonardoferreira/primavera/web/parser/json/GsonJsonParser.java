package br.com.leonardoferreira.primavera.web.parser.json;

import br.com.leonardoferreira.primavera.stereotype.Component;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class GsonJsonParser implements JsonParser {

    private final Gson gson;

    public GsonJsonParser(final Gson gson) {
        this.gson = new Gson();
    }

    @Override
    public <T> T fromJson(final InputStream inputStream, final Class<T> type) {
        final InputStreamReader reader = new InputStreamReader(inputStream);
        return gson.fromJson(reader, type);
    }

    @Override
    public String toJson(final Object object) {
        return gson.toJson(object);
    }
}
