package com.yitai.controller;

import com.yitai.annotation.admin.HasPermit;
import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Category;
import com.yitai.core.req.CommodityReq;
import com.yitai.core.req.CommoditySaveReq;
import com.yitai.result.Result;
import com.yitai.service.CategoryService;
import com.yitai.service.CommodityService;
import com.yitai.utils.ExcelUtils;
import com.yitai.utils.TreeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "类目管理接口")
@RequestMapping("admin/category")
@RestController
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // 定时任务接口(租户级别)
    @PostMapping("/list")
    @Operation(summary = "类目列表")
//    @HasPermit(permission = "core:category:list")
    public Result<List<Category>> list(@RequestBody Category req) {
        List<Category> list = categoryService.list(req);
        ArrayList<Category> categories = TreeUtil.buildTree(list, Category::getPid, Category::getSortNo);
        return Result.success(categories);
    }

}
