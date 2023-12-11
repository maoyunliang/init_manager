package com.yitai.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * ClassName: Supplier
 * Package: com.yitai.core.entity
 * Description:
 *
 * @Author: yujunchun
 * @Create: 2023/12/7 17:29
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supplier {
    private Long id;
    private String supplierName;
    private String phone;
    private String contact;
    private String bankNo;
    private String bankAccount;
    private String foodLicenseNo;
    private Date foodLicenseStartDate;
    private Date foodLicenseEndDate;
    private String businessLicenseNo;
    private Date businessLicenseStartDate;
    private Date businessLicenseEndDate;
    private String businessScope;
    private String address;
    private String remark;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
