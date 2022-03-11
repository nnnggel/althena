/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.txn;

import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import com.google.common.collect.Lists;
import io.althena.spear.Clients;
import io.althena.spear.Utils;
import io.althena.spear.cal.AmountInAndOut;
import io.althena.spear.dex.DexRepo;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author chongyu.yuan
 * @since 2022/3/7
 */
public class DexTransactionsAssembler {

    // TODO CHANGEME testnet
    private static Long thresholdAppID = 0L;

    public static List<Transaction> assemble(Map<BasePool, AmountInAndOut> calculatedMap, Asset assetOut,
        String userAddress, BigDecimal slippage) throws Exception {

        TransactionParametersResponse sp = Utils.getSuggestedParams(Clients.getAlgodClient());

        Map<BasePool, List<Transaction>> txnMap = _build(calculatedMap, assetOut, userAddress, slippage, sp);
        System.out.println("txnMap: " + txnMap);

        // min amount out
        BigDecimal amountOutTotal = calculatedMap.values().stream().map(aio -> aio.getAmountOut())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigInteger minAmountOutTotal = amountOutTotal.multiply(BigDecimal.ONE.subtract(slippage)).toBigInteger();
        // minAmountOutTotal should deduct fee if assetOut is ALGO.
        if (assetOut.getId().longValue() == 0) {
            BigInteger totalFee = txnMap.values().stream().flatMap(txns -> txns.stream()).map(txn -> txn.fee)
                .reduce(BigInteger.ZERO, BigInteger::add);
            minAmountOutTotal = minAmountOutTotal.subtract(totalFee).subtract(BigInteger.valueOf(1000L));
        }

        final List<Transaction> res = Lists.newArrayList();
        if (minAmountOutTotal.compareTo(BigInteger.ZERO) <= 0) {
            txnMap.values().forEach(txns -> res.addAll(txns));
        } else {
            List<Transaction> thresholdTnx = _threshold(assetOut, userAddress, minAmountOutTotal, sp);
            res.add(thresholdTnx.get(0));
            txnMap.values().forEach(txns -> res.addAll(txns));
            res.add(thresholdTnx.get(1));
        }

        return res;
    }

    private static List<Transaction> _threshold(Asset assetOut, String userAddress, BigInteger minAmountOutTotal,
        TransactionParametersResponse sp) {

        // beginTxn
        Transaction beginTxn = Transaction.ApplicationCallTransactionBuilder().sender(userAddress)
            .applicationId(thresholdAppID).args(
                Lists.newArrayList("begin".getBytes(), BigInteger.valueOf(assetOut.getId()).toByteArray(),
                    minAmountOutTotal.toByteArray())).foreignAssets(Lists.newArrayList(assetOut.getId()))
            .suggestedParams(sp).build();

        // finishTxn
        Transaction finishTxn = Transaction.ApplicationCallTransactionBuilder().sender(userAddress)
            .applicationId(thresholdAppID).args(Lists.newArrayList("finish".getBytes()))
            .foreignAssets(Lists.newArrayList(assetOut.getId())).suggestedParams(sp).build();

        return Lists.newArrayList(beginTxn, finishTxn);
    }

    //    private static List<DexTransaction> _sign(Map<BasePool, List<Transaction>> txnMap) {
    //        return txnMap.entrySet().stream().map(e -> {
    //            BasePool basePool = e.getKey();
    //            LogicsigSignature logicsigSignature = basePool.getLogicsigSignature();
    //            String poolAddress = basePool.getAddress();
    //
    //            return e.getValue().stream().map(txn -> {
    //                DexTransaction dexTransaction = null;
    //                try {
    //                    String sender = txn.sender.encodeAsString();
    //                    if (sender.equals(poolAddress)) {
    //                        dexTransaction = new DexTransaction(Account.signLogicsigTransaction(logicsigSignature, txn));
    //                    } else {
    //                        dexTransaction = new DexTransaction(txn);
    //                    }
    //                } catch (Exception ex) {
    //                    ex.printStackTrace();
    //                }
    //                return dexTransaction;
    //            }).collect(Collectors.toList());
    //        }).flatMap(txns -> txns.stream()).collect(Collectors.toList());
    //
    //    }

    private static Map<BasePool, List<Transaction>> _build(Map<BasePool, AmountInAndOut> res, Asset assetOut,
        String userAddress, BigDecimal slippage, TransactionParametersResponse sp) {
        // TODO test
        final BigDecimal maxSlippage = slippage.multiply(BigDecimal.valueOf(5l));

        // txnMap
        return res.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> {
            BasePool basePool = e.getKey();
            AmountInAndOut amountInAndOut = e.getValue();
            return DexRepo.getConfig(basePool.getDex()).getDexTransactionsBuilder()
                .build(sp, basePool, amountInAndOut, assetOut, userAddress, maxSlippage);
        }));
    }
}
