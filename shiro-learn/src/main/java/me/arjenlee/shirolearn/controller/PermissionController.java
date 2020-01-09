package me.arjenlee.shirolearn.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "权限管理相关接口", tags = {"权限相关接口"})
@RestController
@RequestMapping("/permission")
@Slf4j
public class PermissionController {
}
