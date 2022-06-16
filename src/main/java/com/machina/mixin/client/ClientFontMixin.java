package com.machina.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.machina.client.util.MachinaGlyph;

import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.IGlyphInfo;

@Mixin(Font.class)
public class ClientFontMixin {

	// Replace Char 9601 with better space.
	@Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/client/gui/fonts/Font;getRaw(I)Lnet/minecraft/client/gui/fonts/IGlyphInfo;", cancellable = true)
	private void getRaw(int p_212455_1_, CallbackInfoReturnable<IGlyphInfo> callback) {
		if (p_212455_1_ == 9601) {
			callback.setReturnValue(MachinaGlyph.INSTANCE);
		}
	}
}
