package br.com.leonardoferreira.primavera.scanner;

import br.com.leonardoferreira.primavera.util.ExceptionUtils;
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
import javassist.bytecode.ClassFile;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class JarClasspathScanner implements ClasspathScanner {

    private final ClassLoader classLoader;

    private final String packageName;

    @Override
    public Set<Class<?>> scan() {
        try {
            return Collections.list(classLoader.getResources(packageName.replaceAll("\\.", "/")))
                    .stream()
                    .map(this::findClassesByUrl)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Class<?>> findClassesByUrl(final URL url) {
        try {
            final URLConnection urlConnection = url.openConnection();
            try (final JarFile jarFile = ((JarURLConnection) urlConnection).getJarFile()) {
                return Collections.list(jarFile.entries())
                        .stream()
                        .filter(jarEntry -> jarEntry.getRealName().endsWith(".class"))
                        .map(jarEntry -> createClassFile(jarFile, jarEntry))
                        .map(this::classFileToClass)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?> classFileToClass(final ClassFile classFile) {
        return ExceptionUtils.silence(() -> Class.forName(classFile.getName()));
    }

    private ClassFile createClassFile(final JarFile jarFile, final JarEntry jarEntry) {
        try (
                final InputStream inputStream = jarFile.getInputStream(jarEntry);
                final DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream))
        ) {
            return new ClassFile(dis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
