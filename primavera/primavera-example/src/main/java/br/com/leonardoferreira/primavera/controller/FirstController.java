package br.com.leonardoferreira.primavera.controller;

import br.com.leonardoferreira.primavera.web.handler.RequestHandler;
import br.com.leonardoferreira.primavera.web.handler.RequestMethod;
import br.com.leonardoferreira.primavera.primavera.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

@Component
public class FirstController {

    @RequestHandler(method = RequestMethod.GET, path = "/hello")
    public String index(final HttpServletRequest req) {
        return "Hello " + req.getParameter("name");
    }

}
