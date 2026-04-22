package com.machina.api.natives;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface CoolpropJNA extends Library {

	CoolpropJNA INSTANCE = load();

	private static CoolpropJNA load() {
		String path = CoolpropLoader.load();
		return Native.load(path, CoolpropJNA.class);
	}

	void PhaseSI(String Name1, double Prop1, String Name2, double Prop2, String FluidName, byte[] phase, long n);

	static String PhaseSI(String Name1, double Prop1, String Name2, double Prop2, String FluidName) {

		byte[] buffer = new byte[256];

		INSTANCE.PhaseSI(Name1, Prop1, Name2, Prop2, FluidName, buffer, buffer.length);

		String result = Native.toString(buffer);

		if (result == null || result.isEmpty()) {
			throw new RuntimeException("CoolProp returned empty phase string");
		}

		return result;
	}

}
