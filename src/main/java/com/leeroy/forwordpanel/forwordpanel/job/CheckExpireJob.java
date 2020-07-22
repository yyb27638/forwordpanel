package com.leeroy.forwordpanel.forwordpanel.job;

import com.leeroy.forwordpanel.forwordpanel.model.User;
import com.leeroy.forwordpanel.forwordpanel.service.UserPortService;
import com.leeroy.forwordpanel.forwordpanel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检查用户账户到期的任务
 */
@EnableScheduling
@Configurable
@Component
public class CheckExpireJob {

    @Autowired
    private UserService userService;

    @Autowired
    private UserPortService userPortService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {
        List<User> expireUserList = userService.findExpireUserList();
        for (User user : expireUserList) {
            if (user.getExpireTime() == null) {
                continue;
            }
            userPortService.disableUserPort(user.getId());
            userService.disableUserById(user.getId());
        }
    }
}
