package com.yitai.enumeration;

/**
 * ClassName: SizeEnum
 * Package: com.yitai.enumeration
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/7 11:34
 * @Version: 1.0
 */
public enum SizeEnum {
    /**
     * 1KB = 1024B
     */
    KB(1024),

    /**
     * 1MB = 1024KB
     */
    MB(KB.size * 1024),

    /**
     * 1GB = 1024MB
     */
    GB(MB.size * 1024);

    /**
     * 1(K/M/G)B = ? B
     */
    private final long size;
    public long getSize(){
        return size;
    }
    SizeEnum(long size){
        this.size = size;
    }
}
