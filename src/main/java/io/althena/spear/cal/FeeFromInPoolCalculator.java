/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.cal;

import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * FeeFromInPoolCalculator
 *
 * @author chongyu.yuan
 * @since 2022/2/24
 */
public class FeeFromInPoolCalculator extends PoolCalculator {

    protected FeeFromInPoolCalculator(BasePool pool, BigDecimal feeBp, Integer scale) {
        super(pool, feeBp, scale);
    }

    @Override
    protected BigDecimal _fixedInWithAssetA(Asset assetIn, BigDecimal amountIn) {
        BigDecimal swapFee = amountIn.multiply(feeBp).divide(BigDecimal.valueOf(10000L), scale, RoundingMode.FLOOR);
        BigDecimal amountInDeductFee = amountIn.subtract(swapFee);

        // k = reserveA * reserveB
        // amountOut[assetA]
        // = reserveB - k / (reserveA + amountIn)
        // = reserveB - reserveA * reserveB / (reserveA + amountIn)
        // = reserveB * (1 - reserveA / (reserveA + amountIn))
        // = reserveB * ((reserveA + amountIn)/(reserveA + amountIn) - reserveA / (reserveA + amountIn))
        // = reserveB * ([(reserveA + amountIn) - reserveA] / (reserveA + amountIn))
        // = reserveB * amountIn / (reserveA + amountIn)
        BigDecimal amountOut = reserveB.multiply(amountInDeductFee)
            .divide(reserveA.add(amountInDeductFee), scale, RoundingMode.FLOOR);

        // update reserve
        reserveA = reserveA.add(amountInDeductFee);
        reserveB = reserveB.subtract(amountOut);

        return amountOut;
    }

    @Override
    protected BigDecimal _fixedInWithAssetB(Asset assetIn, BigDecimal amountIn) {
        BigDecimal swapFee = amountIn.multiply(feeBp).divide(BigDecimal.valueOf(10000L), scale, RoundingMode.FLOOR);
        BigDecimal amountInDeductFee = amountIn.subtract(swapFee);

        // k = reserveA * reserveB
        // amountOut[assetB]
        // = reserveA - k / (reserveB + amountIn)
        // = reserveA - reserveA * reserveB / (reserveB + amountIn)
        // = reserveA * (1 - reserveB / (reserveB + amountIn))
        // = reserveA * ((reserveB + amountIn)/(reserveB + amountIn) - reserveB / (reserveB + amountIn))
        // = reserveA * ([(reserveB + amountIn) - reserveB] / (reserveB + amountIn))
        // = reserveA * amountIn / (reserveB + amountIn)
        BigDecimal amountOut = reserveA.multiply(amountInDeductFee)
            .divide(reserveB.add(amountInDeductFee), scale, RoundingMode.FLOOR);

        // update reserve
        reserveA = reserveA.subtract(amountInDeductFee);
        reserveB = reserveB.add(amountOut);

        return amountOut;
    }

}
