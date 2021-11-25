/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.init;

import net.minecraft.util.DamageSource;

public final class DamageSourcesInit {

	public static final DamageSource LIQUID_HYDROGEN_DAMAGE_SOURCE = new DamageSource("liquidHydrogen").bypassArmor()
			.bypassMagic();

}
