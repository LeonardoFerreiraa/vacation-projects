package br.com.leonardoferreira.primavera.web.resolver;

import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import br.com.leonardoferreira.primavera.util.StringUtils;
import br.com.leonardoferreira.primavera.web.exception.HttpException;
import br.com.leonardoferreira.primavera.web.parser.requestvariable.RequestVariableParser;
import br.com.leonardoferreira.primavera.web.request.RequestParam;
import br.com.leonardoferreira.primavera.web.request.handler.RequestHandlerMetadata;
import br.com.leonardoferreira.primavera.web.response.HttpStatus;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestParamMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean canResolve(final Parameter parameter) {
        return AnnotationUtils.isAnnotationPresent(parameter, RequestParam.class);
    }

    @Override
    public Object resolve(final HttpServletRequest request, final HttpServletResponse response, final RequestHandlerMetadata handler, final Parameter parameter) {
        final RequestParam requestParam = AnnotationUtils.findAnnotation(parameter, RequestParam.class)
                .orElseThrow();

        final String value = request.getParameter(requestParam.value());
        if (StringUtils.isBlank(value) && requestParam.required()) {
            throw new HttpException("Required param [" + requestParam.value() + "] not found in request", HttpStatus.BAD_REQUEST);
        }

        return RequestVariableParser.parse(parameter.getType(), value);
    }

}
