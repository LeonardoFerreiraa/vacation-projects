package br.com.leonardoferreira.primavera.web.resolver;

import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;

public interface MethodArgumentResolver {

    boolean canResolve(Parameter parameter);

    Object resolve(HttpServletRequest req);

}
