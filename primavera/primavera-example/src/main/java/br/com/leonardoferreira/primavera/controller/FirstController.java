package br.com.leonardoferreira.primavera.controller;

import br.com.leonardoferreira.primavera.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.web.annotation.RequestBody;
import br.com.leonardoferreira.primavera.web.handler.RequestHandler;
import br.com.leonardoferreira.primavera.web.handler.RequestMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

@Component
public class FirstController {

    @RequestHandler(method = RequestMethod.GET, path = "/hello")
    public Greeting index() {
        return new Greeting("hello world");
    }

    @RequestHandler(method = RequestMethod.POST, path = "/hello")
    public Greeting post(@RequestBody Greeting greeting) {
        System.out.println(greeting);
        return greeting;
    }

    @Data
    @AllArgsConstructor
    public static class Greeting {
        private final String message;
    }

}
