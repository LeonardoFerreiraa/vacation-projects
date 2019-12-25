package br.com.leonardoferreira.primavera.metadata;

import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComponentMethodMetadata {

    private final Object instance;

    private final Method method;

}
