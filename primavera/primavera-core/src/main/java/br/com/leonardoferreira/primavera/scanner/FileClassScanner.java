package br.com.leonardoferreira.primavera.scanner;

import br.com.leonardoferreira.primavera.util.Try;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class FileClassScanner implements ClassScanner {

    @Override
    public Set<Class<?>> scan(final URL url, final String packageName) {
        return resolve(new File(url.getFile()), packageName);
    }

    private Set<Class<?>> resolve(final File directory, final String packageName) {
        if (!directory.exists()) {
            return Collections.emptySet();
        }

        return Optional.ofNullable(directory.listFiles())
                .stream()
                .flatMap(Arrays::stream)
                .map(file -> createClassFiles(packageName, file))
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> createClassFiles(final String packageName, final File file) {
        if (file.isDirectory()) {
            return resolve(file, completeQualifier(packageName, file));
        }

        if (file.getName().endsWith(".class")) {
            final Class<?> clazz = Try.uncheck(() ->
                    Class.forName(completeQualifier(packageName, file)));
            return Set.of(clazz);
        }

        return null;
    }

    private static String completeQualifier(final String packageName, final File file) {
        final String fileName = file.getName().replaceFirst("[.][^.]+$", "");
        return packageName + '.' + fileName;
    }

}
