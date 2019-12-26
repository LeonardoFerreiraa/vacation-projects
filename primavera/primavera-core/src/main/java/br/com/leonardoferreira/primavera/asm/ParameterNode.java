package br.com.leonardoferreira.primavera.asm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParameterNode {

    private final String name;

    private final Integer index;

}
