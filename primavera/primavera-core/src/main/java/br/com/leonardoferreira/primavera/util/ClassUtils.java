package br.com.leonardoferreira.primavera.util;

import java.util.Optional;
import javassist.ClassPool;
import javassist.CtClass;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassUtils {

    public static CtClass toCtClass(final Class<?> clazz) {
        return Optional.of(ClassPool.getDefault())
                .map(pool -> Try.silently(() -> pool.getCtClass(clazz.getName())))
                .orElseThrow(() -> new RuntimeException("Unable to convert to CtClass"));
    }

}
