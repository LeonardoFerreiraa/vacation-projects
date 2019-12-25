package br.com.leonardoferreira.primavera.web;

import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.stereotype.Configuration;
import com.google.gson.Gson;

@Configuration
public class WebConfiguration {

    @Component
    public Gson gson() {
        return new Gson();
    }

}
