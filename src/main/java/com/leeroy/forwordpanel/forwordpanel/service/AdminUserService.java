package com.leeroy.forwordpanel.forwordpanel.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leeroy.forwordpanel.forwordpanel.common.response.PageDataResult;
import com.leeroy.forwordpanel.forwordpanel.common.util.DateUtils;
import com.leeroy.forwordpanel.forwordpanel.common.util.DigestUtils;
import com.leeroy.forwordpanel.forwordpanel.dao.UserDao;
import com.leeroy.forwordpanel.forwordpanel.dto.AdminUserDTO;
import com.leeroy.forwordpanel.forwordpanel.dto.UserSearchDTO;
import com.leeroy.forwordpanel.forwordpanel.model.BaseAdminUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
public class AdminUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserDao userDao;

    public PageDataResult getUserList(UserSearchDTO userSearch, Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        List<AdminUserDTO> baseAdminUsers = userDao.getUsers(userSearch);

        PageHelper.startPage(pageNum, pageSize);

        if (baseAdminUsers.size() != 0) {
            PageInfo<AdminUserDTO> pageInfo = new PageInfo<>(baseAdminUsers);
            pageDataResult.setList(baseAdminUsers);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }


    public Map<String, Object> addUser(BaseAdminUser user) {
        Map<String, Object> data = new HashMap();
        try {
            AdminUserDTO old = userDao.getAdminUserDTOBySysUserName(user.getSysUserName());
            if (old != null) {
                data.put("code", 0);
                data.put("msg", "用户名已存在！");
                logger.error("用户[新增]，结果=用户名已存在！");
                return data;
            }
            String phone = user.getUserPhone();
            if (phone.length() != 11) {
                data.put("code", 0);
                data.put("msg", "手机号位数不对！");
                logger.error("置用户[新增或更新]，结果=手机号位数不对！");
                return data;
            }
            String username = user.getSysUserName();
            if (user.getSysUserPwd() == null) {
                String password = DigestUtils.Md5(username, "123456");
                user.setSysUserPwd(password);
            } else {
                String password = DigestUtils.Md5(username, user.getSysUserPwd());
                user.setSysUserPwd(password);
            }
            user.setRegTime(DateUtils.getCurrentDate());
            user.setUserStatus(1);
            userDao.insert(user);
            data.put("code", 1);
            data.put("msg", "新增成功！");
            logger.info("用户[新增]，结果=新增成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户[新增]异常！", e);
            return data;
        }
        return data;
    }


    public Map<String, Object> updateUser(BaseAdminUser user) {
        Map<String, Object> data = new HashMap();
        Integer id = user.getId();
        BaseAdminUser old = userDao.getUserByUserName(user.getSysUserName(), id);
        if (old != null) {
            data.put("code", 0);
            data.put("msg", "用户名已存在！");
            logger.error("用户[更新]，结果=用户名已存在！");
            return data;
        }
        String username = user.getSysUserName();
        if (user.getSysUserPwd() != null) {
            String password = DigestUtils.Md5(username, user.getSysUserPwd());
            user.setSysUserPwd(password);
        }
        userDao.insert(user);
        data.put("code", 1);
        data.put("msg", "更新成功！");
        logger.info("用户[更新]，结果=更新成功！");
        return data;
    }

    public BaseAdminUser getUserById(Integer id) {
        return userDao.selectById(id);
    }


    public Map<String, Object> delUser(Integer id, Integer status) {
        Map<String, Object> data = new HashMap<>();
        try {
            // 删除用户
            userDao.updateStatus(status, id);
            data.put("code", 1);
            data.put("msg", "删除用户成功");
            logger.info("删除用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除用户异常！", e);
        }
        return data;
    }

    public Map<String, Object> recoverUser(Integer id, Integer status) {
        Map<String, Object> data = new HashMap<>();
        try {
            userDao.updateStatus(status, id);
            data.put("code", 1);
            data.put("msg", "恢复用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("恢复用户异常！", e);
        }
        return data;
    }

    public BaseAdminUser findByUserName(String userName) {
        return userDao.getUserByUserName(userName, null);
    }


    public void updatePwd(String userName, String password) {
        password = DigestUtils.Md5(userName, password);
        userDao.updatePwd(userName, password);
    }
}
