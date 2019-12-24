package br.com.leonardoferreira.primavera.controller;

import br.com.leonardoferreira.primavera.stereotype.Controller;
import br.com.leonardoferreira.primavera.web.exception.handler.ResponseError;
import br.com.leonardoferreira.primavera.web.request.RequestBody;
import br.com.leonardoferreira.primavera.web.request.RequestParam;
import br.com.leonardoferreira.primavera.web.request.handler.Get;
import br.com.leonardoferreira.primavera.web.request.handler.Post;
import br.com.leonardoferreira.primavera.web.response.HttpStatus;
import br.com.leonardoferreira.primavera.web.response.ResponseEntity;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;

@Controller
public class FirstController {

    @Get("/hello")
    public Greeting index() {
        return new Greeting("hello world");
    }

    @Get("/greetings")
    public Greeting index(@RequestParam("name") String name, @RequestParam("times") Long times) {
        return new Greeting("hello " + name + " " + times + " times");
    }

    @Post("/anything")
    public ResponseEntity<?> post(@RequestBody Greeting greeting) {
        System.out.println(greeting);
        return ResponseEntity.created(URI.create("/hello"));
    }

    @Get("/anything")
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
