package br.com.leonardoferreira.primavera.web.request.handler;

import br.com.leonardoferreira.primavera.annotation.AliasFor;
import br.com.leonardoferreira.primavera.web.request.RequestMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@RequestHandler(method = RequestMethod.POST)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Post {

    @AliasFor(targetClass = RequestHandler.class, targetMethod = "path")
    String value();

}
