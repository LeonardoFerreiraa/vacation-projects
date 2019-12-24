package br.com.leonardoferreira.primavera.web.request.handler;

import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import br.com.leonardoferreira.primavera.functional.Outcome;
import br.com.leonardoferreira.primavera.web.request.RequestMethod;
import br.com.leonardoferreira.primavera.web.resolver.MethodArgumentResolver;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestHandlerMetadata {

    private final RequestMethod requestMethod;

    private final Object instance;

    private final Method method;

    private final String path;

    public static RequestHandlerMetadata newInstance(final Object instance, final Method method) {
        final RequestHandler handler = AnnotationUtils.retrieveAnnotation(method, RequestHandler.class);
        if (handler == null) {
            return null;
        }

        return new RequestHandlerMetadata(
                handler.method(),
                instance,
                method,
                handler.path()
        );

    }

    public boolean canHandle(final HttpServletRequest req) {
        final String pathInfo = req.getPathInfo();
        final RequestMethod method = RequestMethod.valueOf(req.getMethod());

        return pathInfo.equals(path) && method.equals(this.requestMethod);
    }

    public Outcome<Object, Throwable> handle(final HttpServletRequest request,
                                             final HttpServletResponse response,
                                             final Set<MethodArgumentResolver> resolvers) {
        return Outcome.from(() -> {
            final Object[] args = Arrays.stream(method.getParameters())
                    .map(parameter -> MethodArgumentResolver.resolve(request, response, resolvers, parameter))
                    .toArray();

            return method.invoke(instance, args);
        });
    }
}
