package com.ksptooi.psm.utils.aio.color;

public class GreenDye implements CableDye{

    private GreenDye(){

    }

    @Override
    public int r() {
        return 11;
    }

    @Override
    public int g() {
        return 230;
    }

    @Override
    public int b() {
        return 11;
    }

    public static GreenDye pickUp = new GreenDye();

}
