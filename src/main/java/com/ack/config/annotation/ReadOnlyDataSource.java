package com.ack.config.annotation;


import java.lang.annotation.*;


/**
 * 
 * @author chenzhao @date Apr 9, 2019
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReadOnlyDataSource {
}
