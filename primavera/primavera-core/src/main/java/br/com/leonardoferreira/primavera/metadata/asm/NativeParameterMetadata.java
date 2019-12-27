package br.com.leonardoferreira.primavera.metadata.asm;

import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;
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
        if (parameter.isNamePresent()) {
            return null;
        }

        final int parameterIndex = Integer.parseInt(parameter.getName().replace("arg", ""));
        final Executable executable = parameter.getDeclaringExecutable();

        return Modifier.isStatic(executable.getModifiers()) ? parameterIndex : parameterIndex + 1;
    }

    public String getName() {
        return parameter.isNamePresent() ? parameter.getName() : null;
    }

    public boolean matches(final MethodParameterMetadata methodParameterMetadata) {
        return methodParameterMetadata.getIndex().equals(getIndex());
    }

}
