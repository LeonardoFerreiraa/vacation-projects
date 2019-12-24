package br.com.leonardoferreira.primavera;

import br.com.leonardoferreira.primavera.service.FirstService;
import br.com.leonardoferreira.primavera.service.SecondService;
import br.com.leonardoferreira.primavera.web.parser.json.GsonJsonParser;
import br.com.leonardoferreira.primavera.web.parser.json.JsonParser;

public class Application {

    public static void main(String[] args) throws Exception {
        final Primavera primavera = PrimaveraApplication.runWebApplication(Application.class);

        System.out.println(primavera.retrieveComponent(FirstService.class));
        System.out.println(primavera.retrieveComponent(SecondService.class));
    }

}
