package br.com.leonardoferreira.primavera.web.request.handler;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.collection.set.PrimaveraSet;
import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestHandlerList extends PrimaveraSet<RequestHandlerMetadata> {

    public RequestHandlerList(final Primavera primavera) {
        this.elements = buildHandlers(primavera);
    }

    private Set<RequestHandlerMetadata> buildHandlers(final Primavera primavera) {
        return primavera.components()
                .methods()
                .filter(cm -> AnnotationUtils.isAnnotationPresent(cm.getMethod(), RequestHandler.class))
                .map(cm -> RequestHandlerMetadata.newInstance(cm.getInstance(), cm.getMethod()))
                .collect(Collectors.toSet());
    }

}
