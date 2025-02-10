package com.machina.api.starchart.burke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class Generator {

	private static final Logger log = LoggerFactory.getLogger(Generator.class);

	public static void main(String[] args) {
		AccreteObject.cr.setSeed(AccreteObject.cr.nextLong());
		StarSystem ss = new StarSystem();
		System.out.println(toString(ss.planets));
	}

	public static String toString(Object obj) {
		StringBuilder result = new StringBuilder();
		String newLine = System.lineSeparator();

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
				log.error("e: ", ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}

}
