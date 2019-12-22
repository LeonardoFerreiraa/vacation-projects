package br.com.leonardoferreira.primavera.web.servlet;

import br.com.leonardoferreira.primavera.web.handler.RequestHandlerList;
import br.com.leonardoferreira.primavera.web.handler.RequestHandlerMetadata;
import br.com.leonardoferreira.primavera.primavera.Primavera;
import br.com.leonardoferreira.primavera.web.resolver.MethodArgumentResolver;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private final Primavera primavera;

    private final RequestHandlerList handlers;

    public DispatcherServlet(final Primavera primavera) {
        this.primavera = primavera;
        this.handlers = new RequestHandlerList(primavera);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        RequestHandlerMetadata requestHandlerMetadata = handlers.findByRequest(req);
        if (requestHandlerMetadata == null) {
            handleNotFound(req, resp);
        } else {
            handleRequest(requestHandlerMetadata, req, resp);
        }
    }

    private void handleRequest(final RequestHandlerMetadata requestHandlerMetadata,
                               final HttpServletRequest req,
                               final HttpServletResponse resp) throws IOException {
        try {
            final Method method = requestHandlerMetadata.getMethod();
            final Set<MethodArgumentResolver> resolvers = primavera.retrieveComponents(MethodArgumentResolver.class);

            final Object[] args = Arrays.stream(method.getParameters())
                    .map(parameter -> {
                        final MethodArgumentResolver resolver = resolvers.stream()
                                .filter(it -> it.canResolve(parameter))
                                .findFirst()
                                .orElseThrow();
                        return resolver.resolve(req);
                    })
                    .toArray();

            final Object result = method.invoke(requestHandlerMetadata.getInstance(), args);
            resp.getWriter().println(result);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleNotFound(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        resp.sendError(404, "Resource not found");
    }

}
