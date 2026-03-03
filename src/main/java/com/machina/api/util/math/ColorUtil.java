package com.machina.api.util.math;

import com.mojang.blaze3d.systems.RenderSystem;

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
	}

	public static RGBA ofRGB(int hex) {
		int r = (hex >> 16) & 0xFF;
		int g = (hex >> 8) & 0xFF;
		int b = hex & 0xFF;
		return new RGBA(r, g, b, 255);
	}
}
