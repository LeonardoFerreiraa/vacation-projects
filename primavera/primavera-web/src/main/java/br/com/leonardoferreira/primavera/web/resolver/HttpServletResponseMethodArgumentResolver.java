package br.com.leonardoferreira.primavera.web.resolver;

import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.web.request.handler.RequestHandlerMetadata;
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
    public Object resolve(final HttpServletRequest request, final HttpServletResponse response, final RequestHandlerMetadata handler, final Parameter parameter) {
        return response;
    }

}
