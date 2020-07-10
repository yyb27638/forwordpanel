package com.leeroy.forwordpanel.forwordpanel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.leeroy.forwordpanel.forwordpanel.model")
public class ForwordpanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForwordpanelApplication.class, args);
    }

}
