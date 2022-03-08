/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.dex.algofi;

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
 * AlgofiDexTransactionsBuilder
 *
 * @author chongyu.yuan
 * @since 2022/3/7
 */
public class AlgofiDexTransactionsBuilder implements DexTransactionsBuilder {

    /**
     * # send swap in asset
     * txn0 = get_payment_txn(params, sender, self.address, swap_in_amount, swap_in_asset.asset_id)
     * <p>
     * # swap exact for
     * params.fee = 2000
     * foreign_assets = [self.asset2.asset_id] if swap_in_asset.asset_id == self.asset1.asset_id else ([self.asset1.asset_id] if self.asset1.asset_id != 1 else [])
     * txn1 = ApplicationNoOpTxn(
     * sender=sender,
     * sp=params,
     * index=self.application_id,
     * app_args=[bytes(pool_strings.swap_exact_for, "utf-8"), int_to_bytes(min_amount_to_receive)],
     * foreign_apps=[self.manager_application_id],
     * foreign_assets=foreign_assets,
     * note=int(time.time() * 1000 * 1000).to_bytes(8, 'big')
     * )
     */
    @Override
    public List<Transaction> build(TransactionParametersResponse sp, BasePool pool, AmountInAndOut amountInAndOut,
        Asset assetOut, String userAddress, BigDecimal slippage) {
        final Asset _assetOut = assetOut.clone();
        // algofi use 1 instead of 0 for ALGO
        if (_assetOut.getId().longValue() == 0) {
            _assetOut.setId(1L);
        }

        // find assetIn
        Asset assetIn = pool.getAssetA().equals(_assetOut) ? pool.getAssetB() : pool.getAssetA();

        String poolAddress = pool.getAddress();
        Long appID = pool.getAppID();
        Long managerAppId = ((AlgofiPool) pool).getManageAppID();
        BigInteger amountIn = amountInAndOut.getAmountIn().toBigInteger();
        BigInteger minAmountOut = amountInAndOut.getAmountOut().multiply(BigDecimal.ONE.subtract(slippage))
            .toBigInteger();
        byte[] minAmoutOutByte = Utils.intToBytes(minAmountOut.longValue());

        // PaymentTxn by sender
        Transaction tx1;
        if (assetIn.getId().longValue() == 1L) {
            tx1 = Transaction.PaymentTransactionBuilder().sender(userAddress).receiver(poolAddress).amount(amountIn)
                .suggestedParams(sp).build();
        } else {
            tx1 = Transaction.AssetTransferTransactionBuilder().sender(userAddress).assetReceiver(poolAddress)
                .assetAmount(amountIn).assetIndex(assetIn.getId()).suggestedParams(sp).build();
        }

        List<Long> foreignAsset = _assetOut.getId().longValue() == 1 ? Lists.newArrayList() : Lists.newArrayList(
            _assetOut.equals(pool.getAssetA()) ? pool.getAssetA().getId() : pool.getAssetB().getId());
        // ApplicationNoOpTxn by sender
        Transaction tx2 = Transaction.ApplicationCallTransactionBuilder().sender(userAddress).applicationId(appID)
            .args(Lists.newArrayList("sef".getBytes(), minAmoutOutByte)).foreignApps(Lists.newArrayList(managerAppId))
            .foreignAssets(foreignAsset).suggestedParams(sp).build();
        tx2.setFee(BigInteger.valueOf(2000L));
        return Lists.newArrayList(tx1, tx2);
    }

}
