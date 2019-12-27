package br.com.leonardoferreira.primavera.property;

import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.stereotype.Configuration;

@Configuration
public class PropertiesConfiguration {

    @Component
    public ApplicationProperties properties() {
        return ApplicationProperties.read();
    }

}
