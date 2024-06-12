package com.ksptooi.psm.utils.aio.color;

public class PurpleDye implements CableDye{

    private PurpleDye(){

    }

    @Override
    public int r() {
        return 124;
    }

    @Override
    public int g() {
        return 11;
    }

    @Override
    public int b() {
        return 230;
    }

    public static PurpleDye pickUp = new PurpleDye();

}
