package br.com.leonardoferreira.primavera.web.error.handler;

import br.com.leonardoferreira.primavera.web.response.HttpStatus;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseError {

    HttpStatus status() default HttpStatus.INTERNAL_SERVER_ERROR;

    String message() default "";

}
