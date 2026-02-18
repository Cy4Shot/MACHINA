package com.machina.api.client.model;

import com.machina.Machina;
import com.machina.api.client.model.bewlr.BEWLRLoader;
import com.machina.api.client.model.connector.ConnectorModelLoader;
import com.machina.api.util.MachinaRL;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

@EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT)
public class ModelLoader {

	private static final String BLOCK_ATLAS = "minecraft:textures/atlas/blocks.png";

	public static TextureAtlasSprite MACHINE_FACE_NONE;
	public static TextureAtlasSprite MACHINE_FACE_INPUT;
	public static TextureAtlasSprite MACHINE_FACE_OUTPUT;

	@SubscribeEvent
	public static void postStitch(TextureAtlasStitchedEvent event) {
		if (!event.getAtlas().location().toString().equals(BLOCK_ATLAS)) {
			return;
		}

		SidedBakedModel.clearCache();
		TextureAtlas map = event.getAtlas();
		MACHINE_FACE_NONE = map.getSprite(MachinaRL.create("block/side/none"));
		MACHINE_FACE_INPUT = map.getSprite(MachinaRL.create("block/side/input"));
		MACHINE_FACE_OUTPUT = map.getSprite(MachinaRL.create("block/side/output"));
	}

	@SubscribeEvent
	public static void registerModels(final RegisterGeometryLoaders event) {
		event.register(MachinaRL.create("sided"), new SimpleModel.Loader(SidedBakedModel::new));
		event.register(MachinaRL.create("connector"), ConnectorModelLoader.INSTANCE);
		event.register(MachinaRL.create("bewlr"), BEWLRLoader.INSTANCE);
	}
}
