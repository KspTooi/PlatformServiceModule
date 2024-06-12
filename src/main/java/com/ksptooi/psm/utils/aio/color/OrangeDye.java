package com.ksptooi.psm.utils.aio.color;

public class OrangeDye implements CableDye{

    private OrangeDye(){

    }

    @Override
    public int r() {
        return 230;
    }

    @Override
    public int g() {
        return 146;
    }

    @Override
    public int b() {
        return 11;
    }

    public static OrangeDye pickUp = new OrangeDye();

}
