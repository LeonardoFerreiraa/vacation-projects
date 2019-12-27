package br.com.leonardoferreira.primavera.scanner;

import br.com.leonardoferreira.primavera.metadata.asm.ClassMetadata;
import br.com.leonardoferreira.primavera.util.Try;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

class JarClassScanner implements ClassScanner {

    @Override
    public Set<Class<?>> scan(final URL url, final String packageName) {
        return Try.shrug(() -> {
            final URLConnection urlConnection = url.openConnection();
            try (final JarFile jarFile = ((JarURLConnection) urlConnection).getJarFile()) {
                return Collections.list(jarFile.entries())
                        .stream()
                        .filter(jarEntry -> jarEntry.getRealName().endsWith(".class"))
                        .map(jarEntry -> createClassFile(jarFile, jarEntry))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
            }
        });
    }

    private Class<?> createClassFile(final JarFile jarFile, final JarEntry jarEntry) {
        try (
                final InputStream inputStream = jarFile.getInputStream(jarEntry);
                final DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream))
        ) {
            final ClassMetadata classMetadata = ClassMetadata.fromInputStream(dis);
            return classMetadata.toClass();
        } catch (final Exception e) {
            return null;
        }
    }

}
