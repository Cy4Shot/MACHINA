package com.machina.client.model;

import com.machina.api.util.MachinaRL;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class BladeModel extends Model {

	private static final ResourceLocation TEXTURE = MachinaRL.create("textures/entity/blade.png");

	private final ModelPart rod;
	private final ModelPart hub;
	private final ModelPart blade1;
	private final ModelPart blade2;
	private final ModelPart blade3;
	private final ModelPart blade4;

	public BladeModel() {
		super(RenderType::entityTranslucentCull);
		ModelPart root = this.createBodyLayer().bakeRoot();
		this.rod = root.getChild("rod");
		this.hub = root.getChild("hub");
		this.blade1 = root.getChild("blade1");
		this.blade2 = root.getChild("blade2");
		this.blade3 = root.getChild("blade3");
		this.blade4 = root.getChild("blade4");
	}

	public LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		// Central rod - thin shaft extending slightly beyond block for visual connection
		partdefinition.addOrReplaceChild("rod", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		// Smaller hub where blades attach
		partdefinition.addOrReplaceChild("hub", CubeListBuilder.create()
				.texOffs(8, 0)
				.addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		// Blades - extend ~1.4 blocks from center, thinner profile
		partdefinition.addOrReplaceChild("blade1", CubeListBuilder.create()
				.texOffs(0, 20)
				.addBox(-1.5F, -0.5F, 2.0F, 3.0F, 1.0F, 22.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("blade2", CubeListBuilder.create()
				.texOffs(0, 20)
				.addBox(-1.5F, -0.5F, 2.0F, 3.0F, 1.0F, 22.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, (float) Math.PI / 2, 0.0F));

		partdefinition.addOrReplaceChild("blade3", CubeListBuilder.create()
				.texOffs(0, 20)
				.addBox(-1.5F, -0.5F, 2.0F, 3.0F, 1.0F, 22.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, (float) Math.PI, 0.0F));

		partdefinition.addOrReplaceChild("blade4", CubeListBuilder.create()
				.texOffs(0, 20)
				.addBox(-1.5F, -0.5F, 2.0F, 3.0F, 1.0F, 22.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, (float) (3 * Math.PI / 2), 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
			int color) {
		this.rod.render(poseStack, buffer, packedLight, packedOverlay);
		this.hub.render(poseStack, buffer, packedLight, packedOverlay);
		this.blade1.render(poseStack, buffer, packedLight, packedOverlay);
		this.blade2.render(poseStack, buffer, packedLight, packedOverlay);
		this.blade3.render(poseStack, buffer, packedLight, packedOverlay);
		this.blade4.render(poseStack, buffer, packedLight, packedOverlay);
	}

	public void render(PoseStack poseStack, MultiBufferSource source, int packedLight, int packedOverlay, int color, float rotation) {
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
		this.renderToBuffer(poseStack, source.getBuffer(renderType(TEXTURE)), packedLight, packedOverlay, color);
		poseStack.popPose();
	}
}
