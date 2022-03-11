package io.althena.spear.model;

/**
 * Dex
 *
 * @author chongyu.yuan
 * @since 2022/2/12
 */
public enum Dex {
    FAKE(9999),
    PACT(1),
    ALGOFI(2);

    Dex(int order) {
        this.order = order;
    }

    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
