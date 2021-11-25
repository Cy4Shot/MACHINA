/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.annotation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that an element should only be used in a development environment
 */
@Documented
@Retention(RUNTIME)
@Target({
		TYPE, FIELD, METHOD, CONSTRUCTOR, LOCAL_VARIABLE, PACKAGE
})
public @interface DevelopmentOnly {

	/**
	 * If true, the element should <b>not be used</b> in a development environment
	 * 
	 * @return
	 */
	boolean inversed() default false;

}
