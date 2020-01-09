package me.arjenlee.shirolearn.shiro;

import lombok.extern.slf4j.Slf4j;
import me.arjenlee.shirolearn.entity.Permission;
import me.arjenlee.shirolearn.entity.Role;
import me.arjenlee.shirolearn.entity.User;
import me.arjenlee.shirolearn.service.UserService;
import me.arjenlee.shirolearn.util.Status;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    /**
     * 重写获取AuthorizationCacheKey方法，修改为用户电话号码
     *
     * @param principals
     * @return
     */
    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {

        Object o = principals.getPrimaryPrincipal();
        if (o instanceof User) {
            User userInfo = (User) o;
            if (null != userInfo) {
                // 权限信息写入redis时的key
                return "LANXIN:PERMISSION:" + userInfo.getUsername();
            }
        }

        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户的输入的账号
        String username = (String) token.getPrincipal();

        log.info(username + " doGetAuthenticationInfo!");

        // TODO 认证
        User userInfo = userService.getUserInfoByUserName(username);
        if (userInfo == null) throw new UnknownAccountException();
        if (Status.ENABLED.getValue() != userInfo.getStatus()) {
            throw new LockedAccountException(); // 帐号锁定
        }
        log.info("userInfo: {}", userInfo);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户
                userInfo.getPassword(), //密码
                ByteSource.Util.bytes(username),
                getName()  //realm name
        );
        // 当验证都通过后，把用户信息放在session里
        Session session = SecurityUtils.getSubject().getSession();

        session.setAttribute("userSession", userInfo);
        session.setAttribute("userSessionId", userInfo.getId());
        return authenticationInfo;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        User userInfo = (User) principals.getPrimaryPrincipal();
        if (userInfo != null) {
            //重新获取用户信息，防止被修改了内存没有更新
            userInfo = userService.findById(userInfo.getId());
        }
        if (null == userInfo) {
            return null;
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : userInfo.getRoleList()) {
            authorizationInfo.addRole(role.getName());
            for (Permission p : role.getPermissionList()) {
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }

}
