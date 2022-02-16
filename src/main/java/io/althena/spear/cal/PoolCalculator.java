/*
 * Copyright (C) 2020 ycy
 */
package io.althena.spear.cal;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * PoolCalculator
 *
 * @author chongyu.yuan
 * @since 2022/1/28
 */
public class PoolCalculator {

    private String dex;
    private BigDecimal reserveA;
    private BigDecimal reserveB;
    private BigDecimal feeRate;
    private BigDecimal slippage;

    public PoolCalculator(String dex, BigDecimal reserveA, BigDecimal reserveB, BigDecimal feeRate,
        BigDecimal slippage) {
        this.dex = dex;
        this.feeRate = feeRate;
        this.slippage = slippage;
        this.reserveA = reserveA;
        this.reserveB = reserveB;
    }

    public BigDecimal fixedIn(BigDecimal amountIn, int decimalForamountOut) {
        // TODO
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "PoolCalculator{" + "dex='" + dex + '\'' + ", reserveA=" + reserveA + ", reserveB=" + reserveB
            + ", feeRate=" + feeRate + ", slippage=" + slippage + '}';
    }

    public String getDex() {
        return dex;
    }

    public void setDex(String dex) {
        this.dex = dex;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public BigDecimal getSlippage() {
        return slippage;
    }

    public void setSlippage(BigDecimal slippage) {
        this.slippage = slippage;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    public BigDecimal getReserveA() {
        return reserveA;
    }

    public void setReserveA(BigDecimal reserveA) {
        this.reserveA = reserveA;
    }

    public BigDecimal getReserveB() {
        return reserveB;
    }

    public void setReserveB(BigDecimal reserveB) {
        this.reserveB = reserveB;
    }

}
