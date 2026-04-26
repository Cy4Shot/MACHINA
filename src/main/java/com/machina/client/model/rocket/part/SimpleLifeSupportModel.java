package com.machina.client.model.rocket.part;

import com.machina.api.util.MachinaRL;
import com.machina.client.model.rocket.RocketPartModel;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class SimpleLifeSupportModel extends RocketPartModel {
	private final ModelPart LifeSupport;

	public SimpleLifeSupportModel() {
		ModelPart root = this.createBodyLayer().bakeRoot();
		this.LifeSupport = root.getChild("LifeSupport");
	}

	@Override
	public LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("LifeSupport",
				CubeListBuilder.create().texOffs(8, 108)
						.addBox(-8.0F, -68.0F, -8.0F, 2.0F, 32.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 108)
						.addBox(-8.0F, -68.0F, 6.0F, 2.0F, 32.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(100, 104)
						.addBox(6.0F, -68.0F, 6.0F, 2.0F, 32.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 64)
						.addBox(-6.0F, -68.0F, -6.0F, 12.0F, 32.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(92, 104)
						.addBox(6.0F, -68.0F, -8.0F, 2.0F, 32.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 68.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public ModelPart main() {
		return this.LifeSupport;
	}

	@Override
	protected ResourceLocation getTextureLocation() {
		return MachinaRL.create("textures/rocket/simple_rocket.png");
	}
}