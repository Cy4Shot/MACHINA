package com.machina.client.model.rocket;

import com.machina.api.client.model.rocket.RocketPartModel;
import com.machina.api.util.MachinaRL;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SimpleChassisModel extends RocketPartModel {
    private final ModelPart Core;

    public SimpleChassisModel() {
        ModelPart root = this.createBodyLayer().bakeRoot();
        this.Core = root.getChild("Core");
    }

    @Override
    public LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("Core",
                CubeListBuilder.create().texOffs(0, 36)
                        .addBox(-24.0F, 0.0F, 16.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(32, 36)
                        .addBox(-24.0F, 0.0F, -24.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(48, 0)
                        .addBox(16.0F, 0.0F, -24.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(64, 32)
                        .addBox(16.0F, 0.0F, 16.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                        .addBox(-6.0F, 0.0F, -6.0F, 12.0F, 24.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(48, 32),
                PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart main() {
        return this.Core;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return new MachinaRL("textures/rocket/simple_chassis.png");
    }
}