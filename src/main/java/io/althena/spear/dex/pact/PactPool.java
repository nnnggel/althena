/*
 * Copyright (C) 2020 ycy
 */
package io.althena.spear.dex.pact;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import io.althena.spear.Utils;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import io.althena.spear.model.Dex;
import java.math.BigInteger;
import java.util.Map;

/**
 * PactPool
 *
 * @author chongyu.yuan
 * @since 2022/2/11
 */
public class PactPool extends BasePool {

    public PactPool(Dex dex, Long appID, String address, Asset assetA, Asset assetB) {
        super(dex, appID, address, assetA, assetB);
    }

    @Override
    public PactPool refresh(AlgodClient client) throws Exception {
        Map<String, Object> applicationGlobalState = Utils.getApplicationGlobalState(client, appID);
        this.assetAReserves = (BigInteger) applicationGlobalState.get("A");
        this.assetBReserves = (BigInteger) applicationGlobalState.get("B");
        return this;
    }

}
