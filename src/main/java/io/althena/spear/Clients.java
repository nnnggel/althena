/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.squareup.okhttp.OkHttpClient;

/**
 * clients
 *
 * @author chongyu.yuan
 * @since 2022/2/24
 */
public class Clients {

    // http client
    private static final OkHttpClient client = new OkHttpClient();

    // TODO CHANGEME testnet
    // algod client
    private static final String ALGOD_API_ADDR = "https://testnet-algorand.api.purestake.io/ps2";
    private static final Integer ALGOD_PORT = 443;
    private static final String ALGOD_API_TOKEN_KEY = "X-API-Key";
    private static final String ALGOD_API_TOKEN = "<-- ALGOD_API_TOKEN -->";

    private static final AlgodClient algodClient = new AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN,
        ALGOD_API_TOKEN_KEY);

    public static OkHttpClient getHttpClient() {
        return client;
    }

    public static AlgodClient getAlgodClient() {
        return algodClient;
    }

}
