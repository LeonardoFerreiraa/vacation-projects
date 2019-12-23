package br.com.leonardoferreira.primavera.controller;

import br.com.leonardoferreira.primavera.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.web.error.handler.ResponseError;
import br.com.leonardoferreira.primavera.web.request.RequestBody;
import br.com.leonardoferreira.primavera.web.request.RequestMethod;
import br.com.leonardoferreira.primavera.web.request.handler.RequestHandler;
import br.com.leonardoferreira.primavera.web.response.HttpStatus;
import br.com.leonardoferreira.primavera.web.response.ResponseEntity;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;

@Component
public class FirstController {

    @RequestHandler(method = RequestMethod.GET, path = "/hello")
    public Greeting index() {
        return new Greeting("hello world");
    }

    @RequestHandler(method = RequestMethod.POST, path = "/hello")
    public ResponseEntity<?> post(@RequestBody Greeting greeting) {
        System.out.println(greeting);
        return ResponseEntity.created(URI.create("/hello"));
    }

    @RequestHandler(method = RequestMethod.GET, path = "/anything")
    public void bla() {
        throw new ResourceNotFound();
    }

    @ResponseError(status = HttpStatus.NOT_FOUND, message = "Resource not found!")
    public static class ResourceNotFound extends RuntimeException {
    }

    @Data
    @AllArgsConstructor
    public static class Greeting {
        private final String message;
    }

}
