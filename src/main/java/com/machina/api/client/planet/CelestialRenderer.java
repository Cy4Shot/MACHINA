package com.machina.api.client.planet;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.machina.api.client.RenderTypes;
import com.machina.api.starchart.obj.Orbit;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.rendering.VFXBuilders.WorldVFXBuilder;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailRenderPoint;

public class CelestialRenderer extends WorldVFXBuilder {

	public CelestialRenderer() {
		setPosColorTexLightmapDefaultFormat();
	}

	public WorldVFXBuilder drawCelestial(MultiBufferSource mbs, PoseStack stack, int detail, CelestialRenderInfo info,
			double time, ScreenParticleHolder p_target) {

		Vector3f scaling = new Transformation(stack.last().pose()).getScale();
		float scale = scaling.get(scaling.maxComponent());
		
		double size = 1D / Math.sqrt(info.orbit().a());
		double alpha = size == 0D ? 0f : (1f - (Math.abs(scale - size) / (size * 5f)));
        alpha = Math.max(0.0, Math.min(alpha, 1.0));

		setColor(0, 255, 255, (float) alpha);
		List<TrailPoint> orbit = generateOrbitPoints(info.orbit(), 100).stream().map(TrailPoint::new).toList();
		renderTrail(mbs.getBuffer(RenderTypes.ORBIT), stack.last().pose(), orbit, 0f);
		setColor(Color.WHITE);

		Vec3 pos = info.getOrbitalCoords(time);
		float rad = (float) info.radius();
		stack.pushPose();
		stack.translate(pos.x, pos.y, pos.z);

		Vec2 sp = asScreenPos(stack, info.width(), info.height());
		info.particle().accept(sp, p_target);

		stack.scale(rad, rad, rad);

		sphere(mbs, RenderTypes.getOrCreateCelestial(info.bg()), stack, 1f, detail);
		// TODO: Based on atm density
		sphere(mbs, RenderTypes.getOrCreateCelestial(info.fg()), stack, 0.5f, detail);

		stack.popPose();
		return this;
	}

	public static Vec2 asScreenPos(PoseStack stack, int w, int h) {
		Vector4f spos = new Vector4f(0, 0, 0, 1);
		Matrix4f stm = new Matrix4f(stack.last().pose());
		Matrix4f mvp = new Matrix4f(RenderSystem.getProjectionMatrix()).mul(RenderSystem.getModelViewMatrix());

		stm.transform(spos);
		mvp.transform(spos);

		Vector4f norm = spos.div(spos.w);
		float x = (1.0f + norm.x) * 0.5f * w;
		float y = (1.0f - norm.y) * 0.5f * h;

		return new Vec2(x, y);
	}

	public static List<Vec3> generateOrbitPoints(Orbit o, int numPoints) {
		List<Vec3> orbitPoints = new ArrayList<>();
		double a = o.a();
		double e = o.e();

		for (int i = 0; i < numPoints; i++) {
			double trueAnomaly = 2 * Math.PI * i / numPoints;
			double radius = a * (1 - e * e) / (1 + e * Math.cos(trueAnomaly));

			double x = radius * Math.cos(trueAnomaly);
			double y = radius * Math.sin(trueAnomaly);

			orbitPoints.add(new Vec3(x, 0, y));
		}

		return orbitPoints;
	}

	public WorldVFXBuilder renderTrail(VertexConsumer vertexConsumer, Matrix4f pose, List<TrailPoint> trail, float w) {
		if (trail.size() < 3) {
			return this;
		}
		List<Vector4f> positions = trail.stream().map(TrailPoint::getMatrixPosition).peek(p -> p.mul(pose)).toList();
		ArrayList<TrailRenderPoint> points = new ArrayList<>();
		for (int i = 0; i < trail.size() - 1; i++) {
			points.add(new TrailRenderPoint(positions.get(i), Vec2.ZERO));
		}
		return renderPoints(vertexConsumer, points);
	}

	public WorldVFXBuilder renderPoints(VertexConsumer vc, List<TrailRenderPoint> ps) {
		Consumer<Integer> place = i -> {
			TrailRenderPoint p = ps.get(i);
			supplier.placeVertex(vc, null, p.xp, p.yp, p.z, u0, v1);
		};

		place.accept(0);
		for (int i = 1; i < ps.size(); i++) {
			place.accept(i);
			place.accept(i);
		}
		place.accept(0);
		return this;
	}

	private WorldVFXBuilder sphere(MultiBufferSource m, RenderType t, PoseStack s, float a, int d) {
		return renderSphere(m.getBuffer(t), s, d, d, a);
	}

	// Thanks rat man :)
	public CelestialRenderer renderSphere(VertexConsumer c, PoseStack stack, int longs, int lats, float a) {
		Matrix4f last = stack.last().pose();
		float startU = 0.0F;
		float startV = 0.0F;
		float radius = 1.0F;
		float endU = Mth.TWO_PI;
		float endV = Mth.PI;
		float stepU = (endU - startU) / longs;
		float stepV = (endV - startV) / lats;
		for (int i = 0; i < longs; i++) {
			for (int j = 0; j < lats; j++) {
				float u = i * stepU + startU;
				float v = j * stepV + startV;
				float un = (i + 1 == longs) ? endU : ((i + 1) * stepU + startU);
				float vn = (j + 1 == lats) ? endV : ((j + 1) * stepV + startV);
				float cu = Mth.cos(u);
				float su = Mth.sin(u);
				float cv = Mth.cos(v);
				float sv = Mth.sin(v);
				float cun = Mth.cos(un);
				float sun = Mth.sin(un);
				float cvn = Mth.cos(vn);
				float svn = Mth.sin(vn);
				float p0x = cu * sv * radius;
				float p0y = cv * radius;
				float p0z = su * sv * radius;
				float p1x = cu * svn * radius;
				float p1y = cvn * radius;
				float p1z = su * svn * radius;
				float p2x = cun * sv * radius;
				float p2y = cv * radius;
				float p2z = sun * sv * radius;
				float p3x = cun * svn * radius;
				float p3y = cvn * radius;
				float p3z = sun * svn * radius;
				float textureU = u / endU * radius;
				float textureV = v / endV * radius;
				float textureUN = un / endU * radius;
				float textureVN = vn / endV * radius;
				v(c, last, p0x, p0y, p0z, r, g, b, a, textureU, textureV, light);
				v(c, last, p2x, p2y, p2z, r, g, b, a, textureUN, textureV, light);
				v(c, last, p1x, p1y, p1z, r, g, b, a, textureU, textureVN, light);
				v(c, last, p3x, p3y, p3z, r, g, b, a, textureUN, textureVN, light);
				v(c, last, p1x, p1y, p1z, r, g, b, a, textureU, textureVN, light);
				v(c, last, p2x, p2y, p2z, r, g, b, a, textureUN, textureV, light);
			}
		}
		return this;
	}

	public static void v(VertexConsumer c, Matrix4f m, float x, float y, float z, float r, float g, float b, float a,
			float u, float v, int l) {
		c.vertex(m, x, y, z).color(r, g, b, a).uv(u, v).uv2(l).endVertex();
	}
}