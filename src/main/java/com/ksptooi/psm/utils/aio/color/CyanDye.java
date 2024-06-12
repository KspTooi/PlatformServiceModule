package com.ksptooi.psm.utils.aio.color;

public class CyanDye implements CableDye{

    private CyanDye(){

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
        return 230;
    }

    public static CyanDye pickUp = new CyanDye();

}
