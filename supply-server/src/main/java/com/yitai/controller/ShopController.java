package com.yitai.controller;

import com.yitai.constant.MessageConstant;
import com.yitai.exception.ServiceException;
import com.yitai.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ShopController
 * Package: com.yitai.controller.user
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/27 14:09
 * @Version: 1.0
 */
@Tag(name = "客户端相关接口")
@RequestMapping("/shop")
@RestController
@Slf4j
public class ShopController {
    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @Operation(summary = "获取店铺的营业状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer)redisTemplate.opsForValue().get(KEY);
        if(status==null){
            throw new ServiceException(MessageConstant.SHOP_NOT_FIND);
        }
        log.info("获取到店铺的营业状态为：{}", status == 1 ? "营业中": "打烊中");
        return Result.success(status);
    }
}
