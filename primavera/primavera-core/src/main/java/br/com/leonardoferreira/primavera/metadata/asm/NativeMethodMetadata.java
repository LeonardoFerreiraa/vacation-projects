package br.com.leonardoferreira.primavera.metadata.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.objectweb.asm.Type;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NativeMethodMetadata {

    private final String name;

    private final Type[] parameterTypes;

    @EqualsAndHashCode.Exclude
    private final Executable executable;

    public static NativeMethodMetadata fromExecutable(final Executable executable) {
        final String executableName = executable instanceof Constructor ? "<init>" : executable.getName();
        final Type[] executableParams = Arrays.stream(executable.getParameterTypes())
                .map(Type::getType)
                .toArray(Type[]::new);

        return new NativeMethodMetadata(executableName, executableParams, executable);
    }

    public boolean matches(final String name, final Type[] parameterTypes) {
        return this.name.equals(name) && Arrays.equals(this.parameterTypes, parameterTypes);
    }

    public boolean matches(final NativeMethodMetadata nativeMethod) {
        return equals(nativeMethod);
    }

    public boolean matches(final MethodMetadata methodMetadata) {
        return matches(methodMetadata.getNativeMethod());
    }

    public boolean isStatic() {
        return Modifier.isStatic(executable.getModifiers());
    }
}
