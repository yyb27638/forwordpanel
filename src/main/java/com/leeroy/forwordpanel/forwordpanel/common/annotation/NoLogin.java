package com.leeroy.forwordpanel.forwordpanel.common.annotation;

import java.lang.annotation.*;

/**
 * 免登录校验
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoLogin {

}
