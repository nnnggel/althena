/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.dex.fake;

import com.algorand.algosdk.crypto.Address;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.althena.spear.Clients;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import io.althena.spear.model.Dex;
import io.althena.spear.pool.PoolFinderRunner;

/**
 * FakePoolFinderRunner
 *
 * @author chongyu.yuan
 * @since 2022/2/24
 */
public class FakePoolFinderRunner extends PoolFinderRunner {

    private final String PACT_API_BASE_URL = "https://api.testnet.pact.fi";

    @Override
    public BasePool execute(Asset assetA, Asset assetB) {
        // Primary asset always has lower index.
        if (assetA.getId().compareTo(assetB.getId()) > 0) {
            Asset tmp = assetA;
            assetA = assetB;
            assetB = tmp;
        }

        try {
            Request request = new Request.Builder().url(
                PACT_API_BASE_URL + "/api/pools?primary_asset__algoid=" + assetA.getId() + "&secondary_asset__algoid="
                    + assetB.getId()).get().build();
            Response resp = Clients.getHttpClient().newCall(request).execute();
            if (resp.isSuccessful()) {
                JSONArray results = JSONObject.parseObject(resp.body().string()).getJSONArray("results");
                if (results != null && results.size() == 1) {
                    Long appID = Long.valueOf(results.getJSONObject(0).getString("appid"));
                    String contractAddress = Address.forApplication(appID).encodeAsString();
                    return new FakePool(Dex.FAKE, appID, contractAddress, assetA, assetB).refresh(
                        Clients.getAlgodClient());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
