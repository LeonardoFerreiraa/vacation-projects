package br.com.leonardoferreira.primavera.web.resolver;

import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import br.com.leonardoferreira.primavera.stereotype.Component;
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
        return AnnotationUtils.isAnnotationPresent(parameter, RequestBody.class);
    }

    @Override
    public Object resolve(final Parameter parameter, final HttpServletRequest request, final HttpServletResponse response) {
        try (final InputStream inputStream = request.getInputStream()) {
            return jsonParser.fromJson(inputStream, parameter.getType());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
