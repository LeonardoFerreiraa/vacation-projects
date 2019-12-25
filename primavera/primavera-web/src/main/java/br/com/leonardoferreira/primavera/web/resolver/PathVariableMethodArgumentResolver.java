package br.com.leonardoferreira.primavera.web.resolver;

import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import br.com.leonardoferreira.primavera.web.parser.requestvariable.RequestVariableParser;
import br.com.leonardoferreira.primavera.web.request.PathVariable;
import br.com.leonardoferreira.primavera.web.request.handler.RequestHandlerMetadata;
import java.lang.reflect.Parameter;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PathVariableMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean canResolve(final Parameter parameter) {
        return AnnotationUtils.isAnnotationPresent(parameter, PathVariable.class);
    }

    @Override
    public Object resolve(final HttpServletRequest request,
                          final HttpServletResponse response,
                          final RequestHandlerMetadata handler,
                          final Parameter parameter) {
        final PathVariable pathVariable = AnnotationUtils.findAnnotation(parameter, PathVariable.class)
                .orElseThrow();

        return Optional.of(handler.getPath())
                .map(path -> path.pathVariable(pathVariable.value(), request.getPathInfo()))
                .map(value -> RequestVariableParser.parse(parameter.getType(), value))
                .orElseThrow();
    }

}
