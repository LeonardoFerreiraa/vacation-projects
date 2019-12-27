package br.com.leonardoferreira.primavera.metadata.asm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class MethodParameterMetadata {

    private final String name;

    private final Integer index;

}
