/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.txn;

import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import io.althena.spear.cal.AmountInAndOut;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import java.math.BigDecimal;
import java.util.List;

/**
 * DexTransactionsBuilder
 *
 * @author chongyu.yuan
 * @since 2022/3/4
 */
public interface DexTransactionsBuilder {

    List<Transaction> build(TransactionParametersResponse sp, BasePool pool, AmountInAndOut amountInAndOut,
        Asset assetOut, String userAddress, BigDecimal slippage);
}
