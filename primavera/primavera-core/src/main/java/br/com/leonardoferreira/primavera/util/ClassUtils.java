package br.com.leonardoferreira.primavera.util;

import javassist.ClassPool;
import javassist.CtClass;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassUtils {

    public static CtClass toCtClass(final Class<?> clazz) {
        return Try.silently(() -> ClassPool.getDefault().getCtClass(clazz.getName()));
    }

}
