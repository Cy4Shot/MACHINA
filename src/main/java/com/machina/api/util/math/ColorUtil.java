package com.machina.api.util.math;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ColorUtil {

	public static final RGBA WHITE = new RGBA(255, 255, 255, 255);

	public record RGBA(int r, int g, int b, int a) {
		public RGBA lerp(RGBA other, float t) {
			int newR = (int) (this.r + (other.r - this.r) * t);
			int newG = (int) (this.g + (other.g - this.g) * t);
			int newB = (int) (this.b + (other.b - this.b) * t);
			int newA = (int) (this.a + (other.a - this.a) * t);
			return new RGBA(newR, newG, newB, newA);
		}

		@OnlyIn(Dist.CLIENT)
		public void setRenderSystemColor() {
			RenderSystem.setShaderColor(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
		}

		@OnlyIn(Dist.CLIENT)
		public void setRenderSystemFogColor() {
			RenderSystem.setShaderFogColor(r / 255.0f, g / 255.0f, b / 255.0f, 1f);
		}

		public RGBA mul(RGBA other) {
			int nr = (int) Math.clamp(r * other.r / 255f, 0f, 255f);
			int ng = (int) Math.clamp(g * other.g / 255f, 0f, 255f);
			int nb = (int) Math.clamp(b * other.b / 255f, 0f, 255f);
			int na = (int) Math.clamp(a * other.a / 255f, 0f, 255f);
			return new RGBA(nr, ng, nb, na);
		}
		
		public RGBA mul(Vec3 other) {
			int nr = (int) Math.clamp(r * other.x, 0f, 255f);
			int ng = (int) Math.clamp(g * other.y, 0f, 255f);
			int nb = (int) Math.clamp(b * other.z, 0f, 255f);
			return new RGBA(nr, ng, nb, a);
		}

		public Vec3 vec3() {
			return new Vec3(r / 255f, g / 255f, b / 255f);
		}
	}

	public static RGBA ofRGB(int hex) {
		int r = (hex >> 16) & 0xFF;
		int g = (hex >> 8) & 0xFF;
		int b = hex & 0xFF;
		return new RGBA(r, g, b, 255);
	}

	public static RGBA ofRGBA(int hex) {
		int a = (hex >> 24) & 0xFF;
		int r = (hex >> 16) & 0xFF;
		int g = (hex >> 8) & 0xFF;
		int b = hex & 0xFF;
		return new RGBA(r, g, b, a);
	}
}
