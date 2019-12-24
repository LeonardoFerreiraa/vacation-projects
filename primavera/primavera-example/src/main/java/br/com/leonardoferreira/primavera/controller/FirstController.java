package br.com.leonardoferreira.primavera.controller;

import br.com.leonardoferreira.primavera.service.FirstService;
import br.com.leonardoferreira.primavera.service.SecondService;
import br.com.leonardoferreira.primavera.service.ThirdService;
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

    private final FirstService firstService;

    private final SecondService secondService;

    private final ThirdService thirdService;

    public FirstController(final FirstService firstService,
                           final SecondService secondService,
                           final ThirdService thirdService) {
        System.out.println("firstController");
        this.firstService = firstService;
        this.secondService = secondService;
        this.thirdService = thirdService;
    }

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
