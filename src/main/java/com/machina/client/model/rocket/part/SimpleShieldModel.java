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

public class SimpleShieldModel extends RocketPartModel {
	private final ModelPart Shields;

	public SimpleShieldModel() {
		ModelPart root = this.createBodyLayer().bakeRoot();
		this.Shields = root.getChild("Shields");
	}

	@Override
	public LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("Shields",
				CubeListBuilder.create().texOffs(64, 56)
						.addBox(-3.0F, -12.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(64, 0)
						.addBox(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(64, 18)
						.addBox(-7.0F, -4.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(92, 78)
						.addBox(-6.0F, -6.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(92, 92)
						.addBox(-5.0F, -8.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(16, 108)
						.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public ModelPart main() {
		return this.Shields;
	}

	@Override
	protected ResourceLocation getTextureLocation() {
		return MachinaRL.create("textures/rocket/simple_rocket.png");
	}
}