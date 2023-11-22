package com.yitai.enumeration;

/**
 * ClassName: ShardType
 * Package: com.yitai.enumeration
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/2 17:39
 * @Version: 1.0
 */
public enum ShardType {
    TABLE("分表"),
    ID("字段");

    private final String value;

    public String getValue(){
        return value;
    }

    ShardType(String value) {
        this.value = value;
    }
}
