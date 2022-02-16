/*
 * Copyright (C) 2020 ycy
 */
package io.althena.spear.model;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import java.math.BigInteger;

/**
 * BasePool
 *
 * @author chongyu.yuan
 * @since 2022/2/9
 */
public abstract class BasePool {

    protected String address;
    protected Long asset1Id;
    protected Long asset2Id;
    protected BigInteger asset1Reserves;
    protected BigInteger asset2Reserves;
    protected Long round;

    public BasePool(String address) {
        this.address = address;
    }

    public abstract BasePool refresh(AlgodClient client) throws Exception;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAsset1Id() {
        return asset1Id;
    }

    public void setAsset1Id(Long asset1Id) {
        this.asset1Id = asset1Id;
    }

    public Long getAsset2Id() {
        return asset2Id;
    }

    public void setAsset2Id(Long asset2Id) {
        this.asset2Id = asset2Id;
    }

    public BigInteger getAsset1Reserves() {
        return asset1Reserves;
    }

    public void setAsset1Reserves(BigInteger asset1Reserves) {
        this.asset1Reserves = asset1Reserves;
    }

    public BigInteger getAsset2Reserves() {
        return asset2Reserves;
    }

    public void setAsset2Reserves(BigInteger asset2Reserves) {
        this.asset2Reserves = asset2Reserves;
    }

    public Long getRound() {
        return round;
    }

    public void setRound(Long round) {
        this.round = round;
    }
}
