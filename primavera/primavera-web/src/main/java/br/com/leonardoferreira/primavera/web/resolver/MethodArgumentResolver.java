package br.com.leonardoferreira.primavera.web.resolver;

import br.com.leonardoferreira.primavera.collection.PrimaveraCollectionCollector;
import br.com.leonardoferreira.primavera.collection.list.PrimaveraList;
import br.com.leonardoferreira.primavera.web.request.handler.RequestHandlerMetadata;
import java.lang.reflect.Parameter;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MethodArgumentResolver {

    static Object resolve(final HttpServletRequest request,
                          final HttpServletResponse response,
                          final RequestHandlerMetadata handler,
                          final Set<MethodArgumentResolver> resolvers,
                          final Parameter parameter) {
        final PrimaveraList<MethodArgumentResolver> elected = resolvers.stream()
                .filter(it -> it.canResolve(parameter))
                .collect(PrimaveraCollectionCollector.toList());

        if (elected.size() > 1) {
            throw new RuntimeException("More to one resolver to parameter");
        }

        if (elected.size() < 1) {
            throw new RuntimeException("No resolver found to parameter");
        }

        return elected.first()
                .resolve(request, response, handler, parameter);
    }

    boolean canResolve(Parameter parameter);

    Object resolve(HttpServletRequest request, final HttpServletResponse response, final RequestHandlerMetadata handler, final Parameter parameter);

}
