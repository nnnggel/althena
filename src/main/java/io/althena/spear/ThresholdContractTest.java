/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import com.google.common.collect.Lists;
import java.math.BigInteger;
import java.util.List;

/**
 * Threshold contract test
 *
 * @author chongyu.yuan
 * @since 2022/2/10
 */
public class ThresholdContractTest {

    // TODO CHANGEME local env
    private final String BASE_TEAL_PATH = "<-- PATH_FOR_TEAL_CONTRACT_FILES -->";

    // TODO CHANGEME testnet
    private final String ALGOD_API_ADDR = "https://testnet-algorand.api.purestake.io/ps2";
    private final Integer ALGOD_PORT = 443;
    private final String ALGOD_API_TOKEN_KEY = "X-API-Key";
    private final String ALGOD_API_TOKEN = "<-- API_TOKEN -->";
    private final Account FUNDING_ACCOUNT = new Account("<-- MNEMONIC_FOR_FUNDING_ACOUNT -->");

    private AlgodClient client;

    public ThresholdContractTest() throws Exception {
        client = new AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN, ALGOD_API_TOKEN_KEY);
    }

    public void createThresholdApp() throws Exception {
        System.out.println("=== create threshold start");

        byte[] programApproval = Utils.compileFile(client, BASE_TEAL_PATH + "threshold_approval.teal");
        byte[] clearApproval = Utils.compileFile(client, BASE_TEAL_PATH + "threshold_clear_state.teal");

        TransactionParametersResponse sp = Utils.getSuggestedParams(client);

        Transaction txn = Transaction.ApplicationCreateTransactionBuilder().sender(FUNDING_ACCOUNT.getAddress())
            .approvalProgram(new TEALProgram(programApproval)).clearStateProgram(new TEALProgram(clearApproval))
            .globalStateSchema(new StateSchema(2, 1)).localStateSchema(new StateSchema(0, 0)).suggestedParams(sp)
            .build();

        String txId = Utils.sendTransaction(client, Utils.signTransaction(FUNDING_ACCOUNT, txn));
        PendingTransactionResponse pTrx = Utils.waitForConfirmation(client, txId, 10);
        Long appID = pTrx.applicationIndex;

        System.out.println("Transaction " + txId + " confirmed in round " + pTrx.confirmedRound);
        System.out.println(
            "The auction app ID is " + appID + ", and the escrow account is " + Address.forApplication(appID));

        System.out.println(Utils.getApplicationInfo(client, appID));

        System.out.println("=== create threshold finish");
    }

    private void begin(Long appID, Account test1, Long swapAssetId, Long minSwapOut) throws Exception {
        System.out.println("=== [begin] start");

        TransactionParametersResponse sp = Utils.getSuggestedParams(client);
        List<byte[]> args = Lists.newArrayList("begin".getBytes(), BigInteger.valueOf(swapAssetId).toByteArray(),
            BigInteger.valueOf(minSwapOut).toByteArray());
        Transaction beginTxn = Transaction.ApplicationCallTransactionBuilder().sender(test1.getAddress())
            .applicationId(appID).args(args).foreignAssets(Lists.newArrayList(swapAssetId)).suggestedParams(sp).build();

        String txId = Utils.sendTransaction(client, (Utils.signTransaction(test1, beginTxn)));
        // Wait for transaction confirmation
        PendingTransactionResponse pTrx = Utils.waitForConfirmation(client, txId, 10);
        System.out.println("Transaction " + txId + " confirmed in round " + pTrx.confirmedRound);

        System.out.println("=== [begin] finish");
    }

    private void finish(Long appID, Account test1, Long swapAssetId) throws Exception {
        System.out.println("=== [finish] start");

        TransactionParametersResponse sp = Utils.getSuggestedParams(client);
        List<byte[]> args = Lists.newArrayList("finish".getBytes());
        Transaction finishTxn = Transaction.ApplicationCallTransactionBuilder().sender(test1.getAddress())
            .applicationId(appID).args(args).foreignAssets(Lists.newArrayList(swapAssetId)).suggestedParams(sp).build();

        String txId = Utils.sendTransaction(client, (Utils.signTransaction(test1, finishTxn)));
        // Wait for transaction confirmation
        PendingTransactionResponse pTrx = Utils.waitForConfirmation(client, txId, 10);
        System.out.println("Transaction " + txId + " confirmed in round " + pTrx.confirmedRound);

        System.out.println("=== [finish] finish");
    }

    private void beginAndFinish(Long appID, Account test1, Long swapAssetId, Long minSwapOut) throws Exception {
        System.out.println("=== [beginAndFinish] start");

        TransactionParametersResponse sp = Utils.getSuggestedParams(client);
        Transaction beginTxn = Transaction.ApplicationCallTransactionBuilder().sender(test1.getAddress())
            .applicationId(appID).args(
                Lists.newArrayList("begin".getBytes(), BigInteger.valueOf(swapAssetId).toByteArray(),
                    BigInteger.valueOf(minSwapOut).toByteArray())).foreignAssets(Lists.newArrayList(swapAssetId))
            .suggestedParams(sp).build();

        Transaction finishTxn = Transaction.ApplicationCallTransactionBuilder().sender(test1.getAddress())
            .applicationId(appID).args(Lists.newArrayList("finish".getBytes()))
            .foreignAssets(Lists.newArrayList(swapAssetId)).suggestedParams(sp).build();

        List<Transaction> txns = Lists.newArrayList(beginTxn, finishTxn);
        List<Object> signAccounts = Lists.newArrayList(test1, test1);

        String txId = Utils.sendTransaction(client, (Utils.signTransactions(signAccounts, txns)));
        // Wait for transaction confirmation
        PendingTransactionResponse pTrx = Utils.waitForConfirmation(client, txId, 10);

        System.out.println("Transaction " + txId + " confirmed in round " + pTrx.confirmedRound);

        System.out.println("=== [beginAndFinish] finish");
    }

    public static void main(String[] args) throws Exception {
        ThresholdContractTest thresholdContractTest = new ThresholdContractTest();
        //        thresholdContractTest.createThresholdApp(); // 76590607L
        Account testAcount = new Account("<-- MNEMONIC_FOR_TEST_ACOUNT -->");
        thresholdContractTest.beginAndFinish(76590607L, testAcount, 21582668L, 10L);
    }

}
