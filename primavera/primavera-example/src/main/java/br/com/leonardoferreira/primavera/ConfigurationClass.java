package br.com.leonardoferreira.primavera;

import br.com.leonardoferreira.primavera.controller.FirstController;
import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.stereotype.Configuration;
import com.google.gson.Gson;

@Configuration
public class ConfigurationClass {

    @Component
    public Gson gson(final FirstController firstService) {
        System.out.println("gson");
        return new Gson();
    }

}
