/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.cal;

import io.althena.spear.model.BasePool;

/**
 * PoolCalculatedResult
 *
 * @author chongyu.yuan
 * @since 2022/1/28
 */
public class PoolCalculatedResult implements Comparable<PoolCalculatedResult> {

    private BasePool pool;
    private AmountInAndOut amountInAndOut;

    public PoolCalculatedResult(BasePool pool, AmountInAndOut amountInAndOut) {
        this.pool = pool;
        this.amountInAndOut = amountInAndOut;
    }

    @Override
    public String toString() {
        return "PoolCalculatedResult{" + "pool=" + pool + ", amountInAndOut=" + amountInAndOut + '}';
    }

    public BasePool getPool() {
        return pool;
    }

    public void setPool(BasePool pool) {
        this.pool = pool;
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
