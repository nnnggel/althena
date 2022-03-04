/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.cal;

import io.althena.spear.dex.DexConfiguration;
import io.althena.spear.dex.DexRepo;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import io.althena.spear.model.Dex;
import java.math.BigDecimal;

/**
 * PoolCalculator
 *
 * @author chongyu.yuan
 * @since 2022/1/28
 */
public abstract class PoolCalculator {

    protected BasePool pool;
    protected Dex dex;
    protected Asset assetA;
    protected Asset assetB;
    protected BigDecimal reserveA;
    protected BigDecimal reserveB;
    protected BigDecimal feeBp;
    protected int scale;

    protected PoolCalculator(BasePool pool, BigDecimal feeBp, Integer scale) {
        this.pool = pool;
        this.dex = pool.getDex();
        this.assetA = pool.getAssetA();
        this.assetB = pool.getAssetB();
        this.reserveA = new BigDecimal(pool.getAssetAReserves());
        this.reserveB = new BigDecimal(pool.getAssetBReserves());
        this.feeBp = feeBp;
        this.scale = scale;
    }

    public BigDecimal fixedIn(Asset assetIn, BigDecimal amountIn) {
        if (assetIn.equals(assetA)) {
            return _fixedInWithAssetA(assetIn, amountIn);
        } else if (assetIn.equals(assetB)) {
            return _fixedInWithAssetB(assetIn, amountIn);
        } else {
            throw new RuntimeException("assetIn must be assetA or assetB");
        }
    }

    protected abstract BigDecimal _fixedInWithAssetA(Asset assetIn, BigDecimal amountIn);

    protected abstract BigDecimal _fixedInWithAssetB(Asset assetIn, BigDecimal amountIn);

    public static PoolCalculator newInstance(BasePool pool, int scale) {
        // dex
        Dex dex = pool.getDex();
        // dex configuration
        DexConfiguration dexConfiguration = DexRepo.getConfig(dex);
        if (dexConfiguration != null) {
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
        }
        return null;
    }

    @Override
    public String toString() {
        return "PoolCalculator{" + "pool=" + pool + ", dex=" + dex + ", assetA=" + assetA + ", assetB=" + assetB
            + ", reserveA=" + reserveA + ", reserveB=" + reserveB + ", feeBp=" + feeBp + ", scale=" + scale + '}';
    }

    public BasePool getPool() {
        return pool;
    }

    public void setPool(BasePool pool) {
        this.pool = pool;
    }

    public Dex getDex() {
        return dex;
    }

    public void setDex(Dex dex) {
        this.dex = dex;
    }

    public BigDecimal getFeeBp() {
        return feeBp;
    }

    public void setFeeBp(BigDecimal feeBp) {
        this.feeBp = feeBp;
    }

    public Asset getAssetA() {
        return assetA;
    }

    public void setAssetA(Asset assetA) {
        this.assetA = assetA;
    }

    public Asset getAssetB() {
        return assetB;
    }

    public void setAssetB(Asset assetB) {
        this.assetB = assetB;
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

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
