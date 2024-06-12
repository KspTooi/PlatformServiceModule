package com.ksptooi.psm.utils.aio.color;

public class ColorfulDye implements CableDye{

    private final int r;
    private final int g;
    private final int b;

    public ColorfulDye(int r,int g,int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public int r() {
        return r;
    }

    @Override
    public int g() {
        return g;
    }

    @Override
    public int b() {
        return b;
    }
}
