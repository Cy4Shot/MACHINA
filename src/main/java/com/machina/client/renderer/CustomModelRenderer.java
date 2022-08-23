package com.machina.client.renderer;

import java.lang.reflect.Field;

import com.machina.block.tile.CustomModelTileEntity;
import com.machina.client.model.CustomBlockModel;
import com.machina.util.reflection.ClassHelper;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class CustomModelRenderer extends GeoBlockRenderer<CustomModelTileEntity> {

	public CustomModelRenderer(TileEntityRendererDispatcher disp) {
		super(disp, CustomBlockModel.create("cargo_crate"));
	}

	@Override
	public void render(TileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		try {
			CustomModelTileEntity cmte = (CustomModelTileEntity) te;
			Field field = ObfuscationReflectionHelper.findField(GeoBlockRenderer.class, "modelProvider");
			ClassHelper.removeFinalModifier(field);
			ClassHelper.removePrivateModifier(field);
			field.set(this, CustomBlockModel.create(cmte.name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.render(te, partialTicks, stack, bufferIn, combinedLightIn, combinedOverlayIn);
	}
}