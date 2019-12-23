package br.com.leonardoferreira.primavera.web.request.handler;

import br.com.leonardoferreira.primavera.web.request.RequestMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestHandler {

    RequestMethod method();

    String path();

}
