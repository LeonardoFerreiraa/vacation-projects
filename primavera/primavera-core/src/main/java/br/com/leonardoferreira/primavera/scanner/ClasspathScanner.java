package br.com.leonardoferreira.primavera.scanner;

import br.com.leonardoferreira.primavera.util.ExceptionUtils;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClasspathScanner {

    public static Set<Class<?>> scan(final Class<?> baseClass) {
        return scan(baseClass.getClassLoader(), baseClass.getPackageName());
    }

    public static Set<Class<?>> scan(final String packageName) {
        return scan(ClasspathScanner.class.getClassLoader(), packageName);
    }

    private static Set<Class<?>> scan(final ClassLoader classLoader, final String packageName) {
        return ExceptionUtils.silence(() ->
                Collections.list(classLoader.getResources(packageName.replaceAll("\\.", "/")))
                        .stream()
                        .map(url ->
                                ClassScanner.findByProtocol(url.getProtocol())
                                        .scan(url, packageName)
                        )
                        .flatMap(Set::stream)
                        .filter(clazz -> !clazz.isAnnotation())
                        .collect(Collectors.toSet())
        );
    }

}
