/*
 * Copyright (C) 2020 ycy
 */
package io.althena.spear.cal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.althena.spear.model.Asset;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * PathFinder
 *
 * @author chongyu.yuan
 * @since 2022/1/27
 */
public class PathFinder {

    private int copies;

    public PathFinder(int copies) {
        this.copies = copies;
    }

    // Map<SimplePool,AmountInAndOut>
    public Map<PoolDesc, AmountInAndOut> findByFixedIn(Asset assetA, Asset assetB, List<PoolCalculator> poolCalculators,
        BigInteger amountInTotal) {
        // TODO
        return Maps.newHashMap();
    }

    public static void main(String[] args) {
        // mock assets
        Asset assetA = new Asset(0L).setName("Algo").setUnitName("ALGO").setDecimals(6L);
        Asset assetB = new Asset(21582668L).setName("TestUsdc").setUnitName("USDC").setDecimals(6L);

        // mock pools
        PoolCalculator pool1 = new PoolCalculator("dex1", BigDecimal.valueOf(10000), BigDecimal.valueOf(100),
            BigDecimal.ZERO, BigDecimal.ZERO);
        PoolCalculator pool2 = new PoolCalculator("dex2", BigDecimal.valueOf(15000), BigDecimal.valueOf(200),
            BigDecimal.ZERO, BigDecimal.ZERO);

        // call pathfinder
        PathFinder pathFinder = new PathFinder(10);
        BigInteger amountIn = BigInteger.valueOf(10000L);

        Map<PoolDesc, AmountInAndOut> res = pathFinder.findByFixedIn(assetA, assetB, Lists.newArrayList(pool1, pool2),
            amountIn);
        res.entrySet().stream().forEach(e -> System.out.println(e.getKey().getDex() + " -> " + e.getValue()));
    }
}
