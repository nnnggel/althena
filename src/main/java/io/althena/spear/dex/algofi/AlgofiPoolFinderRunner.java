/*
 * Copyright (C) 2022 chongyu.yuan
 */
package io.althena.spear.dex.algofi;

import com.algorand.algosdk.account.LogicSigAccount;
import com.algorand.algosdk.crypto.Address;
import com.google.common.collect.Lists;
import io.althena.spear.Clients;
import io.althena.spear.Utils;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import io.althena.spear.model.Dex;
import io.althena.spear.pool.PoolFinderRunner;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;

/**
 * description
 *
 * @author chongyu.yuan
 * @since 2022/3/3
 */
public class AlgofiPoolFinderRunner extends PoolFinderRunner {

    // TODO CHANGEME testnet
    private Long managerAppId = 66008735L;

    // mainnet
    //    private Long managerAppId = 605753404L;

    @Override
    public BasePool execute(Asset assetA, Asset assetB) {
        // Primary asset always has lower index.
        if (assetA.getId().compareTo(assetB.getId()) > 0) {
            Asset tmp = assetA;
            assetA = assetB;
            assetB = tmp;
        }

        final Asset _assetA = assetA.clone();
        // algofi use 1 instead of 0 for ALGO
        if (_assetA.getId().longValue() == 0) {
            _assetA.setId(1L);
        }
        final Asset _assetB = assetB;
        Optional<AlgofiPool> poolOpt = Stream.of(AlgofiPoolType.values()).parallel().map(poolType -> {
                byte[] a = generateLogicSig(_assetA.getId(), _assetB.getId(), poolType.getValidatorIndex());
                LogicSigAccount logicSigAccount = new LogicSigAccount(a, null);
                try {
                    Map<String, Object> logicSigLocalState = Utils.getApplicationLocalState(Clients.getAlgodClient(),
                        logicSigAccount.getAddress(), managerAppId);

                    // PoolStatus.UNINITIALIZED
                    if (logicSigLocalState == null || logicSigLocalState.size() == 0) {
                        return null;
                    }

                    Long appID = ((BigInteger) logicSigLocalState.get("p")).longValue();
                    return new AlgofiPool(Dex.ALGOFI, appID, Address.forApplication(appID).encodeAsString(), _assetA,
                        _assetB, poolType, managerAppId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(pool -> pool != null) // FIXME find the one which feeBp is lower
            .sorted(Comparator.comparing(AlgofiPool::getFeeBp)).findFirst();

        AlgofiPool res = null;
        try {
            res = poolOpt.isPresent() ? poolOpt.get().refresh(Clients.getAlgodClient()) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // logicSig
    private List<Integer> POOL_FACTORY_LOGIC_SIG_TEMPLATE_1 = Lists.newArrayList(5, 32, 3);
    private List<Integer> POOL_FACTORY_LOGIC_SIG_TEMPLATE_2 = Lists.newArrayList(1, 34, 35, 12, 68, 49, 16, 129, 6, 18,
        68, 49, 25, 36, 18, 68, 49, 24, 129);
    private List<Integer> POOL_FACTORY_LOGIC_SIG_TEMPLATE_3 = Lists.newArrayList(18, 68, 54, 26, 0, 23, 34, 18, 68, 54,
        26, 1, 23, 35, 18, 68, 54, 26, 2, 23, 129);
    private List<Integer> POOL_FACTORY_LOGIC_SIG_TEMPLATE_4 = Lists.newArrayList(18, 68, 49, 32, 50, 3, 18, 68, 36, 67);

    private byte[] generateLogicSig(Long assetAId, Long assetBId, int validatorIndex) {
        //        concat_array = [
        //        POOL_FACTORY_LOGIC_SIG_TEMPLATE_1,
        //            list(encode_varint(asset1_id)),
        //            list(encode_varint(asset2_id)),
        //            POOL_FACTORY_LOGIC_SIG_TEMPLATE_2,
        //            list(encode_varint(manager_app_id)),
        //            POOL_FACTORY_LOGIC_SIG_TEMPLATE_3,
        //            list(encode_varint(validator_index)),
        //            POOL_FACTORY_LOGIC_SIG_TEMPLATE_4
        //        ]

        List<Integer> concatArray = Lists.newArrayList();
        concatArray.addAll(POOL_FACTORY_LOGIC_SIG_TEMPLATE_1);
        concatArray.addAll(Utils.encodeVarint(assetAId.intValue()));
        concatArray.addAll(Utils.encodeVarint(assetBId.intValue()));
        concatArray.addAll(POOL_FACTORY_LOGIC_SIG_TEMPLATE_2);
        concatArray.addAll(Utils.encodeVarint(managerAppId.intValue()));
        concatArray.addAll(POOL_FACTORY_LOGIC_SIG_TEMPLATE_3);
        concatArray.addAll(Utils.encodeVarint(validatorIndex));
        concatArray.addAll(POOL_FACTORY_LOGIC_SIG_TEMPLATE_4);
        return ArrayUtils.toPrimitive(concatArray.stream().map(entero -> entero.byteValue()).toArray(Byte[]::new));
    }
}
