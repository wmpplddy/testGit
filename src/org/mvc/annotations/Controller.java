package org.mvc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ŀǰ�����þ���һ����ʶ ��ʶ������һ��Controller��<br/>
 * �Լ��ٿ�ܵĲ���Ҫɨ�� δ���Ŀ�ܿ��ܻ�����Ҫ��ע�������ô�<br/>
 * ��ʱ����
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

}
