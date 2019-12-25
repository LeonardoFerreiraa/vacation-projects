package br.com.leonardoferreira.primavera.web.request.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;

@Data
public class RequestPath {

    private final String originalPath;

    private final String regex;

    private final Pattern pattern;

    private RequestPath(final String path) {
        this.originalPath = path;
        this.regex = toRegex(normalizePath(path));
        this.pattern = Pattern.compile(regex);
    }

    public static RequestPath fromPath(final String path) {
        return new RequestPath(path);
    }

    private String toRegex(final String path) {
        final String normalizedPath = normalizePath(path);
        return normalizedPath.replaceAll(":\\w+/", "(?<$0>\\\\w+)/")
                .replaceAll(":", "")
                .replaceAll("/>", ">");
    }

    private String normalizePath(final String path) {
        final StringBuilder sb = new StringBuilder();
        if (!path.startsWith("/")) {
            sb.append("/");
        }
        sb.append(path);
        if (!path.endsWith("/")) {
            sb.append("/");
        }

        return sb.toString();
    }

    public boolean matches(final String path) {
        return normalizePath(path).matches(regex);
    }

    public String pathVariable(final String name, final String path) {
        final Matcher matcher = pattern.matcher(normalizePath(path));
        return matcher.find() ? matcher.group(name) : null;
    }

}
