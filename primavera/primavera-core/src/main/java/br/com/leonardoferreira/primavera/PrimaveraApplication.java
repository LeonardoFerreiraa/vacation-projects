package br.com.leonardoferreira.primavera;

public class PrimaveraApplication {

    public static Primavera runCliApplication(final Class<?> baseClass) {
        return runApplication(PrimaveraType.CLI, baseClass);
    }

    public static Primavera runWebApplication(final Class<?> baseClass) {
        return runApplication(PrimaveraType.WEB, baseClass);
    }

    public static Primavera runApplication(final PrimaveraType primaveraType, final Class<?> baseClass) {
        final Primavera primavera = Primavera.of(primaveraType);
        primavera.scan(baseClass);
        primavera.run();
        return primavera;
    }

}
