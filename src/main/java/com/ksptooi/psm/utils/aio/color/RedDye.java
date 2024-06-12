package com.ksptooi.psm.utils.aio.color;

public class RedDye implements CableDye{

    private RedDye(){

    }

    @Override
    public int r() {
        return 230;
    }

    @Override
    public int g() {
        return 11;
    }

    @Override
    public int b() {
        return 73;
    }

    public static RedDye pickUp = new RedDye();

}
