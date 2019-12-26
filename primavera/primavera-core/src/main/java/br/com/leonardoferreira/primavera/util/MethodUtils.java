package br.com.leonardoferreira.primavera.util;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.functional.Pair;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
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

    public static List<Pair<String, Parameter>> retrieveParametersWithNames(final Method method) {
        final CtMethod ctMethod = toCtMethod(method);

        return Optional.of(ctMethod.getMethodInfo())
                .map(MethodInfo::getCodeAttribute)
                .map(codeAttribute -> (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag))
                .map(attr ->
                        Optional.ofNullable(Try.silently(ctMethod::getParameterTypes))
                                .map(parameters -> IntStream.range(0, parameters.length))
                                .map(stream -> stream.boxed().map(i -> Pair.of(attr.variableName(i + 1), method.getParameters()[i])))
                                .stream()
                                .flatMap(Function.identity())
                                .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
    }

    public static CtMethod toCtMethod(final Method method) {
        return Optional.of(method.getDeclaringClass())
                .map(ClassUtils::toCtClass)
                .map(ctClass ->
                        Try.silently(() ->
                                ctClass.getDeclaredMethod(
                                        method.getName(),
                                        Arrays.stream(method.getParameterTypes())
                                                .map(ClassUtils::toCtClass)
                                                .toArray(CtClass[]::new)
                                )
                        )
                )
                .orElseThrow(() -> new RuntimeException("Unable to convert to CtMethod"));
    }

}
