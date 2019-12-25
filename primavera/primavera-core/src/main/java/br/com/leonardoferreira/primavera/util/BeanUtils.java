package br.com.leonardoferreira.primavera.util;

import br.com.leonardoferreira.primavera.stereotype.Component;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtils {

    public static String beanName(final Class<?> clazz, final Component component) {
        return StringUtils.isBlank(component.name()) ? clazz.getSimpleName() : component.name();
    }

}
