package me.arjenlee.shirolearn.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import me.arjenlee.shirolearn.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "角色管理相关接口", tags = {"角色相关接口"})
@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

    public Result add(){
        Result result = new Result();
        return result;
    }
}
