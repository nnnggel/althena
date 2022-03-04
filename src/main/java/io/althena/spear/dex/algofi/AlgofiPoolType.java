package io.althena.spear.dex.algofi;

/**
 * description
 *
 * @author chongyu.yuan
 * @since 2022/3/3
 */
public enum AlgofiPoolType {

    // mainnet
    //    CONSTANT_PRODUCT_25BP_FEE(0,25),
    //    CONSTANT_PRODUCT_75BP_FEE(1,75);

    // TODO CHANGEME testnet
    CONSTANT_PRODUCT_30BP_FEE(0, 30),
    CONSTANT_PRODUCT_100BP_FEE(1, 100);

    private int validatorIndex;

    private int swapFeeBp;

    AlgofiPoolType(int validatorIndex, int swapFeeBp) {
        this.validatorIndex = validatorIndex;
        this.swapFeeBp = swapFeeBp;
    }

    public int getValidatorIndex() {
        return validatorIndex;
    }

    public void setValidatorIndex(int validatorIndex) {
        this.validatorIndex = validatorIndex;
    }

    public int getSwapFeeBp() {
        return swapFeeBp;
    }

    public void setSwapFeeBp(int swapFeeBp) {
        this.swapFeeBp = swapFeeBp;
    }
}
