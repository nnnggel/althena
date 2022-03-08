/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.pool;

import com.google.common.collect.Lists;
import io.althena.spear.dex.DexRepo;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import io.althena.spear.model.Dex;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author chongyu.yuan
 * @since 2022/2/24
 */
public class PoolFinder {

    private static final int LATCH_TIMEOUT = 10; // second

    public static List<BasePool> find(final Asset assetA, final Asset assetB, final Set<Dex> whitelist) {
        Set<Dex> dexes = DexRepo.getDexes();
        // filter dexes with whitelist
        if (whitelist != null && whitelist.size() != 0) {
            dexes = dexes.stream().filter(dex -> whitelist.contains(dex)).collect(Collectors.toSet());
        }
        int size = dexes.size();
        if (size == 0) {
            return Collections.emptyList();
        }

        ExecutorService es = null;
        try {
            CountDownLatch latch = new CountDownLatch(size);
            es = Executors.newFixedThreadPool(size);
            List<Future<BasePool>> futures = Lists.newLinkedList();
            for (Dex dex : dexes) {
                final PoolFinderRunner poolFinderRunner = DexRepo.getConfig(dex).getPoolFinderRunner();
                futures.add(es.submit(() -> poolFinderRunner.executeParallel(assetA.clone(), assetB.clone(), latch)));
            }

            latch.await(LATCH_TIMEOUT, TimeUnit.SECONDS);
            return futures.stream().map(f -> {
                try {
                    return f.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(e -> e != null).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (es != null) {
                es.shutdown();
            }
        }
        return Collections.emptyList();
    }

    public static List<BasePool> find(final Asset assetA, final Asset assetB) {
        return find(assetA, assetB, null);
    }
}
