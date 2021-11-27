package com.machina.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.machina.api.events.CraftingScreenEvent;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.util.text.ITextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Mixin(CraftingScreen.class)
public abstract class CraftingScreenMixin extends ContainerScreen<WorkbenchContainer> implements IRecipeShownListener {

	private CraftingScreenMixin(WorkbenchContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Inject(method = "renderBg(Lcom/mojang/blaze3d/matrix/MatrixStack;FII)V", at = @At("TAIL"), cancellable = true)
	private void machina$renderBgEvent(MatrixStack matrixStack, float partialTicks, int x, int y, CallbackInfo ci) {
		CraftingScreenEvent.onRenderBg((CraftingScreen) (Object) this, minecraft, matrixStack, partialTicks, x, y);
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;IIF)V", at = @At("TAIL"), cancellable = true)
	private void machina$renderEvent(MatrixStack matrixStack, int x, int y, float partialTicks, CallbackInfo ci) {
		CraftingScreenEvent.onRender((CraftingScreen) (Object) this, minecraft, matrixStack, partialTicks, x, y);
	}

}
