package br.com.leonardoferreira.primavera.util;

public class StringUtils {

    public static boolean isBlank(final String str) {
        return str == null || str.isBlank();
    }

    public static boolean isNotBlank(final String str) {
        return !isBlank(str);
    }

}
