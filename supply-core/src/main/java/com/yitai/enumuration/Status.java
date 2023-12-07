package com.yitai.enumuration;

public enum Status {
    /**
     * 正常
     */
    NORMAL(1),
    /**
     * 暂停
     */
    PAUSE(-1);

    private final Integer value;

    Status(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}