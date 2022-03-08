/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.dex.pact;

import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import com.google.common.collect.Lists;
import io.althena.spear.Utils;
import io.althena.spear.cal.AmountInAndOut;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import io.althena.spear.txn.DexTransactionsBuilder;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * PactDexTransactionsBuilder
 *
 * @author chongyu.yuan
 * @since 2022/3/4
 */
public class PactDexTransactionsBuilder implements DexTransactionsBuilder {

    @Override
    public List<Transaction> build(TransactionParametersResponse sp, BasePool pool, AmountInAndOut amountInAndOut,
        Asset assetOut, String userAddress, BigDecimal slippage) {
        String poolAddress = pool.getAddress();
        Long appID = pool.getAppID();
        BigInteger amountIn = amountInAndOut.getAmountIn().toBigInteger();
        BigInteger minAmountOut = amountInAndOut.getAmountOut().multiply(BigDecimal.ONE.subtract(slippage))
            .toBigInteger();

        // find assetIn
        Asset assetIn = pool.getAssetA().equals(assetOut) ? pool.getAssetB() : pool.getAssetA();

        // PaymentTxn by sender
        Transaction tx1;
        if (assetIn.getId().longValue() == 0L) {
            tx1 = Transaction.PaymentTransactionBuilder().sender(userAddress).receiver(poolAddress).amount(amountIn)
                .suggestedParams(sp).build();
        } else {
            tx1 = Transaction.AssetTransferTransactionBuilder().sender(userAddress).assetReceiver(poolAddress)
                .assetAmount(amountIn).assetIndex(assetIn.getId()).suggestedParams(sp).build();
        }

        // ApplicationNoOpTxn by sender
        Transaction tx2 = Transaction.ApplicationCallTransactionBuilder().sender(userAddress).applicationId(appID)
            .args(Lists.newArrayList("SWAP".getBytes(), Utils.intToBytes(minAmountOut.longValue())))
            .foreignAssets(Lists.newArrayList(pool.getAssetA().getId(), pool.getAssetB().getId())).suggestedParams(sp)
            .build();
        tx2.setFee(BigInteger.valueOf(2000L));
        return Lists.newArrayList(tx1, tx2);
    }
}
