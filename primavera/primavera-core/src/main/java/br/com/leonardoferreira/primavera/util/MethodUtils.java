package br.com.leonardoferreira.primavera.util;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.asm.ClassNode;
import br.com.leonardoferreira.primavera.asm.MethodNode;
import br.com.leonardoferreira.primavera.asm.ParameterNode;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.objectweb.asm.Type;

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
        if (parameter.isNamePresent()) {
            return parameter.getName();
        }

        final int parameterIndex = Integer.parseInt(parameter.getName().replace("arg", ""));

        return Optional.ofNullable(retrieveParameterNames(executable).get(parameterIndex))
                .orElseThrow(() -> new RuntimeException("Parameter not found"));
    }

    public static Map<Integer, String> retrieveParameterNames(final Executable executable) {
        final String executableName = executable instanceof Constructor ? "<init>" : executable.getName();
        final Type[] executableParams = Arrays.stream(executable.getParameterTypes())
                .map(Type::getType)
                .toArray(Type[]::new);

        return ClassNode.fromClass(executable.getDeclaringClass())
                .methods()
                .filter(method -> executableName.equals(method.getName())
                        && Arrays.equals(executableParams, Type.getArgumentTypes(method.getDescriptor())))
                .flatMap(MethodNode::parameters)
                .collect(Collectors.toMap(ParameterNode::getIndex, ParameterNode::getName));
    }

}
