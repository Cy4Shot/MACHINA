/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

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
 * Registers the {@link ParticleType} that is represented by the field that has
 * this annotation. For the ParticleType to be registered the class in which the
 * field is has to be annotated with {@link RegistryHolder}
 *
 * @author matyrobbrt
 *
 */
public @interface RegisterParticleType {

	/**
	 * The registry name of the ParticleType (the modid is specified by the
	 * {@link RegistryHolder} on the class the field is in)
	 *
	 * @return
	 */
	String value();

}
