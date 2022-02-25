/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.cal;

import com.google.common.collect.Lists;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
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

    // Map<BasePool,AmountInAndOut>
    public Map<BasePool, AmountInAndOut> findByFixedIn(List<PoolCalculator> poolCalculators, Asset assetIn,
        BigInteger amountIn) {
        // ignore check
        BigDecimal amountInTotalD = new BigDecimal(amountIn);
        BigDecimal amountInSplited = amountInTotalD.divide(BigDecimal.valueOf(copies), assetIn.getDecimals().intValue(),
            RoundingMode.FLOOR);

        List<PoolCalculatedResult> poolCalculatedResults = Lists.newArrayList();
        for (int i = 0; i < poolCalculators.size(); i++) {
            PoolCalculator poolCalculator = poolCalculators.get(i);
            BasePool pool = poolCalculator.getPool();
            for (int j = 0; j < copies; j++) {
                BigDecimal amountOut = poolCalculator.fixedIn(assetIn, amountInSplited);
                poolCalculatedResults.add(
                    new PoolCalculatedResult(pool, new AmountInAndOut(amountInSplited, amountOut)));
            }
        }

        // sort and split
        Collections.sort(poolCalculatedResults);
        Collections.reverse(poolCalculatedResults);
        poolCalculatedResults = poolCalculatedResults.subList(0, copies);
        //        amountPoolPairs.forEach(System.out::println);

        final Map<BasePool, AmountInAndOut> res = new HashMap<>();
        poolCalculatedResults.forEach(poolCalculatedResult -> {
            BasePool pool = poolCalculatedResult.getPool();
            AmountInAndOut amountInAndOutSum = res.get(pool);
            AmountInAndOut amountInAndOutEach = poolCalculatedResult.getAmountInAndOut();
            if (amountInAndOutSum == null) {
                // init
                amountInAndOutSum = new AmountInAndOut(amountInAndOutEach.getAmountIn(),
                    amountInAndOutEach.getAmountOut());
                res.put(pool, amountInAndOutSum);
            } else {
                // sum
                amountInAndOutSum.addAmountIn(amountInAndOutEach.getAmountIn())
                    .addAmountOut(amountInAndOutEach.getAmountOut());
            }
        });

        return res;
    }

}
