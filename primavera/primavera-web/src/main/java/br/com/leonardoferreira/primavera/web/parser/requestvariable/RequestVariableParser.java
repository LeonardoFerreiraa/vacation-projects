package br.com.leonardoferreira.primavera.web.parser.requestvariable;

public class RequestVariableParser<T> {

    public static Object parse(Class<?> type, String value) {
        if (value == null) {
            return null;
        } else if (type.equals(String.class)) {
            return value;
        } else if (type.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        }

        return value;
    }

}
