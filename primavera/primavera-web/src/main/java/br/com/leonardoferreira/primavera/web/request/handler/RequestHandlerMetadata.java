package br.com.leonardoferreira.primavera.web.request.handler;

import br.com.leonardoferreira.primavera.functional.Outcome;
import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import br.com.leonardoferreira.primavera.web.request.RequestMethod;
import br.com.leonardoferreira.primavera.web.resolver.MethodArgumentResolver;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestHandlerMetadata {

    private final RequestMethod requestMethod;

    private final Object instance;

    private final Method method;

    private final RequestPath path;

    public static RequestHandlerMetadata newInstance(final Object instance, final Method method) {
        return AnnotationUtils.findAnnotation(method, RequestHandler.class)
                .map(handler -> RequestHandlerMetadata.builder()
                        .instance(instance)
                        .method(method)
                        .requestMethod(handler.method())
                        .path(RequestPath.fromPath(handler.path()))
                        .build())
                .orElse(null);
    }

    public boolean canHandle(final HttpServletRequest req) {
        final String pathInfo = req.getPathInfo();
        final RequestMethod method = RequestMethod.valueOf(req.getMethod());

        return path.matches(pathInfo) && method.equals(this.requestMethod);
    }

    public Outcome<Object, Throwable> handle(final HttpServletRequest request,
                                             final HttpServletResponse response,
                                             final Set<MethodArgumentResolver> resolvers) {
        return Outcome.of(() -> {
            final Object[] args = Arrays.stream(method.getParameters())
                    .map(parameter -> MethodArgumentResolver.resolve(request, response, this, resolvers, parameter))
                    .toArray();

            return method.invoke(instance, args);
        });
    }
}
