package com.machina.client.util;

import net.minecraft.client.gui.fonts.IGlyphInfo;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum MachinaGlyph implements IGlyphInfo {
	INSTANCE;

	private static final NativeImage IMAGE_DATA = Util.make(new NativeImage(NativeImage.PixelFormat.RGBA, 8, 8, false),
			(p_211580_0_) -> {
				for (int i = 0; i < 8; ++i) {
					for (int j = 0; j < 8; ++j) {
						p_211580_0_.setPixelRGBA(j, i, 0);
					}
				}

				p_211580_0_.untrack();
			});

	public int getPixelWidth() {
		return 8;
	}

	public int getPixelHeight() {
		return 8;
	}

	public float getAdvance() {
		return 9.0F;
	}

	public float getOversample() {
		return 0.0F;
	}

	public void upload(int pXOffset, int pYOffset) {
		IMAGE_DATA.upload(0, pXOffset, pYOffset, false);
	}

	public boolean isColored() {
		return true;
	}
}