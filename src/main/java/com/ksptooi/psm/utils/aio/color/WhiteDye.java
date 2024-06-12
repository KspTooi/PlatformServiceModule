package com.ksptooi.psm.utils.aio.color;

public class WhiteDye implements CableDye{

    private WhiteDye(){

    }

    @Override
    public int r() {
        return 255;
    }

    @Override
    public int g() {
        return 255;
    }

    @Override
    public int b() {
        return 255;
    }

    public static WhiteDye pickUp = new WhiteDye();

}
