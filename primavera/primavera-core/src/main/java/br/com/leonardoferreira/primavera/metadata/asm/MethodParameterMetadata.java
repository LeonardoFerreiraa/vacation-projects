package br.com.leonardoferreira.primavera.metadata.asm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MethodParameterMetadata {

    private final String name;

    private final Integer index;

}
