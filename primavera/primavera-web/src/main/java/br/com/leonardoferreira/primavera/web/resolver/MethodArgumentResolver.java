package br.com.leonardoferreira.primavera.web.resolver;

import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MethodArgumentResolver {

    boolean canResolve(Parameter parameter);

    Object resolve(final Parameter parameter, HttpServletRequest req, final HttpServletResponse resp);

}
