package br.com.leonardoferreira.primavera.stereotype;

import br.com.leonardoferreira.primavera.annotation.AliasFor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    @AliasFor(targetClass = Component.class, targetMethod = "name")
    String name() default "";

}
