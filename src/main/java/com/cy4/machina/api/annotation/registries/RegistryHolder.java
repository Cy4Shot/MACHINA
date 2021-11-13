package com.cy4.machina.api.annotation.registries;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.cy4.machina.Machina;

@Documented
@Retention(RUNTIME)
@Target({
	TYPE
})
public @interface RegistryHolder {

	/**
	 * The modid under which the registries will be registered
	 * @return
	 */
	String modid() default Machina.MOD_ID;
}
