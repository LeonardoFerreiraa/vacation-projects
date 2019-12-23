package br.com.leonardoferreira.primavera.primavera.scanner;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class PackageClasspathScanner implements ClasspathScanner {

    private final ClassLoader classLoader;

    private final String packageName;

    public PackageClasspathScanner(final Class<?> baseClass) {
        this(baseClass.getClassLoader(), baseClass.getPackageName());
    }

    @Override
    public Set<Class<?>> scan() {
        final String path = packageName.replace('.', '/');

        try {
            return Collections.list(classLoader.getResources(path))
                    .stream()
                    .map(resource -> findClasses(new File(resource.getFile()), packageName))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Class<?>> findClasses(final File directory, final String packageName) {
        final Set<Class<?>> classes = new HashSet<>();
        if (!directory.exists() || !directory.isDirectory()) {
            if (isJar(directory)) {
                return scanJar(directory);
            }
            return classes;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, completeQualifier(packageName, file)));
                } else if (file.getName().endsWith(".class")) {
                    final Class<?> clazz = retrieveClass(packageName, file);
                    if (!clazz.isAnnotation()) {
                        classes.add(clazz);
                    }
                }
            }
        }

        return classes;
    }

    private static Class<?> retrieveClass(final String packageName, final File file) {
        try {
            return Class.forName(completeQualifier(packageName, file));
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String completeQualifier(final String packageName, final File file) {
        final String fileName = file.getName().replaceFirst("[.][^.]+$", "");
        return packageName + '.' + fileName;
    }

    private boolean isJar(final File directory) {
        return directory.getPath().contains(".jar!");
    }

    private Set<Class<?>> scanJar(final File directory) {
        final String packageName = directory.getPath().split("\\.jar!/")[1];
        final JarClasspathScanner scanner = new JarClasspathScanner(classLoader, packageName);
        return scanner.scan();
    }

}
