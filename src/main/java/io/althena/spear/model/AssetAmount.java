/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.model;

import java.math.BigInteger;

/**
 * AssetAmount
 *
 * @author chongyu.yuan
 * @since 2022/2/9
 */
public class AssetAmount {

    private Asset asset;
    private BigInteger amount;

    public AssetAmount(Asset asset, BigInteger amount) {
        this.asset = asset;
        this.amount = amount;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AssetAmount{" + "asset=" + asset + ", amount=" + amount + '}';
    }
}
