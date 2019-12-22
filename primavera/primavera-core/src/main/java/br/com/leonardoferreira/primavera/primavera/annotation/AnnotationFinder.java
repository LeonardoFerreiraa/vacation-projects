package br.com.leonardoferreira.primavera.primavera.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AnnotationFinder {

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
            final T retrieveAnnotation = retrieveAnnotation(clazzAnnotation.annotationType(), annotation);
            if (retrieveAnnotation != null) {
                return retrieveAnnotation;
            }
        }

        return null;
    }

    public static boolean isAnnotationPresent(final AnnotatedElement element, Class<? extends Annotation> annotation) {
        return retrieveAnnotation(element, annotation) != null;
    }

}
