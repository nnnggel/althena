/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.cal;

import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * FeeFromOutPoolCalculator
 *
 * @author chongyu.yuan
 * @since 2022/2/24
 */
public class FeeFromOutPoolCalculator extends PoolCalculator {

    protected FeeFromOutPoolCalculator(BasePool pool, BigDecimal feeBp, Integer scale) {
        super(pool, feeBp, scale);
    }

    @Override
    protected BigDecimal _fixedInWithAssetA(Asset assetIn, BigDecimal amountIn) {
        // k = reserveA * reserveB
        // amountOut[assetA]
        // = reserveB - k / (reserveA + amountIn)
        // = reserveB - reserveA * reserveB / (reserveA + amountIn)
        // = reserveB * (1 - reserveA / (reserveA + amountIn))
        // = reserveB * ((reserveA + amountIn)/(reserveA + amountIn) - reserveA / (reserveA + amountIn))
        // = reserveB * ([(reserveA + amountIn) - reserveA] / (reserveA + amountIn))
        // = reserveB * amountIn / (reserveA + amountIn)
        BigDecimal amountOutBeforeDeductFee = reserveB.multiply(amountIn)
            .divide(reserveA.add(amountIn), scale, RoundingMode.FLOOR);
        BigDecimal amountOut = amountOutBeforeDeductFee.multiply(BigDecimal.valueOf(10000L).subtract(feeBp))
            .divide(BigDecimal.valueOf(10000L));
        //        BigInteger swapFee = amountOutBeforeDeductFee.subtract(amountOut);

        // update reserve
        reserveA = reserveA.add(amountIn);
        reserveB = reserveB.subtract(amountOutBeforeDeductFee);

        return amountOut;
    }

    @Override
    protected BigDecimal _fixedInWithAssetB(Asset assetIn, BigDecimal amountIn) {
        // k = reserveA * reserveB
        // amountOut[assetB]
        // = reserveA - k / (reserveB + amountIn)
        // = reserveA - reserveA * reserveB / (reserveB + amountIn)
        // = reserveA * (1 - reserveB / (reserveB + amountIn))
        // = reserveA * ((reserveB + amountIn)/(reserveB + amountIn) - reserveB / (reserveB + amountIn))
        // = reserveA * ([(reserveB + amountIn) - reserveB] / (reserveB + amountIn))
        // = reserveA * amountIn / (reserveB + amountIn)
        BigDecimal amountOutBeforeDeductFee = reserveA.multiply(amountIn)
            .divide(reserveB.add(amountIn), scale, RoundingMode.FLOOR);
        BigDecimal amountOut = amountOutBeforeDeductFee.multiply(BigDecimal.valueOf(10000L).subtract(feeBp))
            .divide(BigDecimal.valueOf(10000L));
        //        BigInteger swapFee = amountOutBeforeDeductFee.subtract(amountOut);

        // update reserve
        reserveA = reserveA.subtract(amountIn);
        reserveB = reserveB.add(amountOutBeforeDeductFee);

        return amountOut;
    }

}
