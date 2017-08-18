package com.test.qqy.checkpermission.aop;

import android.app.Activity;
import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;

import io.reactivex.functions.Consumer;

/**
 * @Description: .
 * @Author: YQQ.yang.
 * @Date: 2017/8/17 14:49.
 */
@Aspect
public class CheckPermissionAsp {
    private static final String TAG = "QQ";

    // FIXME: 2017/8/18 memory leak.
    private static Activity mActivity;

    public static void init(Activity activity) {
        mActivity = activity;
    }

    @Around("execution(@com.test.qqy.checkpermission.aop.CheckPermission * *(..))")
    public void aroundMethodCall(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (null == mActivity) {
            throw new Exception("not bind Activity!");
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Log.e(TAG, "methodSignature----- > " + methodSignature.toString());
        String className = methodSignature.getDeclaringTypeName(); // 获取 hook 的 类 名称.
        Method method = methodSignature.getMethod(); // 获取 hook 的 Method 对象.
        Class<?> clz = Class.forName(className); // 获取 类对象.
        Method[] methods = clz.getDeclaredMethods(); // 获取 类里所有的方法.
        for (Method m : methods) {
            if (m.equals(method)) { // 同一个方法(参数 返回值 相同).
                CheckPermission checkPermission = method.getAnnotation(CheckPermission.class);
                String[] permissions = checkPermission.value(); // 获取 注解参数.
                Log.i(TAG, Arrays.toString(permissions));
                RxPermissions rxPermissions = new RxPermissions(mActivity);
                rxPermissions
                        .request(permissions)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean granted) throws Exception {
                                if (granted) {
                                    // permission is granted!
                                    Log.i(TAG, "permission granted!");
                                    try {
                                        joinPoint.proceed(); // 调用原方法的执行.
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                } else {
                                    // Need to go to the settings
                                    Log.i(TAG, "permission denied!");
                                }
                            }
                        });
                break;
            }
        }
    }
}
