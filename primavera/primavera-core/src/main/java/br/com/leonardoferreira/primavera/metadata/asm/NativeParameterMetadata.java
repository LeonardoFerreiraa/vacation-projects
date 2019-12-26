package br.com.leonardoferreira.primavera.metadata.asm;

import java.lang.reflect.Parameter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NativeParameterMetadata {

    private Parameter parameter;

    public static NativeParameterMetadata fromParameter(final Parameter parameter) {
        return new NativeParameterMetadata(parameter);
    }

    public Integer getIndex() {
        return parameter.isNamePresent() ? null : Integer.parseInt(parameter.getName().replace("arg", ""));
    }

    public String getName() {
        return parameter.isNamePresent() ? parameter.getName() : null;
    }

    public boolean matches(final MethodParameterMetadata methodParameterMetadata) {
        return methodParameterMetadata.getIndex().equals(getIndex());
    }

}
