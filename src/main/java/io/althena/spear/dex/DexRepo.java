/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.dex;

import io.althena.spear.model.Dex;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DexConfiguration
 *
 * @author chongyu.yuan
 * @since 2022/2/24
 */
public class DexRepo {

    private static final Map<Dex, DexConfiguration> _repo = new ConcurrentHashMap<>();

    public static void register(Dex dex, DexConfiguration configuration) {
        _repo.put(dex, configuration);
    }

    public static void unregister(Dex dex) {
        _repo.remove(dex);
    }

    public static Set<Dex> getDexes() {
        return _repo.keySet();
    }

    public static DexConfiguration getConfig(Dex dex) {
        return _repo.get(dex);
    }
}
