package com.docker.annotation;

import java.lang.annotation.*;

/**
 * 切面注解
 * Created by CHEN on 2019/11/26.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Login {
}
