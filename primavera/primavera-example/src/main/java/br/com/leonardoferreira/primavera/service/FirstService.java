package br.com.leonardoferreira.primavera.service;

import br.com.leonardoferreira.primavera.stereotype.Service;

@Service
public class FirstService {

    private final SecondService secondService;

    public FirstService(final SecondService secondService) {
        this.secondService = secondService;
    }

    @Override
    public String toString() {
        return "FirstService{" +
                "secondService=" + secondService +
                '}';
    }
}
