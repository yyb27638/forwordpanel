package com.leeroy.forwordpanel.forwordpanel.common.annotation;

import java.lang.annotation.*;

/**
 * 免权限校验
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuth {

}
