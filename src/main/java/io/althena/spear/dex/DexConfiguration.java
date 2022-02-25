/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.dex;

import io.althena.spear.cal.PoolCalculator;
import io.althena.spear.pool.PoolFinderRunner;

/**
 * DexConfiguration
 *
 * @author chongyu.yuan
 * @since 2022/2/24
 */
public class DexConfiguration {

    // n/10000
    private int feeBp;

    private PoolFinderRunner poolFinderRunner;

    private Class<? extends PoolCalculator> poolCalculatorClazz;

    public DexConfiguration(int feeBp, PoolFinderRunner poolFinderRunner,
        Class<? extends PoolCalculator> poolCalculatorClazz) {
        this.feeBp = feeBp;
        this.poolFinderRunner = poolFinderRunner;
        this.poolCalculatorClazz = poolCalculatorClazz;
    }

    public int getFeeBp() {
        return feeBp;
    }

    public void setFeeBp(int feeBp) {
        this.feeBp = feeBp;
    }

    public PoolFinderRunner getPoolFinderRunner() {
        return poolFinderRunner;
    }

    public void setPoolFinderRunner(PoolFinderRunner poolFinderRunner) {
        this.poolFinderRunner = poolFinderRunner;
    }

    public Class<? extends PoolCalculator> getPoolCalculatorClazz() {
        return poolCalculatorClazz;
    }

    public void setPoolCalculatorClazz(Class<? extends PoolCalculator> poolCalculatorClazz) {
        this.poolCalculatorClazz = poolCalculatorClazz;
    }
}
