/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.util.objects;

import java.lang.reflect.Field;

public class TargetField {

	public final Class<?> targetFieldClass;
	public final String targetFieldName;

	public TargetField(Class<?> targetFieldClass, String targetFieldName) {
		this.targetFieldClass = targetFieldClass;
		this.targetFieldName = targetFieldName;
	}

	public Field getField() throws NoSuchFieldException, SecurityException {
		return targetFieldClass.getDeclaredField(targetFieldName);
	}

}
