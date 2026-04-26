package com.machina.api.client.cinema;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import com.machina.api.client.cinema.entity.CinematicClientEntity;
import com.machina.api.client.cinema.effect.renderer.CinematicTextOverlay;
import com.machina.api.client.cinema.effect.renderer.CinematicTextureOverlay;
import com.machina.api.util.math.VecUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(value = Dist.CLIENT)
public class CinematicHandler {

	public static final CinematicHandler INSTANCE = new CinematicHandler();
	private static final Minecraft mc = Minecraft.getInstance();
	private Queue<Pair<BooleanSupplier, Supplier<Cinematic>>> queue = new LinkedList<>();
	private Cinematic current = null;
	private int elapsed = 0;

	public void tick(boolean isClient) {
		if (!mc.isPaused()) {
			if (this.isActive()) {
				mc.setScreen(null);
				float partial = ((DeltaTracker.Timer) mc.getTimer()).getGameTimeDeltaTicks();
				if (isClient) {
					current.transform();

					if (elapsed == 0)
						current.begin();

					current.onClientTick(elapsed, partial);

					elapsed++;
					if (elapsed == current.getDuration()) {
						current.finish();
						current = null;
						elapsed = 0;
					}
				} else {
					current.onRenderTick(elapsed, partial);
				}
			} else {
				Pair<BooleanSupplier, Supplier<Cinematic>> top = this.queue.peek();
				if (top != null && top.getFirst().getAsBoolean()) {
					Cinematic next = top.getSecond().get();
					if (next != null) {
						mc.setScreen(null);
						this.current = next;
						this.queue.poll(); // Dequeue
					}
				}
			}
		}
	}

	public boolean isActive() {
		return current != null;
	}

	public void enqueueCinematic(BooleanSupplier predicate, Supplier<Cinematic> c) {
		this.queue.offer(Pair.of(predicate, c));
	}

	@SubscribeEvent
	public static void onPlayerRender(RenderPlayerEvent.Pre event) {
		if (event.getEntity() == mc.player && INSTANCE.isActive()) {
			event.setCanceled(true);
		}
	}

	public static void setup() {
		NeoForge.EVENT_BUS.addListener((ClientTickEvent.Pre e) -> {
			INSTANCE.tick(true);
		});

		NeoForge.EVENT_BUS.addListener((RenderLevelStageEvent e) -> {
			if (e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES))
				INSTANCE.tick(false);

			if (e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_ENTITIES)) {
				if (INSTANCE.isActive() && INSTANCE.current.clientEntity != null) {
					CinematicClientEntity cce = INSTANCE.current.clientEntity;
					Vec3 delta = cce.position().subtract(INSTANCE.current.player.position());
					PoseStack stack = e.getPoseStack();

					stack.pushPose();
					stack.translate(delta.x, delta.y - cce.getEyeHeight(), delta.z);
					stack.mulPose(VecUtil.rotationDegrees(VecUtil.YN, cce.getYRot()));

					mc.getEntityRenderDispatcher().getRenderer(cce).render(cce, 0, 0, stack,
							mc.renderBuffers().bufferSource(), LightTexture.pack(0, 15));

					stack.popPose();
				}
			}
		});

		NeoForge.EVENT_BUS.addListener((RenderGuiEvent.Pre e) -> {
			if (INSTANCE.isActive() && !mc.isPaused()) {
				CinematicTextureOverlay.renderOverlay();
				CinematicTextOverlay.renderOverlay(e.getGuiGraphics());
				e.setCanceled(true);
			}
		});

		NeoForge.EVENT_BUS.addListener((ScreenEvent.Opening e) -> {
			if (INSTANCE.isActive() && !mc.isPaused()) {
				e.getScreen();
				e.setCanceled(true);
			}
		});

		NeoForge.EVENT_BUS.addListener((RenderHandEvent e) -> {
			if (INSTANCE.isActive() && !mc.isPaused()) {
				e.setCanceled(true);
			}
		});
	}
}