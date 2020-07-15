package com.leeroy.forwordpanel.forwordpanel.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leeroy.forwordpanel.forwordpanel.common.WebCurrentData;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.dao.ClashDao;
import com.leeroy.forwordpanel.forwordpanel.model.Clash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClashService {

    @Autowired
    private ClashDao clashDao;


    /**
     * 保存clash
     */
    public ApiResponse save(Clash clash) {
        if (clash.getId() == null) {
            clash.setDeleted(false);
            clash.setDisabled(false);
            clash.setCreateTime(System.currentTimeMillis());
            clashDao.insert(clash);
        } else {
            Clash existClash = clashDao.selectById(clash.getId());
            BeanUtils.copyProperties(clash, existClash);
            existClash.setUpdateTime(System.currentTimeMillis());
            clashDao.updateById(existClash);
        }
        return ApiResponse.ok();
    }


    /**
     * 查询clash列表
     *
     * @return
     */
    public List<Clash> findClashList() {
        Integer userId = WebCurrentData.getUserId();
        LambdaQueryWrapper<Clash> queryWrapper = Wrappers.<Clash>lambdaQuery().eq(Clash::getUserId, userId)
                .eq(Clash::getDeleted, false);
        return clashDao.selectList(queryWrapper);
    }

    /**
     * 删除clash
     */
    public void delClash(String id) {
        Clash userPort = new Clash();
        userPort.setId(id);
        userPort.setDeleted(true);
        clashDao.updateById(userPort);
    }

    /**
     * 查询clash
     * @param id
     * @return
     */
    public Clash findClashById(String id){
        return clashDao.selectById(id);
    }

}
