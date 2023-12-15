package com.yitai.core.dto;

import com.yitai.annotation.excel.ExcelExport;
import com.yitai.annotation.excel.ExcelSheet;
import com.yitai.base.BaseBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ExcelSheet
public class CategoryDTO extends BaseBody {
    private Long id;
    private String categoryName;
    private Long pid;
    private String sortNo;
    private String remark;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
    private Integer isDel;

    private List<CategoryDTO> children;
}
