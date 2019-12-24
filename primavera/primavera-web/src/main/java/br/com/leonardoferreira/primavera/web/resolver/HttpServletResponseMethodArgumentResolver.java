package br.com.leonardoferreira.primavera.web.resolver;

import br.com.leonardoferreira.primavera.stereotype.Component;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpServletResponseMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean canResolve(final Parameter parameter) {
        return parameter.getType().equals(HttpServletResponse.class);
    }

    @Override
    public Object resolve(final Parameter parameter, final HttpServletRequest request, final HttpServletResponse response) {
        return response;
    }

}
