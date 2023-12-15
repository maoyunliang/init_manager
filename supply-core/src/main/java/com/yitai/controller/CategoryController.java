package com.yitai.controller;

import cn.hutool.core.bean.BeanUtil;
import com.yitai.annotation.admin.HasPermit;
import com.yitai.core.dto.CategoryDTO;
import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Category;
import com.yitai.core.req.CategoryReq;
import com.yitai.core.req.CommodityReq;
import com.yitai.core.req.CommoditySaveReq;
import com.yitai.result.Result;
import com.yitai.service.CategoryService;
import com.yitai.utils.TreeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result<List<CategoryDTO>> list(@RequestBody CategoryReq req) {
        List<Category> list = categoryService.list(req);
        List<CategoryDTO> categoryDTOS = BeanUtil.copyToList(list, CategoryDTO.class);
        ArrayList<CategoryDTO> categories = TreeUtil.buildTree(categoryDTOS, CategoryDTO::getPid, CategoryDTO::getSortNo);
        return Result.success(categories);
    }

}
