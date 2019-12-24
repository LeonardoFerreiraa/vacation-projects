package br.com.leonardoferreira.primavera.scanner;

import java.util.Set;

public interface ClasspathScanner {

    Set<Class<?>> scan();

    static Set<Class<?>> scan(Class<?> baseClass) {
        final PackageClasspathScanner scanner = new PackageClasspathScanner(baseClass);
        return scanner.scan();
    }

    static Set<Class<?>> scan(String packageName) {
        final PackageClasspathScanner scanner = new PackageClasspathScanner(ClasspathScanner.class.getClassLoader(), packageName);
        return scanner.scan();
    }

}
