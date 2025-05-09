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

public class ConeShieldModel extends RocketPartModel {
	private final ModelPart Shields;

	public ConeShieldModel() {
		ModelPart root = this.createBodyLayer().bakeRoot();
		this.Shields = root.getChild("Shields");
	}

	@SuppressWarnings("unused")
	@Override
	public LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Shields = partdefinition.addOrReplaceChild("Shields",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-14.0F, -136.0F, -14.0F, 28.0F, 4.0F, 28.0F, new CubeDeformation(0.0F)).texOffs(0, 32)
						.addBox(-12.0F, -140.0F, -12.0F, 24.0F, 4.0F, 24.0F, new CubeDeformation(0.0F)).texOffs(0, 60)
						.addBox(-10.0F, -144.0F, -10.0F, 20.0F, 4.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(80, 60)
						.addBox(-8.0F, -148.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 156.0F, 0.0F));

		PartDefinition Corners2 = Shields.addOrReplaceChild("Corners2", CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Corner_r1 = Corners2.addOrReplaceChild("Corner_r1",
				CubeListBuilder.create().texOffs(32, 84).addBox(0.0F, -18.0F, -4.0F, 4.0F, 19.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-14.0F, -132.0F, 14.0F, 0.3927F, 0.0F, 0.3927F));

		PartDefinition Corner_r2 = Corners2.addOrReplaceChild("Corner_r2",
				CubeListBuilder.create().texOffs(16, 84).addBox(0.0F, -18.0F, 0.0F, 4.0F, 19.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-14.0F, -132.0F, -14.0F, -0.3927F, 0.0F, 0.3927F));

		PartDefinition Corner_r3 = Corners2.addOrReplaceChild("Corner_r3",
				CubeListBuilder.create().texOffs(0, 84).addBox(-4.0F, -18.0F, 0.0F, 4.0F, 20.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(14.0F, -132.0F, -14.0F, -0.3927F, 0.0F, -0.3927F));

		PartDefinition Corner_r4 = Corners2.addOrReplaceChild("Corner_r4",
				CubeListBuilder.create().texOffs(80, 80).addBox(-4.0F, -18.0F, -4.0F, 4.0F, 20.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(14.0F, -132.0F, 14.0F, 0.3927F, 0.0F, -0.3927F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	protected ModelPart main() {
		return this.Shields;
	}

	@Override
	protected ResourceLocation getTextureLocation() {
		return new MachinaRL("textures/rocket/simple_shield.png");
	}
}