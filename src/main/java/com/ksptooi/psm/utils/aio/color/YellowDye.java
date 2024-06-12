package com.ksptooi.psm.utils.aio.color;

public class YellowDye implements CableDye{

    private YellowDye(){

    }

    @Override
    public int r() {
        return 230;
    }

    @Override
    public int g() {
        return 223;
    }

    @Override
    public int b() {
        return 11;
    }

    public static YellowDye pickUp = new YellowDye();

}
