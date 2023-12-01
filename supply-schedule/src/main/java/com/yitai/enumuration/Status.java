package com.yitai.enumuration;

public enum Status {
    /**
     * 正常
     */
    NORMAL("1"),
    /**
     * 暂停
     */
    PAUSE("-1");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}