/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.LogicsigSignature;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import io.althena.spear.cal.AmountInAndOut;
import io.althena.spear.cal.PathFinder;
import io.althena.spear.cal.PoolCalculator;
import io.althena.spear.cal.PoolCalculatorBuilder;
import io.althena.spear.dex.DexConfiguration;
import io.althena.spear.dex.DexRepo;
import io.althena.spear.dex.algofi.AlgofiDexTransactionsBuilder;
import io.althena.spear.dex.algofi.AlgofiPoolCalculator;
import io.althena.spear.dex.algofi.AlgofiPoolFinderRunner;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import io.althena.spear.model.Dex;
import io.althena.spear.pool.PoolFinder;
import io.althena.spear.txn.DexTransactionsAssembler;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
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
        test();
    }

    private static void test() throws Exception {
        // TODO CHANGEME testnet
        // Mock request parameter
        // user acount/address
        Account user = new Account("<-- MNEMONIC_FOR_FUNDING_ACOUNT -->");
        String userAddress = user.getAddress().encodeAsString();
        // assets
        Asset ASSET_IN = new Asset(0L).fetch(Clients.getAlgodClient());
        Asset ASSET_OUT = new Asset(21582668L).fetch(Clients.getAlgodClient());
        // amountIn
        BigInteger amountIn = BigInteger.valueOf(100_000L);
        // slippage, must <= 0.1
        BigDecimal slippage = BigDecimal.valueOf(0.01);

        // Step: init
        // register dexes
        //        DexRepo.register(Dex.FAKE,
        //            new DexConfiguration(30, new FakePoolFinderRunner(), FeeFromOutPoolCalculator.class));
        //        DexRepo.register(Dex.PACT, new DexConfiguration(30, new PactPoolFinderRunner(), FeeFromOutPoolCalculator.class,
        //            new PactDexTransactionsBuilder()));
        DexRepo.register(Dex.ALGOFI, new DexConfiguration(-1, new AlgofiPoolFinderRunner(), AlgofiPoolCalculator.class,
            new AlgofiDexTransactionsBuilder()));

        // Step: find path
        // find available pools
        List<BasePool> pools = PoolFinder.find(ASSET_IN, ASSET_OUT);
        if (pools.size() == 0) {
            throw new RuntimeException("pools size is zero.");
        }

        // init pathfinder
        // TODO determine copis(power of 10), base_decimal(10=1,100=2,1000=3...) by estimating amountIn value
        PathFinder pathFinder = new PathFinder(100);
        int decimal = 2 + ASSET_OUT.getDecimals().intValue();

        // build calculated pool
        List<PoolCalculator> poolCalculators = pools.stream().map(pool -> PoolCalculatorBuilder.build(pool, decimal))
            .collect(Collectors.toList());
        System.out.println("poolCalculators: " + poolCalculators);

        // calculate and group
        Map<BasePool, AmountInAndOut> calculatedMap = pathFinder.findByFixedIn(poolCalculators, ASSET_IN, amountIn);
        System.out.println("calculatedMap: ");
        calculatedMap.entrySet().stream().forEach(
            e -> System.out.println(e.getKey().getDex() + ":" + e.getKey().getAppID() + " -> " + e.getValue()));

        // Step: build txns
        List<Transaction> txns = DexTransactionsAssembler.assemble(calculatedMap, ASSET_OUT, userAddress, slippage);
        System.out.println("txns: ");
        txns.stream().forEach(txn -> {
            try {
                System.out.println(new ObjectMapper().writeValueAsString(txn));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        // TODO Setp: build quote

        // FIXME test send
        final Map<String, Object> keyMap = Maps.newHashMap();
        calculatedMap.entrySet().stream().forEach(e -> {
            BasePool basePool = e.getKey();
            LogicsigSignature logicsigSignature = basePool.getLogicsigSignature();
            if (logicsigSignature != null) {
                keyMap.put(basePool.getAddress(), logicsigSignature);
            }
        });
        keyMap.put(user.getAddress().encodeAsString(), user);
        _signAndSend(keyMap, txns);
    }

    private static void _signAndSend(Map<String, Object> keyMap, List<Transaction> txns) throws Exception {
        List<Object> signAccounts = txns.stream().map(txn -> {
            try {
                return keyMap.get(txn.sender.encodeAsString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        String txId = Utils.sendTransaction(Clients.getAlgodClient(), Utils.signTransactions(signAccounts, txns));
        PendingTransactionResponse pTrx = Utils.waitForConfirmation(Clients.getAlgodClient(), txId, 10);
        System.out.println("Transaction " + txId + " confirmed in round " + pTrx.confirmedRound);
    }
}
