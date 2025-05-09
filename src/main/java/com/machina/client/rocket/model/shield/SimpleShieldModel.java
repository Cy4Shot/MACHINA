package com.machina.client.rocket.model.shield;

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

public class SimpleShieldModel extends RocketPartModel {
	private final ModelPart shield;

	public SimpleShieldModel() {
		ModelPart root = this.createBodyLayer().bakeRoot();
		this.shield = root.getChild("shield");
	}

	@Override
	public LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("shield", CubeListBuilder.create().texOffs(64, 64).addBox(-6.0F, -87.0F, -6.0F,
				12.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 100.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected ModelPart main() {
		return this.shield;
	}

	@Override
	protected ResourceLocation getTextureLocation() {
		return new MachinaRL("textures/rocket/tri_rocket.png");
	}
}