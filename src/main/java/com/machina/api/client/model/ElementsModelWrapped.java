package com.machina.api.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.SimpleUnbakedGeometry;

// CofH
public class ElementsModelWrapped extends SimpleUnbakedGeometry<ElementsModelWrapped> {

	private final List<BlockElement> elements;

	private ElementsModelWrapped(List<BlockElement> elements) {

		this.elements = elements;
	}

	@Override
	public void addQuads(IGeometryBakingContext context, IModelBuilder<?> modelBuilder, ModelBaker bakery,
			Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState,
			ResourceLocation modelLocation) {

		var rootTransform = context.getRootTransform();
		if (!rootTransform.isIdentity())
			modelState = new SimpleModelState(modelState.getRotation().compose(rootTransform), modelState.isUvLocked());

		for (BlockElement element : elements) {
			for (Direction direction : element.faces.keySet()) {
				BlockElementFace face = element.faces.get(direction);
				TextureAtlasSprite sprite = spriteGetter.apply(context.getMaterial(face.texture));
				BakedQuad quad = BlockModel.bakeFace(element, face, sprite, direction, modelState, modelLocation);

				modelBuilder.addCulledFace(modelState.getRotation().rotateTransform(face.cullForDirection), quad);
			}
		}
	}

	public static final class Loader implements IGeometryLoader<ElementsModelWrapped> {

		public static final Loader INSTANCE = new Loader();

		private Loader() {

		}

		@Override
		public ElementsModelWrapped read(JsonObject jsonObject, JsonDeserializationContext deserializationContext)
				throws JsonParseException {

			if (!jsonObject.has("elements")) {
				throw new JsonParseException("An element model must have an \"elements\" member.");
			}
			List<BlockElement> elements = new ArrayList<>();
			for (JsonElement element : GsonHelper.getAsJsonArray(jsonObject, "elements")) {
				elements.add(deserializationContext.deserialize(element, BlockElement.class));
			}
			return new ElementsModelWrapped(elements);
		}

	}

}