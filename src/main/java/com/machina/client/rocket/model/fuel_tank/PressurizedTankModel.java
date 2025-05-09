package com.machina.client.rocket.model.fuel_tank;

import com.machina.api.util.MachinaRL;
import com.machina.client.rocket.model.RocketPartModel;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class PressurizedTankModel extends RocketPartModel {
	private final ModelPart core;

	public PressurizedTankModel(ModelPart root) {
		this.core = root.getChild("core");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("core", CubeListBuilder.create().texOffs(48, 32).addBox(-8.0F, -44.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 80).addBox(-6.0F, -41.0F, -6.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(48, 87).addBox(-4.0F, -44.0F, -4.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, 52.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	protected ModelPart main() {
		return this.core;
	}

	@Override
	protected ResourceLocation getTextureLocation() {
		return new MachinaRL("textures/rocket/tri_rocket.png");
	}
}