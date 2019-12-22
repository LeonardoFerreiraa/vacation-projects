package br.com.leonardoferreira.primavera.web.server;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import javax.servlet.Servlet;

public class UndertowWebServer implements WebServer {

    private Undertow server;

    @Override
    public void init(final Servlet servlet) {
        final DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(UndertowWebServer.class.getClassLoader())
                .setContextPath("/")
                .setDeploymentName("primavera.war")
                .addServlets(Servlets.servlet("DispatcherServlet", servlet.getClass(), instanceFactory(servlet)).addMapping("/*"));

        final DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();

        final HttpHandler handler;
        try {
            handler = manager.start();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        final PathHandler path = Handlers.path(Handlers.redirect("/"))
                .addPrefixPath("/", handler);

        server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(path)
                .build();
    }

    @Override
    public void start() {
        server.start();
    }

    private InstanceFactory<Servlet> instanceFactory(final Servlet servlet) {
        return () -> new InstanceHandle<>() {
            @Override
            public Servlet getInstance() {
                return servlet;
            }

            @Override
            public void release() {
            }
        };
    }

}
