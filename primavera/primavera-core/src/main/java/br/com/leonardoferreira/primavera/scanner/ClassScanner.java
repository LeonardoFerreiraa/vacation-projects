package br.com.leonardoferreira.primavera.scanner;

import java.net.URL;
import java.util.Map;
import java.util.Set;

interface ClassScanner {

    Set<Class<?>> scan(final URL url, final String packageName);

    static ClassScanner findByProtocol(final String protocol) {
        return resolvers().get(protocol);
    }

    static Map<String, ClassScanner> resolvers() {
        return Map.of(
                "file", new FileClassScanner(),
                "jar", new JarClassScanner()
        );
    }
}
