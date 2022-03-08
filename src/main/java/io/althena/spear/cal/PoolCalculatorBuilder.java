/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.cal;

import io.althena.spear.dex.DexConfiguration;
import io.althena.spear.dex.DexRepo;
import io.althena.spear.model.BasePool;
import io.althena.spear.model.Dex;
import java.math.BigDecimal;

/**
 * description
 *
 * @author chongyu.yuan
 * @since 2022/3/4
 */
public class PoolCalculatorBuilder {

    public static PoolCalculator build(BasePool pool, int scale) {
        // dex
        Dex dex = pool.getDex();
        // dex configuration
        DexConfiguration dexConfiguration = DexRepo.getConfig(dex);
        if (dexConfiguration == null) {
            throw new RuntimeException(dex + " dexConfiguration is null");
        }
        // class
        Class<? extends PoolCalculator> clazz = dexConfiguration.getPoolCalculatorClazz();
        // feeBp
        Integer feeBpInPool = pool.getFeeBp();
        BigDecimal feeBp = BigDecimal.valueOf(feeBpInPool == null ? dexConfiguration.getFeeBp() : feeBpInPool);
        try {
            return clazz.getDeclaredConstructor(BasePool.class, BigDecimal.class, Integer.class)
                .newInstance(pool, feeBp, scale);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
