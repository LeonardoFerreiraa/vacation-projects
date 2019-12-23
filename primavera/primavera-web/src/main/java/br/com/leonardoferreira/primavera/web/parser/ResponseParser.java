package br.com.leonardoferreira.primavera.web.parser;

import javax.servlet.http.HttpServletResponse;

public interface ResponseParser {

    void parse(Object object, HttpServletResponse response);

}
