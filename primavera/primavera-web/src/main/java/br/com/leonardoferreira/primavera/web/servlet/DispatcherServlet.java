package br.com.leonardoferreira.primavera.web.servlet;

import br.com.leonardoferreira.primavera.primavera.Primavera;
import br.com.leonardoferreira.primavera.web.request.handler.RequestHandlerList;
import br.com.leonardoferreira.primavera.web.request.handler.RequestHandlerMetadata;
import br.com.leonardoferreira.primavera.web.resolver.MethodArgumentResolver;
import br.com.leonardoferreira.primavera.web.response.handler.ResponseHandler;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private final RequestHandlerList handlers;

    private final ResponseHandler responseHandler;

    private final Set<MethodArgumentResolver> resolvers;

    public DispatcherServlet(final Primavera primavera) {
        this.handlers = new RequestHandlerList(primavera);
        this.responseHandler = primavera.retrieveComponent(ResponseHandler.class);
        this.resolvers = primavera.retrieveComponents(MethodArgumentResolver.class);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        RequestHandlerMetadata requestHandlerMetadata = handlers.findByRequest(req);

        if (requestHandlerMetadata == null) {
            responseHandler.handleNotFound(req, resp);
        } else {
            handleRequest(requestHandlerMetadata, req, resp);
        }
    }

    private void handleRequest(final RequestHandlerMetadata requestHandlerMetadata,
                               final HttpServletRequest req,
                               final HttpServletResponse resp) throws IOException {
        try {
            final Method method = requestHandlerMetadata.getMethod();

            final Object[] args = Arrays.stream(method.getParameters())
                    .map(parameter -> MethodArgumentResolver.resolve(req, resp, resolvers, parameter))
                    .toArray();

            final Object result = method.invoke(requestHandlerMetadata.getInstance(), args);

            responseHandler.handleResponse(result, resp);
        } catch (final Exception e) {
            responseHandler.handleError(req, resp, e.getCause());
        }
    }

}
