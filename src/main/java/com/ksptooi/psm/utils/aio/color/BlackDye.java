package com.ksptooi.psm.utils.aio.color;

public class BlackDye implements CableDye{

    private BlackDye(){

    }

    @Override
    public int r() {
        return 0;
    }

    @Override
    public int g() {
        return 0;
    }

    @Override
    public int b() {
        return 0;
    }

    public static BlackDye pickUp = new BlackDye();

}
