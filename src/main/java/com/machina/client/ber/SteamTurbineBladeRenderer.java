package com.machina.client.ber;

import com.machina.api.util.MachinaRL;
import com.machina.block.entity.machine.steam_turbine.SteamTurbineBladeBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SteamTurbineBladeRenderer implements BlockEntityRenderer<SteamTurbineBladeBlockEntity> {

	private static final ResourceLocation TEXTURE = MachinaRL.create("textures/entity/blade.png");

	private final ModelPart rod;
	private final ModelPart blades;

	public SteamTurbineBladeRenderer(BlockEntityRendererProvider.Context ctx) {
		ModelPart root = createModel().bakeRoot();
		this.rod = root.getChild("rod");
		this.blades = root.getChild("blades");
	}

	// 16 units = 1 block in ModelPart coordinate system
	private static LayerDefinition createModel() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		// Central rod - 4x4 units wide, exactly 1 block height (-8 to +8 = 16 units)
		root.addOrReplaceChild("rod", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-2.0F, -8.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO);

		// Blades - no central hub, just the 4 blades extending outward
		// Each extends from 2 units out to 24 units out (0.125 to 1.5 blocks)
		PartDefinition bladesGroup = root.addOrReplaceChild("blades", CubeListBuilder.create(), PartPose.ZERO);

		bladesGroup.addOrReplaceChild("blade1", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-3.0F, -0.5F, 2.0F, 6.0F, 1.0F, 22.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO);

		bladesGroup.addOrReplaceChild("blade2", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-3.0F, -0.5F, 2.0F, 6.0F, 1.0F, 22.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, (float) Math.PI / 2, 0.0F));

		bladesGroup.addOrReplaceChild("blade3", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-3.0F, -0.5F, 2.0F, 6.0F, 1.0F, 22.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, (float) Math.PI, 0.0F));

		bladesGroup.addOrReplaceChild("blade4", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-3.0F, -0.5F, 2.0F, 6.0F, 1.0F, 22.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, (float) (3 * Math.PI / 2), 0.0F));

		return LayerDefinition.create(mesh, 16, 16);
	}

	@Override
	public void render(SteamTurbineBladeBlockEntity be, float partialTick, PoseStack pose,
			MultiBufferSource buffer, int packedLight, int packedOverlay) {

		float rotation = be.getRotation() + be.getRotationSpeed() * partialTick;

		VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

		pose.pushPose();
		pose.translate(0.5D, 0.5D, 0.5D);

		// Render rod (stationary)
		rod.render(pose, vc, packedLight, packedOverlay);

		// Render blades (rotating)
		pose.mulPose(Axis.YP.rotationDegrees(rotation));
		blades.render(pose, vc, packedLight, packedOverlay);

		pose.popPose();
	}

	@Override
	public boolean shouldRenderOffScreen(SteamTurbineBladeBlockEntity be) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 256;
	}
}
