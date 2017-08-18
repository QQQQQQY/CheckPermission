package com.test.qqy.checkpermission.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: .
 * @Author: YQQ.yang.
 * @Date: 2017/8/17 14:52.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface CheckPermission {
    String[] value();
}
