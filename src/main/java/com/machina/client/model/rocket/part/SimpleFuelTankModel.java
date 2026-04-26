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

public class SimpleFuelTankModel extends RocketPartModel {
	private final ModelPart Reactor;

	public SimpleFuelTankModel() {
		ModelPart root = this.createBodyLayer().bakeRoot();
		this.Reactor = root.getChild("Reactor");
	}

	@Override
	public LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("Reactor",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-7.0F, -2.0F, -29.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(64, 34)
						.addBox(-9.0F, 12.0F, -31.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(92, 56)
						.addBox(-9.0F, -2.0F, -31.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(48, 64)
						.addBox(9.0F, 12.0F, -31.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(48, 86)
						.addBox(9.0F, -2.0F, -31.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(108, 34)
						.addBox(-7.0F, 12.0F, -31.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(108, 42)
						.addBox(-7.0F, -2.0F, -31.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(108, 38)
						.addBox(-7.0F, 12.0F, -13.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(108, 46)
						.addBox(-7.0F, -2.0F, -13.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-1.0F, 2.0F, 21.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public ModelPart main() {
		return this.Reactor;
	}

	@Override
	protected ResourceLocation getTextureLocation() {
		return MachinaRL.create("textures/rocket/simple_rocket.png");
	}
}