package br.com.leonardoferreira.primavera.provider;

import br.com.leonardoferreira.primavera.PrimaveraType;

public class PrimaveraCliProvider extends PrimaveraProvider {

    @Override
    public PrimaveraType type() {
        return PrimaveraType.CLI;
    }

}
