package com.machina.client.screen.overlay;

import com.machina.Machina;
import com.machina.api.util.MachinaRL;
import com.machina.registration.init.EffectInit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT)
public class RadiationHeartOverlay {

	private static final ResourceLocation RADIATION_HEART_FULL = MachinaRL.create("hud/heart/radiation_full");
	private static final ResourceLocation RADIATION_HEART_FULL_BLINKING = MachinaRL
			.create("hud/heart/radiation_full_blinking");
	private static final ResourceLocation RADIATION_HEART_HALF = MachinaRL.create("hud/heart/radiation_half");
	private static final ResourceLocation RADIATION_HEART_HALF_BLINKING = MachinaRL
			.create("hud/heart/radiation_half_blinking");
	private static final ResourceLocation RADIATION_HEART_CONTAINER = MachinaRL.create("hud/heart/radiation_container");
	private static final ResourceLocation RADIATION_HEART_CONTAINER_BLINKING = MachinaRL
			.create("hud/heart/radiation_container_blinking");

	@SubscribeEvent
	public static void onRenderGuiLayerPost(RenderGuiLayerEvent.Post event) {
		if (!event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH)) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player == null || mc.options.hideGui || !shouldShowOverlay(mc)) {
			return;
		}

		MobEffectInstance radiation = player.getEffect(EffectInit.RADIATION);
		if (radiation == null) {
			return;
		}

		renderRadiationHearts(event.getGuiGraphics(), mc, player);
	}

	private static boolean shouldShowOverlay(Minecraft mc) {
		return mc.gameMode != null && mc.gameMode.canHurtPlayer();
	}

	private static void renderRadiationHearts(GuiGraphics graphics, Minecraft mc, Player player) {
		int health = Mth.ceil(player.getHealth());
		int absorb = Mth.ceil(player.getAbsorptionAmount());

		int maxHearts = Mth.ceil(player.getAttributeValue(Attributes.MAX_HEALTH) / 2.0);
		int rowCount = Mth.ceil((maxHearts + Mth.ceil(absorb / 2.0F)) / 10.0F);
		int rowHeight = Math.max(10 - (rowCount - 2), 3);

		int screenWidth = mc.getWindow().getGuiScaledWidth();
		int screenHeight = mc.getWindow().getGuiScaledHeight();

		int left = screenWidth / 2 - 91;
		int top = screenHeight - 39;

		int regen = -1;
		if (player.hasEffect(net.minecraft.world.effect.MobEffects.REGENERATION)) {
			regen = mc.gui.getGuiTicks() % (maxHearts * 5);
		}

		long tickCount = mc.gui.getGuiTicks();
		boolean blink = (tickCount / 3) % 2 == 0;

		for (int i = maxHearts - 1; i >= 0; i--) {
			int row = i / 10;
			int col = i % 10;

			int x = left + col * 8;
			int y = top - row * rowHeight;

			if (regen == i) {
				y -= 2;
			}

			ResourceLocation container = blink ? RADIATION_HEART_CONTAINER_BLINKING : RADIATION_HEART_CONTAINER;
			graphics.blitSprite(container, x, y, 9, 9);

			int heartIndex = i * 2;
			if (heartIndex + 1 < health) {
				ResourceLocation full = blink ? RADIATION_HEART_FULL_BLINKING : RADIATION_HEART_FULL;
				graphics.blitSprite(full, x, y, 9, 9);
			} else if (heartIndex < health) {
				ResourceLocation half = blink ? RADIATION_HEART_HALF_BLINKING : RADIATION_HEART_HALF;
				graphics.blitSprite(half, x, y, 9, 9);
			}
		}
	}
}
