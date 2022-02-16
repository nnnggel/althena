/*
 * Copyright (C) 2020 ycy
 */
package io.althena.spear.cal;

import io.althena.spear.model.Asset;

/**
 * PoolDesc
 *
 * @author chongyu.yuan
 * @since 2022/1/28
 */
public class PoolDesc {

    private String dex;
    private Asset assetA;
    private Asset assetB;

    public PoolDesc(String dex, Asset assetA, Asset assetB) {
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

    public String getDex() {
        return dex;
    }

    public void setDex(String dex) {
        this.dex = dex;
    }
}
