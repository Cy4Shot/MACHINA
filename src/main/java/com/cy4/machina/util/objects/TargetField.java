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
