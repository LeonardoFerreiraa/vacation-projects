package br.com.leonardoferreira.primavera.web.parser.json;

import java.io.InputStream;

public interface JsonParser {

    <T> T fromJson(InputStream inputStream, Class<T> type);

    String toJson(Object object);

}
