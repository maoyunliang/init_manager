package com.yitai.controller;

import com.yitai.annotation.admin.HasPermit;
import com.yitai.core.dto.TenderDTO;
import com.yitai.core.vo.TenderVO;
import com.yitai.result.PageResult;
import com.yitai.result.Result;
import com.yitai.service.TenderService;
import com.yitai.utils.ExcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ClassName: TenderController
 * Package: com.yitai.controller
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/12 16:15
 * @Version: 1.0
 */
@Tag(name = "招采管理接口")
@RequestMapping("admin/tender")
@RestController
@Slf4j
public class TenderController {

    @Autowired
    TenderService tenderService;

    // 招标询价列表(租户级别)
    @PostMapping("/list")
    @Operation(summary = "招标询价列表")
    @HasPermit(permission = "tender:inquiry:list")
    public Result<PageResult> list(@RequestBody TenderDTO tenderDTO){
        PageResult pageResult = tenderService.page(tenderDTO);
        return Result.success(pageResult);
    }


    // 招标询价导出(租户级别)
    @PostMapping("/export/{tenantId}")
    @Operation(summary = "招标询价导出")
    @HasPermit(permission = "tender:inquiry:export")
    public void export(@PathVariable Long tenantId,
                       @RequestParam(value = "idList" ,required = false)  List<Long> idList,
                       @RequestParam(value = "fieldList")  List<String> fieldList , HttpServletResponse response){
        List<TenderVO> list = tenderService.list(tenantId, idList);
        String fileName = "招标清单"+ LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        if (list != null && list.size() == 1){
            fileName = list.get(0).getTenderName()+"-".concat(fileName);
        }
        ExcelUtils.export(response, fileName, list, TenderVO.class, fieldList);
    }

    @PostMapping("/save")
    @Operation(summary = "新建招标")
    @HasPermit(permission = "tender:inquiry:add")
    public Result<?> save(@RequestBody TenderDTO tenderDTO){
        tenderService.save(tenderDTO);
        return Result.success();
    }

}
