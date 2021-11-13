package com.cy4.machina.util.helper;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Class that contains different methods that may be useful when dealing with {@link PlayerEntity}s
 * @author matyrobbrt
 *
 */
public class PlayerHelper {

	public static void damageAllArmour(PlayerEntity player, int damageAmount) {
		for (int i = 0; i < player.inventory.armor.size() - 1; i++) {
			if (player.inventory.armor.get(i).hurt(damageAmount, new Random(), null)) {
				player.inventory.armor.get(i).shrink(1);
			}
		}
	}

}
