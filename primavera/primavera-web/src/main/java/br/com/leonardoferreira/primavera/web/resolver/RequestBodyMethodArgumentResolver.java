package br.com.leonardoferreira.primavera.web.resolver;

import br.com.leonardoferreira.primavera.primavera.annotation.AnnotationFinder;
import br.com.leonardoferreira.primavera.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.web.request.RequestBody;
import br.com.leonardoferreira.primavera.web.parser.json.JsonParser;
import java.io.InputStream;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestBodyMethodArgumentResolver implements MethodArgumentResolver {

    private final JsonParser jsonParser;

    public RequestBodyMethodArgumentResolver(final JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    @Override
    public boolean canResolve(final Parameter parameter) {
        return AnnotationFinder.isAnnotationPresent(parameter, RequestBody.class);
    }

    @Override
    public Object resolve(final Parameter parameter, final HttpServletRequest req, final HttpServletResponse resp) {
        try (final InputStream inputStream = req.getInputStream()) {
            return jsonParser.fromJson(inputStream, parameter.getType());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
