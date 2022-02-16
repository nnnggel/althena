/*
 * Copyright (C) 2020 ycy
 */
package io.althena.spear.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * SwapQuote
 *
 * @author chongyu.yuan
 * @since 2022/2/9
 */
public class SwapQuote {

    private SwapType swapType;
    private AssetAmount amountIn;
    private AssetAmount amountOut;
    private BigInteger swapFees;
    private BigDecimal slippage;

    public SwapQuote(SwapType swapType, AssetAmount amountIn, AssetAmount amountOut, BigInteger swapFees,
        BigDecimal slippage) {
        this.swapType = swapType;
        this.amountIn = amountIn;
        this.amountOut = amountOut;
        this.swapFees = swapFees;
        this.slippage = slippage;
    }

    public BigInteger amountOutWithSlippage() {
        if (SwapType.FIXED_OUTPUT == swapType) {
            return this.amountOut.getAmount();
        } else {
            return new BigDecimal(amountOut.getAmount()).multiply(BigDecimal.ONE.subtract(slippage)).toBigInteger();
        }
    }

    public BigInteger amountInWithSlippage() {
        if (SwapType.FIXED_INPUT == swapType) {
            return this.amountIn.getAmount();
        } else {
            return new BigDecimal(amountIn.getAmount()).multiply(BigDecimal.ONE.subtract(slippage)).toBigInteger();
        }
    }

    public BigDecimal price() {
        return new BigDecimal(this.amountOut.getAmount()).divide(new BigDecimal(this.amountIn.getAmount()),
            amountIn.getAsset().getDecimals().intValue(), RoundingMode.FLOOR);
    }

    public BigDecimal priceWithSlippage() {
        return new BigDecimal(this.amountOutWithSlippage()).divide(new BigDecimal(this.amountInWithSlippage()),
            amountIn.getAsset().getDecimals().intValue(), RoundingMode.FLOOR);
    }

    public SwapType getSwapType() {
        return swapType;
    }

    public void setSwapType(SwapType swapType) {
        this.swapType = swapType;
    }

    public AssetAmount getAmountIn() {
        return amountIn;
    }

    public void setAmountIn(AssetAmount amountIn) {
        this.amountIn = amountIn;
    }

    public AssetAmount getAmountOut() {
        return amountOut;
    }

    public void setAmountOut(AssetAmount amountOut) {
        this.amountOut = amountOut;
    }

    public BigInteger getSwapFees() {
        return swapFees;
    }

    public void setSwapFees(BigInteger swapFees) {
        this.swapFees = swapFees;
    }

    public BigDecimal getSlippage() {
        return slippage;
    }

    public void setSlippage(BigDecimal slippage) {
        this.slippage = slippage;
    }

    @Override
    public String toString() {
        return "SwapQuote{" + "swapType='" + swapType + '\'' + ", amountIn=" + amountIn + ", amountOut=" + amountOut
            + ", swapFees=" + swapFees + ", slippage=" + slippage + '}';
    }
}
