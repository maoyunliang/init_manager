package com.yitai.controller.admin;

import com.yitai.admin.dto.user.UserDTO;
import com.yitai.admin.dto.user.UserPageQueryDTO;
import com.yitai.admin.dto.user.UserRoleDTO;
import com.yitai.admin.vo.UserVO;
import com.yitai.annotation.admin.AutoLog;
import com.yitai.annotation.admin.HasPermit;
import com.yitai.enumeration.LogType;
import com.yitai.exception.ServiceException;
import com.yitai.result.PageResult;
import com.yitai.result.Result;
import com.yitai.service.UserService;
import com.yitai.utils.ExcelUtils;
import com.yitai.utils.RegularUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ClassName: EmployeeController
 * Package: com.yitai.controller.admin
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 9:00
 * @Version: 1.0
 */


@Tag(name = "用户管理相关接口")
@RestController
@RequestMapping("admin/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "新增用户接口")
    @PostMapping("/add")
    @HasPermit(permission = "sys:user:add")
    @AutoLog(operation = "新增用户接口", type = LogType.ADD)
    public Result<String> save(@RequestBody UserDTO userDTO){
        log.info("新增用户:{}", userDTO);
        if(!RegularUtil.checkPhoneNumber(userDTO.getPhone())){
            throw new ServiceException("手机号格式不正确");
        }
        if(userDTO.getIdNumber() != null){
            if(!RegularUtil.checkIdNumber(userDTO.getIdNumber())){
                throw new ServiceException("身份证号格式不正确");
            }
        }
        if(userDTO.getEmail() != null){
            if(!RegularUtil.checkEmail(userDTO.getEmail())){
                throw new ServiceException("邮箱格式不正确");
            }
        }
        userService.save(userDTO);
        return Result.success();
    }

    @Operation(summary = "用户分页查询")
    @PostMapping("/page")
    @HasPermit(permission = "sys:user:list")
    public Result<PageResult> page(@RequestBody UserPageQueryDTO userPageQueryDTO){
        log.info("分页查询:{}", userPageQueryDTO);
        PageResult pageResult = userService.pageQuery(userPageQueryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "账号启用或停用")
    @HasPermit(permission = "sys:user:update")
    @AutoLog(operation = "编辑用户信息", type = LogType.UPDATE)
    @PostMapping("/status/{status}")
    public Result<?> startOrStop(@PathVariable Integer status, @RequestParam("id") Long id){
        log.info("启用或禁用账号：{}， {}", status, id);
        userService.startOrStop(status,id);
        return Result.success();
    }

    @Operation(summary = "删除用户")
    @HasPermit(permission = "sys:user:delete")
    @AutoLog(operation = "删除用户", type = LogType.DELETE)
    @PostMapping("/delete")
    public Result<?> delete(@RequestBody UserDTO userDTO){
        log.info("删除用户");
        userService.delete(userDTO);
        return Result.success();
    }

    @Operation(summary = "编辑用户信息")
    @PostMapping("/update")
    @HasPermit(permission = "sys:user:update")
    @AutoLog(operation = "编辑用户信息", type = LogType.UPDATE)
    public Result<Void> update(@RequestBody UserDTO userDTO){
        log.info("编辑用户信息：{}", userDTO);
        if(userDTO.getPhone() != null) {
            if (!RegularUtil.checkPhoneNumber(userDTO.getPhone())) {
                throw new ServiceException("手机号格式不正确");
            }
        }
        if(userDTO.getIdNumber() != null && !userDTO.getIdNumber().equals("")){
            if(!RegularUtil.checkIdNumber(userDTO.getIdNumber())){
                throw new ServiceException("身份证号格式不正确");
            }
        }
        if(userDTO.getEmail() != null && !userDTO.getEmail().equals("")){
            if(!RegularUtil.checkEmail(userDTO.getEmail())){
                throw new ServiceException("邮箱格式不正确");
            }
        }
        userService.update(userDTO);
        return Result.success();
    }


    @Operation(summary = "分配角色")
    @HasPermit(permission = "sys:user:ass")
    @AutoLog(operation = "分配用户角色", type = LogType.ASSIGN)
    @PostMapping ("/assRole")
    public Result<?> assRole(@RequestBody UserRoleDTO userRoleDTO){
        log.info("分配角色：{}", userRoleDTO);
        userService.assRole(userRoleDTO);
        return Result.success();
    }

    @Operation(summary = "用户导出")
    @HasPermit(permission = "sys:user:export")
    @PostMapping (value = "/export/{tenantId}")
    public void export1(HttpServletResponse response,@PathVariable Long tenantId,
                        @RequestParam(value = "list" ,required = false) List<Long> idList){
        log.info("用户导出");
        List<UserVO> list = userService.list(tenantId, idList);
        ExcelUtils.export(response,"用户导出表", list, UserVO.class);
    }

    @Operation(summary = "用户导入")
    @HasPermit(permission = "sys:user:import")
    @PostMapping("/import/{tenantId}")
    public Result<?> importUser(@PathVariable Long tenantId, @RequestParam("userSheet") MultipartFile multipartFile){
        log.info("用户导入");
        List<UserDTO> userDTOS = ExcelUtils.read(multipartFile, UserDTO.class);
        userService.saveBatch(userDTOS);
        return Result.success(userDTOS);
    }

}
