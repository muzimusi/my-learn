package me.arjenlee.shirolearn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.arjenlee.shirolearn.entity.User;
import me.arjenlee.shirolearn.util.Base64;
import me.arjenlee.shirolearn.util.RSA;
import me.arjenlee.shirolearn.util.Result;
import me.arjenlee.shirolearn.util.ResultCode;
import me.arjenlee.shirolearn.vo.LoginRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/login")
@Api(value = "用户登录接口", tags = {"用户登录接口"})
public class LoginController {

    @Value("${shiro.learn.privateKey}")
    String rsAPrivateKey;

    @ApiOperation(value = "登录接口", notes = "登录接口")
    @PostMapping(value = "/login")
    public Result login(@RequestBody LoginRequest loginRequest) throws Exception {
        Result result = new Result();
        Subject subject = SecurityUtils.getSubject();

        String username = getAutho(loginRequest);
        String password = loginRequest.getPassword();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        } catch (AuthenticationException e) {
            result.setCode(ResultCode.UNKNOWN_ERROR.val());
            result.setMsg("用户名或密码错误，请检查！");
            return result;
        }
        Map<String, String> map = new HashMap<>(16);
        User userInfo = (User) SecurityUtils.getSubject().getPrincipal();
        map.put("userName", userInfo.getUsername());
        result.setCode(ResultCode.OK.val());
        result.setMsg("登录成功！");
        result.setData(map);
        return result;
    }

    @ApiOperation(value = "安全登录接口", notes = "安全登录接口")
    @PostMapping(value = "/safelogin")
    public Result safeLogin(@RequestBody LoginRequest loginRequest) throws Exception {
        Result result = new Result();
        Subject subject = SecurityUtils.getSubject();

        String username = getAutho(loginRequest);
        String encRsaPassword = loginRequest.getPassword();
        log.info("用户登录，userName:{}, 密文encRsaPassword:{}", username, encRsaPassword);
        try {
            byte[] passwdBytes = Base64.decode(encRsaPassword);
            if (passwdBytes == null || passwdBytes.length == 0) {
                result.setCode(ResultCode.UNKNOWN_ERROR.val());
                result.setMsg("密码错误，请检查！");
                return result;
            }
            String password = RSA.decryptByPrivateKey(encRsaPassword, rsAPrivateKey);
            //log.info("用户登录，userName:{}, 明文password:{}", mobile, password);

            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        } catch (AuthenticationException e) {
            result.setCode(ResultCode.UNKNOWN_ERROR.val());
            result.setMsg("用户名或密码错误，请检查！");
            return result;
        }
        User userInfo = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, String> map = new HashMap<>(16);
        map.put("userName", userInfo.getUsername());
        result.setCode(ResultCode.OK.val());
        result.setMsg("登录成功！");
        result.setData(map);
        return result;
    }

    private String getAutho(LoginRequest loginRequest) throws Exception {
        if (!StringUtils.isEmpty(loginRequest.getUsername()))
            return loginRequest.getUsername();
        if (!StringUtils.isEmpty(loginRequest.getMobile()))
            return loginRequest.getMobile();
        if (!StringUtils.isEmpty(loginRequest.getEmail()))
            return loginRequest.getEmail();
        log.warn("登录账号为空！");
        throw new Exception("登录账号为空！");
    }
}
