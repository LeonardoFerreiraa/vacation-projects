package br.com.leonardoferreira.primavera.web.request.handler;

import br.com.leonardoferreira.primavera.primavera.Primavera;
import br.com.leonardoferreira.primavera.primavera.annotation.AnnotationFinder;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

public class RequestHandlerList {

    private final Set<RequestHandlerMetadata> handlers;

    public RequestHandlerList(final Primavera primavera) {
        handlers = buildHandlers(primavera);
    }

    private Set<RequestHandlerMetadata> buildHandlers(final Primavera primavera) {
        return primavera.components()
                .methods()
                .filter(cm -> AnnotationFinder.isAnnotationPresent(cm.getMethod(), RequestHandler.class))
                .map(cm -> RequestHandlerMetadata.newInstance(cm.getInstance(), cm.getMethod()))
                .collect(Collectors.toSet());
    }

    public RequestHandlerMetadata findByRequest(final HttpServletRequest req) {
        return handlers.stream()
                .filter(handlerMetadata -> handlerMetadata.canHandle(req))
                .findFirst()
                .orElse(null);
    }

}
