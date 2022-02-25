/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear;

import io.althena.spear.cal.AmountInAndOut;
import io.althena.spear.cal.FeeFromOutPoolCalculator;
import io.althena.spear.cal.PathFinder;
import io.althena.spear.cal.PoolCalculator;
import io.althena.spear.dex.DexConfiguration;
import io.althena.spear.dex.DexRepo;
import io.althena.spear.dex.fake.FakePoolFinderRunner;
import io.althena.spear.dex.pact.PactPoolFinderRunner;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import io.althena.spear.model.Dex;
import io.althena.spear.pool.PoolFinder;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author chongyu.yuan
 * @since 2022/2/24
 */
public class MainTest {

    public static void main(String[] args) throws Exception {
        // register dexes
                DexRepo.register(Dex.FAKE,
                    new DexConfiguration(30, new FakePoolFinderRunner(), FeeFromOutPoolCalculator.class));
        DexRepo.register(Dex.PACT,
            new DexConfiguration(30, new PactPoolFinderRunner(), FeeFromOutPoolCalculator.class));

        // mock assets
        Asset ALGO = new Asset(0L).fetch(Clients.getAlgodClient());
        Asset USDC = new Asset(67395862L).fetch(Clients.getAlgodClient());

        // find available pools
        List<BasePool> pools = PoolFinder.find(ALGO, USDC);
        if (pools.size() == 0) {
            throw new RuntimeException("pools size is zero.");
        }

        // init pathfinder
        BigInteger amountIn = BigInteger.valueOf(1_000_000L);
        // TODO determine copis(power of 10), based_ecimal(10=1,100=2,1000=3...) by estimating amountIn value
        PathFinder pathFinder = new PathFinder(100);
        int decimal = 2 + USDC.getDecimals().intValue();

        // build calculated pool
        List<PoolCalculator> poolCalculators = pools.stream().map(pool -> {
            Dex dex = pool.getDex();
            DexConfiguration dexConfiguration = DexRepo.getConfig(dex);
            return PoolCalculator.newInstance(dexConfiguration.getPoolCalculatorClazz(), pool,
                BigDecimal.valueOf(dexConfiguration.getFeeBp()), decimal);
        }).collect(Collectors.toList());
        System.out.println(poolCalculators);

        // calculate and group
        Map<BasePool, AmountInAndOut> res = pathFinder.findByFixedIn(poolCalculators, ALGO, amountIn);
        res.entrySet().stream().forEach(
            e -> System.out.println(e.getKey().getDex() + ":" + e.getKey().getAppID() + " -> " + e.getValue()));

        // prepare txns
    }
}
