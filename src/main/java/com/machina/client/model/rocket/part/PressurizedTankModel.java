package com.machina.client.model.rocket.part;

import com.machina.api.util.MachinaRL;
import com.machina.client.model.rocket.RocketPartModel;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class PressurizedTankModel extends RocketPartModel {
    private final ModelPart core;

    public PressurizedTankModel() {
        ModelPart root = this.createBodyLayer().bakeRoot();
        this.core = root.getChild("core");
    }

    @Override
    public LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("core",
                CubeListBuilder.create().texOffs(48, 32)
                        .addBox(-8.0F, 0.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(0, 80)
                        .addBox(-6.0F, 3.0F, -6.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(48, 87)
                        .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(-0.001F)),
                PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart main() {
        return this.core;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return new MachinaRL("textures/rocket/tri_rocket.png");
    }
}