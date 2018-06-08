package com.yaphets.storage.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Deprecated
@Retention(RUNTIME)
@Target(FIELD)
public @interface ManyToOne {
	/**
	 * 	当前表外键列名
	 * @return
	 * 	外键列名
	 */
	String name();
	
	/**
	 * 	引用表的主键列名
	 * @return
	 * 	引用表的主键名
	 */
	String mappedBy();
}
