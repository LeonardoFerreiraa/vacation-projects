package br.com.leonardoferreira.primavera.web.request.handler;

import br.com.leonardoferreira.primavera.primavera.annotation.AnnotationFinder;
import br.com.leonardoferreira.primavera.web.request.RequestMethod;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
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
        final RequestHandler handler = AnnotationFinder.retrieveAnnotation(method, RequestHandler.class);
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

}
