package br.com.leonardoferreira.primavera.web.server;

import javax.servlet.Servlet;

public interface WebServer {

    void init(Servlet servlet);

    void start();

}
