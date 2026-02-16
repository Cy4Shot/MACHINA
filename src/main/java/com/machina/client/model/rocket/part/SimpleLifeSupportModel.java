package com.machina.client.model.rocket.part;

import com.machina.api.util.MachinaRL;
import com.machina.client.model.rocket.RocketPartModel;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SimpleLifeSupportModel extends RocketPartModel {
    private final ModelPart LifeSupport;

    public SimpleLifeSupportModel() {
        ModelPart root = this.createBodyLayer().bakeRoot();
        this.LifeSupport = root.getChild("LifeSupport");
    }

    @SuppressWarnings("unused")
    @Override
    public LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition LifeSupport = partdefinition.addOrReplaceChild("LifeSupport",
                CubeListBuilder.create().texOffs(0, 52)
                        .addBox(-16.0F, 0.0F, -16.0F, 32.0F, 4.0F, 32.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                        .addBox(-24.0F, 44.0F, -24.0F, 48.0F, 4.0F, 48.0F, new CubeDeformation(0.0F)).texOffs(0, 88)
                        .addBox(12.0F, 4.0F, -12.0F, 4.0F, 40.0F, 24.0F, new CubeDeformation(0.0F)).texOffs(56, 88)
                        .addBox(-16.0F, 4.0F, -12.0F, 4.0F, 40.0F, 24.0F, new CubeDeformation(0.0F)).texOffs(112, 88)
                        .addBox(-16.0F, 4.0F, -16.0F, 32.0F, 40.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(112, 132)
                        .addBox(-16.0F, 4.0F, 12.0F, 32.0F, 40.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

        PartDefinition Corners = LifeSupport.addOrReplaceChild("Corners", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition Corner_r1 = Corners.addOrReplaceChild("Corner_r1",
                CubeListBuilder.create().texOffs(48, 152).addBox(-3.0F, -34.0F, -44.0F, 4.0F, 44.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-20.0F, 44.0F, 22.0F, -0.1745F, 0.0F, 0.1745F));

        PartDefinition Corner_r2 = Corners.addOrReplaceChild("Corner_r2",
                CubeListBuilder.create().texOffs(32, 152).addBox(-3.0F, -43.0F, -3.0F, 4.0F, 44.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-20.0F, 44.0F, 22.0F, 0.1745F, 0.0F, 0.1745F));

        PartDefinition Corner_r3 = Corners.addOrReplaceChild("Corner_r3",
                CubeListBuilder.create().texOffs(16, 152).addBox(-3.0F, -34.0F, -44.0F, 4.0F, 44.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(22.0F, 44.0F, 22.0F, -0.1745F, 0.0F, -0.1745F));

        PartDefinition Corner_r4 = Corners.addOrReplaceChild("Corner_r4",
                CubeListBuilder.create().texOffs(0, 152).addBox(-3.0F, -43.0F, -3.0F, 4.0F, 44.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(22.0F, 44.0F, 22.0F, 0.1745F, 0.0F, -0.1745F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public ModelPart main() {
        return this.LifeSupport;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return MachinaRL.create("textures/rocket/simple_life_support.png");
    }
}