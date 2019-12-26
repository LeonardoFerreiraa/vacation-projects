package br.com.leonardoferreira.primavera.util;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.metadata.asm.ClassMetadata;
import br.com.leonardoferreira.primavera.metadata.asm.MethodMetadata;
import br.com.leonardoferreira.primavera.metadata.asm.MethodParameterMetadata;
import br.com.leonardoferreira.primavera.metadata.asm.NativeMethodMetadata;
import br.com.leonardoferreira.primavera.metadata.asm.NativeParameterMetadata;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodUtils {

    @SuppressWarnings("unchecked")
    public static <T> T invokeWithComponents(final Method method, final Primavera primavera) {
        return Try.rethrowAsRuntime(() -> {
            final Object[] args = Arrays.stream(method.getParameterTypes())
                    .map(primavera::retrieveComponent)
                    .toArray();

            final Object component = primavera.retrieveComponent(method.getDeclaringClass());
            return (T) method.invoke(component, args);
        });
    }

    public static String retrieveParameterName(final Executable executable, final Parameter parameter) {
        final NativeParameterMetadata nativeParameter = NativeParameterMetadata.fromParameter(parameter);

        return Optional.ofNullable(nativeParameter.getName())
                .orElseGet(() -> {
                    final NativeMethodMetadata nativeMethod = NativeMethodMetadata.fromExecutable(executable);

                    return ClassMetadata.fromClass(executable.getDeclaringClass())
                            .methods()
                            .filter(nativeMethod::matches)
                            .flatMap(MethodMetadata::parameters)
                            .filter(nativeParameter::matches)
                            .findFirst()
                            .map(MethodParameterMetadata::getName)
                            .orElseThrow();
                });
    }

}
