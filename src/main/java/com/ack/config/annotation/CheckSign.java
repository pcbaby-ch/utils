/**
 * 
 */
package com.ack.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口验签注解(默认同时验证登录，可以设置isCheckLogin=false,关闭验证登录)
 * （！！！！！！！注意：此验签要求control方法的@requestBody 的修饰对象必须为JSONObject类型，否则参与验签的数据会和前端不一致）
 * 
 * @author chenzhao @date Apr 12, 2019
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface CheckSign {

	boolean isCheckLogin() default true;

}
