package com.leeroy.forwordpanel.forwordpanel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.leeroy.forwordpanel.forwordpanel.dao")
public class ForwordpanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForwordpanelApplication.class, args);
    }

}
