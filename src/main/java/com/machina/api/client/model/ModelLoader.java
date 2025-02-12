package com.machina.api.client.model;

import com.machina.Machina;
import com.machina.api.client.model.connector.ConnectorModelLoader;
import com.machina.api.util.MachinaRL;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelLoader {

	private static final String BLOCK_ATLAS = "minecraft:textures/atlas/blocks.png";

	public static TextureAtlasSprite MACHINE_FACE_NONE;
	public static TextureAtlasSprite MACHINE_FACE_INPUT;
	public static TextureAtlasSprite MACHINE_FACE_OUTPUT;

	@SubscribeEvent
	public static void postStitch(TextureStitchEvent.Post event) {
		if (!event.getAtlas().location().toString().equals(BLOCK_ATLAS)) {
			return;
		}

		SidedBakedModel.clearCache();
		TextureAtlas map = event.getAtlas();
		MACHINE_FACE_NONE = map.getSprite(new MachinaRL("block/side/none"));
		MACHINE_FACE_INPUT = map.getSprite(new MachinaRL("block/side/input"));
		MACHINE_FACE_OUTPUT = map.getSprite(new MachinaRL("block/side/output"));
	}

	@SubscribeEvent
	public static void registerModels(final RegisterGeometryLoaders event) {
		event.register("sided", new SimpleModel.Loader(SidedBakedModel::new));
		event.register("connector", ConnectorModelLoader.INSTANCE);
	}
}
