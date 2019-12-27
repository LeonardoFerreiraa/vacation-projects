package br.com.leonardoferreira.primavera.jpa;

import br.com.leonardoferreira.primavera.property.ApplicationProperties;
import br.com.leonardoferreira.primavera.property.DatasourceProperty;
import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.stereotype.Configuration;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
public class JpaConfiguration {

    @Component
    public EntityManagerFactory entityManagerFactory(final ApplicationProperties applicationProperties) {
        final DatasourceProperty datasourceProperty = applicationProperties.retrieveProperty("datasource", DatasourceProperty.class);

        final Map<String, String> properties = new HashMap<>();

        properties.put("javax.persistence.jdbc.url", datasourceProperty.getUrl());
        properties.put("javax.persistence.jdbc.user", datasourceProperty.getUsername());
        properties.put("javax.persistence.jdbc.password", datasourceProperty.getPassword());

        properties.put("hibernate.dialect", datasourceProperty.getDialect());

        return Persistence.createEntityManagerFactory("empty-persistence-unit", properties);
    }

}
