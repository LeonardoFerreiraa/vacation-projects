package br.com.leonardoferreira.primavera.proxy;

import br.com.leonardoferreira.primavera.annotation.AliasFor;
import br.com.leonardoferreira.primavera.functional.Pair;
import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import br.com.leonardoferreira.primavera.util.ExceptionUtils;
import br.com.leonardoferreira.primavera.util.MapUtils;
import br.com.leonardoferreira.primavera.util.ProxyUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AnnotationProxy implements InvocationHandler {

    private final Map<String, Object> methods;

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return methods.get(method.getName());
    }

    public static <T extends Annotation> T of(final Class<T> clazz,
                                              final Pair<Class<?>, Object> requestedAnnotation,
                                              final Pair<Class<?>, Object> annotationFound) {
        return ProxyUtil.createProxy(clazz, new AnnotationProxy(mapMethods(requestedAnnotation, annotationFound)));
    }


    private static Map<String, Object> mapMethods(final Pair<Class<?>, Object> requestedAnnotation,
                                                  final Pair<Class<?>, Object> annotationFound) {
        final Map<String, Object> aliasMethods = Stream.of(annotationFound.getKey().getDeclaredMethods())
                .map(method -> retrieveAliasFor(method, requestedAnnotation.getKey()))
                .filter(Objects::nonNull)
                .map(pair -> ExceptionUtils.rethrowAsRuntime(() -> Pair.of(pair.getKey(), pair.getValue().invoke(annotationFound.getValue()))))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        final Map<String, Object> methods = Stream.of(requestedAnnotation.getKey().getDeclaredMethods())
                .map(method -> ExceptionUtils.rethrowAsRuntime(() -> Pair.of(method.getName(), method.invoke(requestedAnnotation.getValue()))))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        return MapUtils.join(methods, aliasMethods);
    }

    private static Pair<String, Method> retrieveAliasFor(final Method method, final Class<?> value) {
        return Optional.ofNullable(AnnotationUtils.retrieveAnnotation(method, AliasFor.class))
                .filter(alias -> alias.targetClass().equals(value))
                .map(alias -> Pair.of(alias.targetMethod(), method))
                .orElse(null);
    }

}
