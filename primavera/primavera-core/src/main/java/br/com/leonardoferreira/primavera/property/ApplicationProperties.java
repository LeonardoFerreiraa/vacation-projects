package br.com.leonardoferreira.primavera.property;

import br.com.leonardoferreira.primavera.util.Try;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationProperties {

    private final Map<String, Object> properties;

    public static ApplicationProperties read(final Class<?> baseClass) {
        return read(baseClass.getClassLoader(), "application.yml");
    }

    private static ApplicationProperties read(final ClassLoader classLoader, final String fileName) {
        final URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            return new ApplicationProperties(Collections.emptyMap());
        }

        return Try.uncheck(() -> {
            final URLConnection urlConnection = resource.openConnection();
            try (final InputStream is = urlConnection.getInputStream()) {
                final Yaml yaml = new Yaml();
                final Map<String, Object> properties = yaml.load(is);

                return new ApplicationProperties(properties);
            }
        });
    }

}
