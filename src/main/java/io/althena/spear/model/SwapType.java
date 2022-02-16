package io.althena.spear.model;

/**
 * SwapType
 *
 * @author chongyu.yuan
 * @since 2022/2/9
 */
public enum SwapType {
    FIXED_INPUT("fixed-input"),
    FIXED_OUTPUT("fixed-output");

    private String value;

    SwapType(String value) {
        this.value = value;
    }
}
