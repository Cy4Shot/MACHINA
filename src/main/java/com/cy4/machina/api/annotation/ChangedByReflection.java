/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.annotation.Syntax;

/**
 * Indicates that a constant which has the initial value of <code>null</code>
 * will have its value changed using reflection at some point.
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface ChangedByReflection {

	/**
	 * Indicates when the value of the field will be changed
	 * 
	 * @return
	 */
	@Syntax("Java")
	String when() default "";

}
