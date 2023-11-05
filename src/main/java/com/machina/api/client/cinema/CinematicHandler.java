package com.machina.api.client.cinema;

import com.machina.Machina;
import com.machina.api.client.ClientTimer;
import com.machina.api.client.cinema.entity.CinematicClientEntity;
import com.machina.api.util.math.MathUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.FORGE)
public class CinematicHandler {

	public static final CinematicHandler INSTANCE = new CinematicHandler();
	private static final Minecraft mc = Minecraft.getInstance();
	private Cinematic current = null;
	private int elapsed = 0;

	public void tick(TickEvent.Type type, float partial) {
		if (!mc.isPaused()) {
			if (this.isActive()) {
				mc.setScreen(null);
				if (type == TickEvent.Type.CLIENT) {
					current.transform(mc.isPaused() ? 0f : partial);

					if (elapsed == 0)
						current.begin(partial);

					current.onClientTick(elapsed, partial);

					elapsed++;
					if (elapsed == current.getDuration()) {
						current.finish(partial);
						current = null;
						elapsed = 0;
					}
				} else if (type == TickEvent.Type.RENDER) {
					current.onRenderTick(elapsed, partial);
				}
			}
		}
	}

	public boolean isActive() {
		return current != null;
	}

	public void setCinematic(Cinematic c) {
		if (!isActive()) {
			mc.setScreen(null);
			System.out.println("Playing cinematic " + c.getDuration());
			this.current = c;
		}
	}

	@SubscribeEvent
	public static void onPlayerRender(RenderPlayerEvent.Pre event) {
		if (event.getEntity() == mc.player && INSTANCE.isActive()) {
			event.setCanceled(true);
		}
	}

	public static void setup() {
		MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent e) -> {
			if (e.phase == TickEvent.Phase.START)
				INSTANCE.tick(e.type, ClientTimer.partialTick);
		});

		MinecraftForge.EVENT_BUS.addListener((RenderLevelStageEvent e) -> {
			if (e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES))
				INSTANCE.tick(TickEvent.Type.RENDER, ClientTimer.partialTick);

			if (e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_ENTITIES)) {
				if (INSTANCE.isActive() && INSTANCE.current.clientEntity != null) {
					CinematicClientEntity cce = INSTANCE.current.clientEntity;
					Vec3 delta = cce.position().subtract(INSTANCE.current.player.position());
					PoseStack stack = e.getPoseStack();

					stack.pushPose();
					stack.translate(delta.x, delta.y - cce.getEyeHeight(), delta.z);
					stack.mulPose(MathUtil.rotationDegrees(MathUtil.YN, cce.getYRot()));

					mc.getEntityRenderDispatcher().getRenderer(cce).render(cce, 0, 0, stack,
							mc.renderBuffers().bufferSource(), LightTexture.pack(0, 15));

					stack.popPose();
				}
			}
		});

		MinecraftForge.EVENT_BUS.addListener((RenderGuiOverlayEvent.Pre e) -> {
			if (INSTANCE.isActive() && !mc.isPaused()) {
				if (!e.getOverlay().id().getNamespace().equals(Machina.MOD_ID))
					e.setCanceled(true);
			}
		});

		MinecraftForge.EVENT_BUS.addListener((ScreenEvent.Opening e) -> {
			if (INSTANCE.isActive() && !mc.isPaused()) {
				if (e.getScreen() != null || e.getScreen() instanceof PauseScreen) {
					e.setCanceled(true);
				}
			}
		});

		MinecraftForge.EVENT_BUS.addListener((RenderHandEvent e) -> {
			if (INSTANCE.isActive() && !mc.isPaused()) {
				e.setCanceled(true);
			}
		});
	}
}