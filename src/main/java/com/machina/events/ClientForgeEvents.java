package com.machina.events;

import java.util.Set;

import com.machina.Machina;
import com.machina.api.client.ClientStarchart;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.client.ClientRadiationData;
import com.machina.client.ClientVaultState;
import com.machina.client.explosion.NuclearExplosionEffects;
import com.machina.client.explosion.NuclearExplosionManager;
import com.machina.client.explosion.NuclearExplosionRenderer;
import com.machina.client.explosion.sound.NuclearExplosionSounds;
import com.machina.client.screen.StarchartScreen;
import com.machina.registration.init.KeyBindingInit;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import com.machina.particle.BiomeAshParticleType.BiomeAshParticleOptions;
import com.machina.registration.init.ParticleTypeInit;

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
import net.neoforged.neoforge.client.event.ViewportEvent;

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

		// Spawn vault particles when inside vault (synced from server)
		if (ClientVaultState.isInVault()) {
			spawnVaultParticles();
		}

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

	// === VAULT EFFECTS ===

	private static void spawnVaultParticles() {
		if (mc.player == null || mc.level == null || mc.isPaused())
			return;

		ClientLevel level = mc.level;
		RandomSource random = level.random;

		// Mostly gray/white ash, some cyan
		for (int i = 0; i < 800; i++) {
			double x = mc.player.getX() + (random.nextDouble() - 0.5) * 30;
			double y = mc.player.getY() + 1 + random.nextDouble() * 8;
			double z = mc.player.getZ() + (random.nextDouble() - 0.5) * 30;

			float roll = random.nextFloat();
			int color = roll < 0.85f ? 0xFF1c1924 : 0xFF00CCCC;
			level.addParticle(new BiomeAshParticleOptions(color), x, y, z, 0, -0.03, 0);
		}
	}

	@SubscribeEvent
	public static void onRenderFog(final ViewportEvent.RenderFog event) {
		if (!ClientVaultState.isInVault())
			return;

		// Very dark, dense fog in the vault
		RenderSystem.setShaderFogStart(2.0f);
		RenderSystem.setShaderFogEnd(40.0f);
		RenderSystem.setShaderFogShape(FogShape.SPHERE);
		event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onFogColor(final ViewportEvent.ComputeFogColor event) {
		if (!ClientVaultState.isInVault())
			return;

		// Very dark fog color
		event.setRed(0.02f);
		event.setGreen(0.02f);
		event.setBlue(0.03f);
	}
}
