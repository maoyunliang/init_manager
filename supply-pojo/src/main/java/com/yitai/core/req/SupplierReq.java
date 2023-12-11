package com.yitai.core.req;

import com.yitai.base.BaseBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierReq extends BaseBody {
    private String supplierName;
    private String phone;
    private String contact;
    private String bankNo;
    private String bankAccount;
    private String foodLicenseNo;
    private String businessLicenseNo;
    private String businessScope;
}
