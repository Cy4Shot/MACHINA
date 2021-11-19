package com.cy4.machina.api.annotation.registries;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
/**
 * Registers the {@link Fluid} that is represented by the field that has this
 * annotation. For the fluid to be registered the class in which the field is
 * has to be annotated with {@link RegistryHolder}
 *
 * @author matyrobbrt
 *
 */
public @interface RegisterFluid {

	/**
	 * The registry name of the fluid (the modid is specified by the
	 * {@link RegistryHolder} on the class the field is in)
	 *
	 * @return
	 */
	String value();

}