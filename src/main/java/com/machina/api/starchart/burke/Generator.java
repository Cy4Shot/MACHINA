package com.machina.api.starchart.burke;

import java.lang.reflect.Field;

public class Generator {

	public static void main(String[] args) {
		AccreteObject.cr.setSeed(AccreteObject.cr.nextLong());
		StarSystem ss = new StarSystem();
		System.out.println(toString(ss.planets));
	}

	public static String toString(Object obj) {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(obj.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		// determine fields declared in this class only (no fields of superclass)
		Field[] fields = obj.getClass().getDeclaredFields();

		// print field names paired with their values
		for (Field field : fields) {
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				result.append(field.get(obj));
			} catch (IllegalAccessException ex) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}

}
