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

public class SimpleThrusterModel extends RocketPartModel {
	private final ModelPart Thrusters;

	public SimpleThrusterModel() {
		ModelPart root = this.createBodyLayer().bakeRoot();
		this.Thrusters = root.getChild("Thrusters");
	}

	@Override
	public LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("Thrusters",
				CubeListBuilder.create().texOffs(48, 108)
						.addBox(0.0F, -12.0F, 0.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(72, 108)
						.addBox(7.0F, -12.0F, -5.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(108, 104)
						.addBox(-5.0F, -12.0F, -5.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(108, 117)
						.addBox(-5.0F, -12.0F, 7.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(16, 118)
						.addBox(7.0F, -12.0F, 7.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(32, 118)
						.addBox(6.0F, -12.0F, 1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(48, 118)
						.addBox(1.0F, -12.0F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(120, 18)
						.addBox(1.0F, -12.0F, 6.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(64, 121)
						.addBox(-4.0F, -12.0F, 1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-3.0F, 12.0F, -3.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public ModelPart main() {
		return this.Thrusters;
	}

	@Override
	protected ResourceLocation getTextureLocation() {
		return MachinaRL.create("textures/rocket/simple_rocket.png");
	}
}