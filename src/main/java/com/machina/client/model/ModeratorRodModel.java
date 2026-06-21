package com.machina.client.model;

import com.machina.api.util.MachinaRL;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

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

public class ModeratorRodModel extends Model {

	private static final ResourceLocation TEXTURE = MachinaRL.create("textures/entity/moderator_rod.png");

	private final ModelPart core;

	public ModeratorRodModel() {
		super(RenderType::entityCutoutNoCull);
		ModelPart root = this.createBodyLayer().bakeRoot();
		this.core = root.getChild("core");
	}

	public LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -10.0F, -2.0F,
				4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
			int color) {
		this.core.render(poseStack, buffer, packedLight, packedOverlay);
	}

	public void render(PoseStack poseStack, MultiBufferSource source, int packedLight, int packedOverlay, int color) {
		this.renderToBuffer(poseStack, source.getBuffer(renderType(TEXTURE)), packedLight, packedOverlay, color);
	}
}