package br.com.leonardoferreira.primavera.web.request.handler;

import br.com.leonardoferreira.primavera.primavera.Primavera;
import br.com.leonardoferreira.primavera.primavera.metadata.ComponentMetaData;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;

public class RequestHandlerList {

    private final Set<RequestHandlerMetadata> handlers;

    public RequestHandlerList(final Primavera primavera) {
        handlers = buildHandlers(primavera);
    }

    private Set<RequestHandlerMetadata> buildHandlers(final Primavera primavera) {
        return primavera.components()
                .stream()
                .flatMap(this::buildHandlers)
                .collect(Collectors.toSet());
    }

    private Stream<RequestHandlerMetadata> buildHandlers(final ComponentMetaData<?> component) {
        final Object instance = component.getInstance();
        return Arrays.stream(instance.getClass().getDeclaredMethods())
                .map(method -> RequestHandlerMetadata.newInstance(instance, method))
                .filter(Objects::nonNull);
    }

    public RequestHandlerMetadata findByRequest(final HttpServletRequest req) {
        return handlers.stream()
                .filter(handlerMetadata -> handlerMetadata.canHandle(req))
                .findFirst()
                .orElse(null);
    }

}
