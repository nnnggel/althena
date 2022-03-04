/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.dex.algofi;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import io.althena.spear.Clients;
import io.althena.spear.Utils;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import io.althena.spear.model.Dex;
import java.math.BigInteger;
import java.util.Map;

/**
 * description
 *
 * @author chongyu.yuan
 * @since 2022/3/3
 */
public class AlgofiPool extends BasePool {

    public AlgofiPool(Dex dex, Long appID, String address, Asset assetA, Asset assetB, AlgofiPoolType poolType) {
        super(dex, appID, address, assetA, assetB);
        this.feeBp = poolType.getSwapFeeBp();
    }

    @Override
    public AlgofiPool refresh(AlgodClient client) throws Exception {
        Map<String, Object> applicationGlobalState = Utils.getApplicationGlobalState(Clients.getAlgodClient(),
            this.appID);
        this.assetAReserves = (BigInteger) applicationGlobalState.get("b1");
        this.assetBReserves = (BigInteger) applicationGlobalState.get("b2");
        return this;
    }

}
