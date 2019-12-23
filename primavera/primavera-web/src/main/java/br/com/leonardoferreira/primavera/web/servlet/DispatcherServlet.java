package br.com.leonardoferreira.primavera.web.servlet;

import br.com.leonardoferreira.primavera.primavera.Primavera;
import br.com.leonardoferreira.primavera.web.handler.RequestHandlerList;
import br.com.leonardoferreira.primavera.web.handler.RequestHandlerMetadata;
import br.com.leonardoferreira.primavera.web.parser.ResponseParser;
import br.com.leonardoferreira.primavera.web.resolver.MethodArgumentResolver;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
                               final HttpServletResponse resp) {
        try {
            final Method method = requestHandlerMetadata.getMethod();
            final Set<MethodArgumentResolver> resolvers = primavera.retrieveComponents(MethodArgumentResolver.class);

            final Object[] args = Arrays.stream(method.getParameters())
                    .map(parameter -> resolve(req, resp, resolvers, parameter))
                    .toArray();

            final Object result = method.invoke(requestHandlerMetadata.getInstance(), args);

            primavera.retrieveComponent(ResponseParser.class).parse(result, resp);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object resolve(final HttpServletRequest req,
                           final HttpServletResponse resp,
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

        return elected.get(0).resolve(parameter, req, resp);
    }

    private void handleNotFound(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        resp.sendError(404, "Resource not found");
    }

}
