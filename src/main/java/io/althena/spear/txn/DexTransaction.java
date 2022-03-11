/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.txn;

import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;

/**
 * description
 *
 * @author chongyu.yuan
 * @since 2022/3/4
 */
public class DexTransaction {

    private Transaction transaction;

    private SignedTransaction signedTransaction;

    private boolean signed;

    public DexTransaction(Transaction transaction) {
        this.transaction = transaction;
        this.signed = false;
    }

    public DexTransaction(SignedTransaction signedTransaction) {
        this.signedTransaction = signedTransaction;
        this.signed = true;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public SignedTransaction getSignedTransaction() {
        return signedTransaction;
    }

    public void setSignedTransaction(SignedTransaction signedTransaction) {
        this.signedTransaction = signedTransaction;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }
}
