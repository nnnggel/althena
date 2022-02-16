/*
 * Copyright (C) 2020 ycy
 */
package io.althena.spear.cal;

import java.math.BigDecimal;

/**
 * AmountInAndOut
 *
 * @author chongyu.yuan
 * @since 2022/1/28
 */
public class AmountInAndOut {

    private BigDecimal amountIn;
    private BigDecimal amountOut;

    public AmountInAndOut(BigDecimal amountIn, BigDecimal amountOut) {
        this.amountIn = amountIn;
        this.amountOut = amountOut;
    }

    @Override
    public String toString() {
        return "AmountInAndOut{" + "amountIn=" + amountIn + ", amountOut=" + amountOut + '}';
    }

    public AmountInAndOut addAmountIn(BigDecimal amount) {
        this.amountIn = this.amountIn.add(amount);
        return this;
    }

    public AmountInAndOut addAmountOut(BigDecimal amount) {
        this.amountOut = this.amountOut.add(amount);
        return this;
    }

    public BigDecimal getAmountIn() {
        return amountIn;
    }

    public void setAmountIn(BigDecimal amountIn) {
        this.amountIn = amountIn;
    }

    public BigDecimal getAmountOut() {
        return amountOut;
    }

    public void setAmountOut(BigDecimal amountOut) {
        this.amountOut = amountOut;
    }
}
