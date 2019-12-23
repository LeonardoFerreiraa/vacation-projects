package br.com.leonardoferreira.primavera.web.resolver;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MethodArgumentResolver {

    static Object resolve(final HttpServletRequest request,
                          final HttpServletResponse response,
                          final Set<MethodArgumentResolver> resolvers,
                          final Parameter parameter) {
        final List<MethodArgumentResolver> elected = resolvers.stream()
                .filter(it -> it.canResolve(parameter))
                .collect(Collectors.toList());

        if (elected.size() > 1) {
            throw new RuntimeException("More to one resolver to parameter");
        }

        if (elected.size() < 1) {
            throw new RuntimeException("No resolver found to parameter");
        }

        return elected.get(0).resolve(parameter, request, response);
    }

    boolean canResolve(Parameter parameter);

    Object resolve(final Parameter parameter, HttpServletRequest req, final HttpServletResponse resp);

}
