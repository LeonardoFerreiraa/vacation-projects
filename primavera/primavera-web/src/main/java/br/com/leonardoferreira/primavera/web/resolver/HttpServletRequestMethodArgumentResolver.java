package br.com.leonardoferreira.primavera.web.resolver;

import br.com.leonardoferreira.primavera.primavera.stereotype.Component;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpServletRequestMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean canResolve(final Parameter parameter) {
        return parameter.getType().equals(HttpServletRequest.class);
    }

    @Override
    public Object resolve(final Parameter parameter, final HttpServletRequest req, final HttpServletResponse resp) {
        return req;
    }

}