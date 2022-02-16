/*
 * Copyright (C) 2020 ycy
 */
package io.althena.spear.cal;

/**
 * PoolCalculatedResult
 *
 * @author chongyu.yuan
 * @since 2022/1/28
 */
public class PoolCalculatedResult implements Comparable<PoolCalculatedResult> {

    private PoolDesc poolDesc;
    private AmountInAndOut amountInAndOut;

    public PoolCalculatedResult(PoolDesc poolDesc, AmountInAndOut amountInAndOut) {
        this.poolDesc = poolDesc;
        this.amountInAndOut = amountInAndOut;
    }

    @Override
    public String toString() {
        return "PoolCalculatedResult{" + "poolDesc=" + poolDesc + ", amountInAndOut=" + amountInAndOut + '}';
    }

    public PoolDesc getPoolDesc() {
        return poolDesc;
    }

    public void setPoolDesc(PoolDesc poolDesc) {
        this.poolDesc = poolDesc;
    }

    public AmountInAndOut getAmountInAndOut() {
        return amountInAndOut;
    }

    public void setAmountInAndOut(AmountInAndOut amountInAndOut) {
        this.amountInAndOut = amountInAndOut;
    }

    @Override
    public int compareTo(PoolCalculatedResult o) {
        return this.amountInAndOut.getAmountOut().compareTo(o.getAmountInAndOut().getAmountOut());
    }
}
