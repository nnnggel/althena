/*
 * Copyright (C) 2020 ycy
 */
package io.althena.spear.model;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import io.althena.spear.Utils;
import java.util.HashMap;
import java.util.Map;

/**
 * Asset
 *
 * @author chongyu.yuan
 * @since 2022/2/9
 */
public class Asset {

    private Long id;
    private String name;
    private String unitName;
    private Long decimals;

    private static final Map<Long, Asset> cache = new HashMap<>();

    public Asset(Long id) {
        this.id = id;
    }

    public Asset fetch(AlgodClient client) throws Exception {
        Asset cached = Asset.cache.get(this.id);
        if (cached != null) {
            return cached;
        }

        if (this.id != 0) {
            com.algorand.algosdk.v2.client.model.AssetParams assetParams = Utils.getAssetInfo(client, id).params;
            this.name = assetParams.name;
            this.unitName = assetParams.unitName;
            this.decimals = assetParams.decimals;
        } else {
            this.name = "Algo";
            this.unitName = "ALGO";
            this.decimals = 6L;
        }
        cache.put(this.id, this);
        return this;
    }

    public Long getId() {
        return id;
    }

    public Asset setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Asset setName(String name) {
        this.name = name;
        return this;
    }

    public String getUnitName() {
        return unitName;
    }

    public Asset setUnitName(String unitName) {
        this.unitName = unitName;
        return this;
    }

    public Long getDecimals() {
        return decimals;
    }

    public Asset setDecimals(Long decimals) {
        this.decimals = decimals;
        return this;
    }

    @Override
    public String toString() {
        return "Asset{" + "id=" + id + ", name='" + name + '\'' + ", unitName='" + unitName + '\'' + ", decimals="
            + decimals + '}';
    }
}
