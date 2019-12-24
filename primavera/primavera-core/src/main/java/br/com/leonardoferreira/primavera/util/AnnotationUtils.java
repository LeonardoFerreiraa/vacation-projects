package br.com.leonardoferreira.primavera.util;

import br.com.leonardoferreira.primavera.functional.Pair;
import br.com.leonardoferreira.primavera.proxy.AnnotationProxy;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AnnotationUtils {

    public static <T extends Annotation> T retrieveAnnotation(final AnnotatedElement element, final Class<T> annotation) {
        if (element instanceof Class) {
            if (((Class<?>) element).getPackageName().startsWith("java.")) {
                return null;
            }
        }

        if (element.isAnnotationPresent(annotation)) {
            return element.getAnnotation(annotation);
        }

        for (final Annotation clazzAnnotation : element.getAnnotations()) {
            final T annotationFound = retrieveAnnotation(clazzAnnotation.annotationType(), annotation);
            if (annotationFound != null) {
                return AnnotationProxy.of(annotation, Pair.of(annotation, annotationFound), Pair.of(clazzAnnotation.annotationType(), clazzAnnotation));
            }
        }

        return null;
    }

    public static boolean isAnnotationPresent(final AnnotatedElement element, Class<? extends Annotation> annotation) {
        return retrieveAnnotation(element, annotation) != null;
    }

}
