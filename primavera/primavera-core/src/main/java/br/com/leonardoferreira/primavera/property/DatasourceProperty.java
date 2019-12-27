package br.com.leonardoferreira.primavera.property;

import lombok.Data;

@Data
public class DatasourceProperty {

    private String url;

    private String username;

    private String password;

    public String dialect;

}
