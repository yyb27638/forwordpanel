package com.leeroy.forwordpanel.forwordpanel.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leeroy.forwordpanel.forwordpanel.common.WebCurrentData;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.common.response.PageDataResult;
import com.leeroy.forwordpanel.forwordpanel.common.util.DigestUtils;
import com.leeroy.forwordpanel.forwordpanel.dao.UserDao;
import com.leeroy.forwordpanel.forwordpanel.dto.UserSearchDTO;
import com.leeroy.forwordpanel.forwordpanel.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: AdminUserServiceImpl
 * @Description:
 * @author: leeroy
 * @version: 1.0
 * @date: 2020/11/21 11:04
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Value("${panel.default-password}")
    private String defaultPassword;

    public PageDataResult getUserList(UserSearchDTO userSearch, Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        Integer userType = WebCurrentData.getUser().getUserType();
        if(userType>0){
            userSearch.setId(WebCurrentData.getUserId());
        }
        List<User> baseAdminUsers = userDao.getUsers(userSearch);
        PageHelper.startPage(pageNum, pageSize);
        if (baseAdminUsers.size() != 0) {
            PageInfo<User> pageInfo = new PageInfo<>(baseAdminUsers);
            pageDataResult.setList(baseAdminUsers);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }


    public ApiResponse addUser(User user) {
        Map<String, Object> data = new HashMap();
        try {
            if(WebCurrentData.getUser().getUserType()>0){
                return ApiResponse.error("403", "您没有权限新增用户");
            }
            LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername())
                    .eq(User::getDeleted, false);
            User old = userDao.selectOne(queryWrapper);
            if (old != null) {
                return ApiResponse.error("400", "用户名已存在");
            }
            String username = user.getUsername();
            if (user.getPassword() == null) {
                String password = DigestUtils.Md5(username, defaultPassword);
                user.setPassword(password);
            } else {
                String password = DigestUtils.Md5(username, user.getPassword());
                user.setPassword(password);
            }
            user.setRegTime(System.currentTimeMillis());
            user.setDisabled(false);
            user.setDeleted(false);
            userDao.insert(user);
        } catch (Exception e) {
            log.error("新增用户失败", e);
            return ApiResponse.error("400", "用户新增异常:" + e.getMessage());
        }
        return ApiResponse.ok();
    }

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    public ApiResponse updateUser(User user) {
        Integer id = user.getId();
        if(WebCurrentData.getUser().getUserType()>0&&!WebCurrentData.getUserId().equals(id)){
            return ApiResponse.error("403", "您没有权限修改其他用户");
        }
        User old = userDao.getUserByUserName(user.getUsername(), id);
        if (old != null) {
            log.info("用户[更新]，结果=用户名已存在！");
            return ApiResponse.error("400", "用户名已存在");
        }
        String username = user.getUsername();
        if (user.getPassword() != null) {
            String password = DigestUtils.Md5(username, user.getPassword());
            user.setPassword(password);
        }
        userDao.insert(user);
        return ApiResponse.ok();
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    public User getUserById(Integer id) {
        return userDao.selectById(id);
    }


    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    public ApiResponse delUser(Integer id) {
        if(WebCurrentData.getUser().getUserType()>0&&!WebCurrentData.getUserId().equals(id)){
            return ApiResponse.error("403", "您没有权限修改其他用户");
        }
        if (id.equals(WebCurrentData.getUserId())) {
            return ApiResponse.error("400", "您不能删除自己");
        }
        userDao.deleteUser(id);
        return ApiResponse.ok();
    }

    /**
     * 禁用用户
     *
     * @param id
     * @return
     */
    public ApiResponse disableUser(Integer id) {
        if(WebCurrentData.getUser().getUserType()>0&&!WebCurrentData.getUserId().equals(id)){
            return ApiResponse.error("403", "您没有权限修改其他用户");
        }
        if (id.equals(WebCurrentData.getUserId())) {
            return ApiResponse.error("400", "您不能禁用自己");
        }
        userDao.updateDisable(true, id);
        return ApiResponse.ok();
    }

    /**
     * 启用用户
     *
     * @param id
     * @return
     */
    public ApiResponse enableUser(Integer id) {
        if(WebCurrentData.getUser().getUserType()>0&&!WebCurrentData.getUserId().equals(id)){
            return ApiResponse.error("403", "您没有权限修改其他用户");
        }
        if (id.equals(WebCurrentData.getUserId())) {
            return ApiResponse.error("400", "您不能启用自己");
        }
        userDao.updateDisable(false, id);
        return ApiResponse.ok();
    }

    /**
     * 根据用户名查询用户
     *
     * @param userName
     * @return
     */
    public User findByUserName(String userName) {
        return userDao.getUserByUserName(userName, null);
    }


    /**
     * 更新密码
     *
     * @param userName
     * @param password
     */
    public void updatePwd(String userName, String password) {
        password = DigestUtils.Md5(userName, password);
        userDao.updatePwd(userName, password);
    }
}
