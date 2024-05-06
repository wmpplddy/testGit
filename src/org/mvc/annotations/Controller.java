package org.mvc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 目前的作用就是一个标识 标识此类是一个Controller类<br/>
 * 以减少框架的不必要扫描 未来的框架可能还会需要此注解其他用处<br/>
 * 暂时这样
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

}
