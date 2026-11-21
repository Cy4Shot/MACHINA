package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.util.MachinaRL;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundInit {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT,
			Machina.MOD_ID);

	public static final Supplier<SoundEvent> MUSIC = create("music");
	public static final Supplier<SoundEvent> ROCKET_LAUNCH = create("rocket_launch");
	public static final Supplier<SoundEvent> ROCKET_LAND = create("rocket_land");

	// Nuclear explosion sounds
	public static final Supplier<SoundEvent> NUKE_SNAP = create("nuke_snap");
	public static final Supplier<SoundEvent> NUKE_RUMBLE = create("nuke_rumble");
	public static final Supplier<SoundEvent> NUKE_WIND = create("nuke_wind");
	public static final Supplier<SoundEvent> NUKE_DISTANT_BOOM = create("nuke_distant_boom");

	private static Supplier<SoundEvent> create(String name) {
		return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(MachinaRL.create(name)));
	}

	public static Music asMusic(Supplier<SoundEvent> reg) {
		return new Music(Holder.direct(reg.get()), 0, 0, true);
	}
}
