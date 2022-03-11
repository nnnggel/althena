/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.cal;

import io.althena.spear.model.Asset;
import io.althena.spear.model.Dex;

/**
 * PoolDesc
 *
 * @author chongyu.yuan
 * @since 2022/1/28
 */
public class PoolDesc {

    private Dex dex;
    private Asset assetA;
    private Asset assetB;

    public PoolDesc(Dex dex, Asset assetA, Asset assetB) {
        this.dex = dex;
        this.assetA = assetA;
        this.assetB = assetB;
    }


    @Override
    public String toString() {
        return "PoolDesc{" + "dex='" + dex + '\'' + ", assetA=" + assetA + ", assetB=" + assetB + '}';
    }

    public Asset getAssetA() {
        return assetA;
    }

    public void setAssetA(Asset assetA) {
        this.assetA = assetA;
    }

    public Asset getAssetB() {
        return assetB;
    }

    public void setAssetB(Asset assetB) {
        this.assetB = assetB;
    }

    public Dex getDex() {
        return dex;
    }

    public void setDex(Dex dex) {
        this.dex = dex;
    }
}
