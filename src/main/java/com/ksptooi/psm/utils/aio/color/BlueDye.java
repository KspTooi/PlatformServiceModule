package com.ksptooi.psm.utils.aio.color;

public class BlueDye implements CableDye{

    private BlueDye(){

    }

    @Override
    public int r() {
        return 11;
    }

    @Override
    public int g() {
        return 135;
    }

    @Override
    public int b() {
        return 230;
    }

    public static BlueDye pickUp = new BlueDye();

}
