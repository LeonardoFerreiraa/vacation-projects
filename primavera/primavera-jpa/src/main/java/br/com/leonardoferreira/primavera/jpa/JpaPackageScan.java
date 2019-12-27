package br.com.leonardoferreira.primavera.jpa;

import br.com.leonardoferreira.primavera.scanner.PackageScan;

public class JpaPackageScan implements PackageScan {

    @Override
    public String getPackage() {
        return JpaPackageScan.class.getPackageName();
    }

}
