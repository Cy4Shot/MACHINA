package com.machina.api.natives;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface CoolpropJNA extends Library {
	CoolpropJNA INSTANCE = load();

	private static CoolpropJNA load() {
		String path = CoolpropLoader.load();
		return Native.load(path, CoolpropJNA.class);
	}

	long PhaseSI(String Name1, double Prop1, String Name2, double Prop2, String FluidName, Pointer phase, int n);

	static String PhaseSI(String Name1, double Prop1, String Name2, double Prop2, String FluidName) {
		int bufsize = 64;
		Memory phaseBuf = new Memory(bufsize);
		long err = INSTANCE.PhaseSI(Name1, Prop1, Name2, Prop2, FluidName, phaseBuf, bufsize);
		if (err == 0) {
			return phaseBuf.getString(0);
		} else {
			return "";
		}
	}
}