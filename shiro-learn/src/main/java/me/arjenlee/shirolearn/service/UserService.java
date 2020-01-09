package me.arjenlee.shirolearn.service;

import lombok.extern.slf4j.Slf4j;
import me.arjenlee.shirolearn.dao.UserMapper;
import me.arjenlee.shirolearn.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Resource
    private UserMapper userInfoMapper;

    public User getUserInfoByUserName(String username) {
        log.info("getUserInfoByUserName, username: {}", username);
        Example query = new Example(User.class);
        query.createCriteria()
                .andGreaterThanOrEqualTo("name", username);
        List<User> userInfoList = this.userInfoMapper.selectByExample(query);
        log.info("getUserInfoByUserName, result: {}", userInfoList);
        if (!CollectionUtils.isEmpty(userInfoList))
            return userInfoList.get(0);
        return null;
    }

    public User findById(Integer id) {
        log.info("findById: {}", id);
        User userQuery = User.builder().id(id).build();
        User userInfo = this.userInfoMapper.selectByPrimaryKey(userQuery);
        return userInfo;
    }

    public void saveUserInfo(User user) {
        log.info("saveUserInfo: {}", user);
        this.userInfoMapper.insert(user);
    }

    public List<User> selectByOrgId(String orgid) {
        Example query = new Example(User.class);
        query.createCriteria()
                .andEqualTo("orgid",orgid);
        List<User> userList = userInfoMapper.selectByExample(query);
        return userList;
    }
}
