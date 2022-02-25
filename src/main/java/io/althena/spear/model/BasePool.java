/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.model;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import java.math.BigInteger;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * BasePool
 *
 * @author chongyu.yuan
 * @since 2022/2/9
 */
public abstract class BasePool {

    protected Dex dex;
    protected Long appID;
    protected String address;
    protected Asset assetA;
    protected Asset assetB;
    protected BigInteger assetAReserves;
    protected BigInteger assetBReserves;
    protected Long round;

    public BasePool(Dex dex, Long appID, String address, Asset assetA, Asset assetB) {
        this.dex = dex;
        this.appID = appID;
        this.address = address;
        this.assetA = assetA;
        this.assetB = assetB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BasePool basePool = (BasePool) o;

        return new EqualsBuilder().append(dex, basePool.dex).append(appID, basePool.appID).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(dex).append(appID).toHashCode();
    }

    public abstract BasePool refresh(AlgodClient client) throws Exception;

    public Dex getDex() {
        return dex;
    }

    public void setDex(Dex dex) {
        this.dex = dex;
    }

    public Long getAppID() {
        return appID;
    }

    public void setAppID(Long appID) {
        this.appID = appID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public BigInteger getAssetAReserves() {
        return assetAReserves;
    }

    public void setAssetAReserves(BigInteger assetAReserves) {
        this.assetAReserves = assetAReserves;
    }

    public BigInteger getAssetBReserves() {
        return assetBReserves;
    }

    public void setAssetBReserves(BigInteger assetBReserves) {
        this.assetBReserves = assetBReserves;
    }

    public Long getRound() {
        return round;
    }

    public void setRound(Long round) {
        this.round = round;
    }
}
