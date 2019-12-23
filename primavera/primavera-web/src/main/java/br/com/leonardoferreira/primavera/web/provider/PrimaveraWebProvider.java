package br.com.leonardoferreira.primavera.web.provider;

import br.com.leonardoferreira.primavera.primavera.PrimaveraType;
import br.com.leonardoferreira.primavera.primavera.provider.PrimaveraProvider;
import br.com.leonardoferreira.primavera.primavera.scanner.ClasspathScanner;
import br.com.leonardoferreira.primavera.web.server.UndertowWebServer;
import br.com.leonardoferreira.primavera.web.server.WebServer;
import br.com.leonardoferreira.primavera.web.servlet.DispatcherServlet;

public class PrimaveraWebProvider extends PrimaveraProvider {

    private final WebServer webServer;

    public PrimaveraWebProvider() {
        webServer = new UndertowWebServer();
    }

    @Override
    public void scan(final Class<?> baseClass) {
        classes.addAll(ClasspathScanner.scan("br.com.leonardoferreira.primavera.web"));

        super.scan(baseClass);
    }

    @Override
    public void run() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet(this);
        webServer.init(dispatcherServlet);
        webServer.start();
    }

    @Override
    public PrimaveraType type() {
        return PrimaveraType.WEB;
    }

}
