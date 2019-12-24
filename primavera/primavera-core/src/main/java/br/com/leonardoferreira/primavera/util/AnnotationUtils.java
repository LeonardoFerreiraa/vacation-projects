package br.com.leonardoferreira.primavera.util;

import br.com.leonardoferreira.primavera.functional.Pair;
import br.com.leonardoferreira.primavera.proxy.AnnotationProxy;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotationUtils {

    public static <T extends Annotation> Optional<T> findAnnotation(final AnnotatedElement element, final Class<T> annotation) {
        if (isJavaAnnotation(element)) {
            return Optional.empty();
        }

        if (element.isAnnotationPresent(annotation)) {
            return Optional.of(element.getAnnotation(annotation));
        }

        return findAnnotationInAnnotations(element, annotation);
    }

    public static boolean isAnnotationPresent(final AnnotatedElement element, Class<? extends Annotation> annotation) {
        return findAnnotation(element, annotation).isPresent();
    }

    private static <T extends Annotation> Optional<T> findAnnotationInAnnotations(final AnnotatedElement element, final Class<T> annotation) {
        return Arrays.stream(element.getAnnotations())
                .map(clazzAnnotation -> Pair.of(clazzAnnotation, findAnnotation(clazzAnnotation.annotationType(), annotation)))
                .filter(pair -> pair.getValue().isPresent())
                .map(pair -> Pair.of(pair.getKey(), pair.getValue().get()))
                .map(pair -> AnnotationProxy.of(annotation, Pair.of(annotation, pair.getValue()), Pair.of(pair.getKey().annotationType(), pair.getKey())))
                .findFirst();
    }

    private static boolean isJavaAnnotation(AnnotatedElement element) {
        return element instanceof Class && ((Class<?>) element).getPackageName().startsWith("java.");
    }

}
