/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.registry.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(ElementType.METHOD)
/**
 * Used in order to register custom forge registries during {@link NewRegistry}
 * <br>
 * In order for it to work, you need to annotate a method that takes in
 * <strong>just<strong> a {@link RegistryEvent.NewRegistry} as a parameter with
 * this annotation. <br>
 * {@link RegistryHolder} is <strong>still needed</strong>
 *
 * @author matyrobbrt
 *
 */
public @interface RegisterCustomRegistry {

}
