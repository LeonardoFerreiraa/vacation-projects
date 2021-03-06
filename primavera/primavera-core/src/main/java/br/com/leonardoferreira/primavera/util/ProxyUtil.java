package br.com.leonardoferreira.primavera.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProxyUtil {

    public static <T> T createProxy(final Class<T> clazz, final InvocationHandler handler) {
        final Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, handler);
        return clazz.cast(instance);
    }

}
