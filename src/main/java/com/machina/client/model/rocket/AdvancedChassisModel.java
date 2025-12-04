package com.machina.client.model.rocket;

import com.machina.api.client.model.rocket.RocketPartModel;
import com.machina.api.util.MachinaRL;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class AdvancedChassisModel extends RocketPartModel {
    private final ModelPart reactor;

    public AdvancedChassisModel() {
        ModelPart root = this.createBodyLayer().bakeRoot();
        this.reactor = root.getChild("reactor");
    }

    @Override
    public LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition reactor = partdefinition.addOrReplaceChild("reactor",
                CubeListBuilder.create().texOffs(0, 48)
                        .addBox(-8.0F, 0.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-0.01F)).texOffs(65, 1)
                        .addBox(-5.0F, 1.0F, -8.0F, 10.0F, 14.0F, 15.0F, new CubeDeformation(-0.01F)).texOffs(0, 0)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(96, 30)
                        .addBox(-3.0F, 4.0F, -3.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(36, 80)
                        .addBox(-6.0F, 7.0F, 0.0F, 12.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(48, 0)
                        .addBox(-4.0F, 9.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

        reactor.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 80).addBox(-6.0F, 7.0F, 0.0F, 12.0F,
                        6.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.6144F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart main() {
        return this.reactor;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return new MachinaRL("textures/rocket/tri_rocket.png");
    }
}