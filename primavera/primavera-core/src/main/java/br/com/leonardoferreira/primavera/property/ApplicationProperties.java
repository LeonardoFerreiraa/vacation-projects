package br.com.leonardoferreira.primavera.property;

import br.com.leonardoferreira.primavera.collection.list.PrimaveraList;
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
import org.yaml.snakeyaml.constructor.Constructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationProperties {

    private final Map<String, Object> properties;

    public static ApplicationProperties read() {
        return read(ClassLoader.getSystemClassLoader(), "application.yml");
    }

    public static ApplicationProperties read(final Class<?> baseClass) {
        return read(baseClass.getClassLoader(), "application.yml");
    }

    private static ApplicationProperties read(final ClassLoader classLoader, final String fileName) {
        final URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            return new ApplicationProperties(Collections.emptyMap());
        }

        return Try.shrug(() -> {
            final URLConnection urlConnection = resource.openConnection();
            try (final InputStream is = urlConnection.getInputStream()) {
                final Yaml yaml = new Yaml();
                final Map<String, Object> properties = yaml.load(is);

                return new ApplicationProperties(properties);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> T retrieveProperty(final String propertyName) {
        final PrimaveraList<String> propertyNames = PrimaveraList.of(propertyName.split("\\."));

        Map<String, Object> currentProperty = this.properties;
        for (int i = 0; i < propertyNames.size() - 1; i++) {
            String name = propertyNames.get(i);
            currentProperty = (Map<String, Object>) currentProperty.get(name);
        }

        return (T) currentProperty.get(propertyNames.last());
    }

    public <T> T retrieveProperty(final String property, final Class<T> clazz) {
        final Map<String, Object> node = retrieveProperty(property);

        final Yaml yaml = new Yaml(new Constructor(clazz));

        return yaml.load(yaml.dump(node));
    }

}
