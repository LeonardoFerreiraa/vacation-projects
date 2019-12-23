package br.com.leonardoferreira.primavera.web.resolver;

import br.com.leonardoferreira.primavera.primavera.annotation.AnnotationFinder;
import br.com.leonardoferreira.primavera.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.web.annotation.RequestBody;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestBodyMethodArgumentResolver implements MethodArgumentResolver {

    private final Gson gson;

    public RequestBodyMethodArgumentResolver() {
        this.gson = new Gson();
    }

    @Override
    public boolean canResolve(final Parameter parameter) {
        return AnnotationFinder.isAnnotationPresent(parameter, RequestBody.class);
    }

    @Override
    public Object resolve(final Parameter parameter, final HttpServletRequest req, final HttpServletResponse resp) {
        try (var is = req.getInputStream()) {
            final InputStreamReader reader = new InputStreamReader(is);
            return gson.fromJson(reader, parameter.getType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
