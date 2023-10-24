package com.yitai.enumeration;

/**
 * ClassName: LogType
 * Package: com.yitai.enumeration
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/8 9:24
 * @Version: 1.0
 */
public enum LogType {
    ADD("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    LOGIN("登录"),
    ASSIGN("分配权限"),
    REGISTER("注册");

    private final String value;

    public String getValue(){
        return value;
    }

    LogType(String value) {
        this.value = value;
    }
}
