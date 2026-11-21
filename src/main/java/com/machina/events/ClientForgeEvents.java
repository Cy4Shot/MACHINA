package com.machina.events;

import java.util.Set;

import com.machina.Machina;
import com.machina.api.client.ClientStarchart;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.client.ClientRadiationData;
import com.machina.client.explosion.NuclearExplosionEffects;
import com.machina.client.explosion.NuclearExplosionManager;
import com.machina.client.explosion.NuclearExplosionRenderer;
import com.machina.client.explosion.sound.NuclearExplosionSounds;
import com.machina.client.screen.StarchartScreen;
import com.machina.registration.init.KeyBindingInit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

@EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT)
public class ClientForgeEvents {

	private static final Minecraft mc = Minecraft.getInstance();

	@SubscribeEvent
	public static void onClientTick(final ClientTickEvent.Pre event) {
		if (mc.level == null)
			return;

		// Effects run even with screen open (overlay, shake, sounds)
		NuclearExplosionManager.tick();
		NuclearExplosionEffects.tick();
		NuclearExplosionSounds.tick();

		if (mc.screen != null)
			return;

		if (KeyBindingInit.STARCHART_KEY.isDown()) {
			mc.setScreen(new StarchartScreen(ClientStarchart.system));
		}

		spawnRadiationMist();
	}

	@SubscribeEvent
	public static void onRenderLevelStage(final RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
			NuclearExplosionRenderer.render(event.getPoseStack(), event.getCamera(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
		}
	}

	private static void spawnRadiationMist() {
		if (mc.player == null || mc.level == null || mc.isPaused()) {
			return;
		}

		ClientLevel level = mc.level;
		RandomSource random = level.random;
		Set<ChunkPos> irradiatedChunks = ClientRadiationData.getIrradiatedChunks();

		if (irradiatedChunks.isEmpty()) {
			return;
		}

		ChunkPos playerChunk = new ChunkPos(mc.player.blockPosition());

		for (ChunkPos chunk : irradiatedChunks) {
			int dx = Math.abs(chunk.x - playerChunk.x);
			int dz = Math.abs(chunk.z - playerChunk.z);
			if (dx > 4 || dz > 4) {
				continue;
			}

			int particleCount = (dx <= 1 && dz <= 1) ? 3 : 1;

			for (int i = 0; i < particleCount; i++) {
				double x = chunk.getMinBlockX() + random.nextDouble() * 16;
				double z = chunk.getMinBlockZ() + random.nextDouble() * 16;
				double y = mc.player.getY() + (random.nextDouble() - 0.5) * 8;

				int green = 0x7FFF00;
				int color = (0x80 << 24) | green;
				level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, color), x, y, z, 0, 0.02, 0);
			}
		}
	}

	@SubscribeEvent
	public static void tooltipCol(final RenderTooltipEvent.Color event) {
		Screen s = mc.screen;
		if (s instanceof MachinaMenuScreen<?>) {
			event.setBackground(0xFF_202020);
			event.setBorderEnd(0xFF_1bcccc);
			event.setBorderStart(0xFF_00fefe);
		}
	}
}
