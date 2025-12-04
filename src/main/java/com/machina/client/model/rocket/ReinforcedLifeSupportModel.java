package com.machina.client.model.rocket;

import com.machina.api.client.model.rocket.RocketPartModel;
import com.machina.api.util.MachinaRL;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ReinforcedLifeSupportModel extends RocketPartModel {
    private final ModelPart lifesupport;

    public ReinforcedLifeSupportModel() {
        ModelPart root = this.createBodyLayer().bakeRoot();
        this.lifesupport = root.getChild("lifesupport");
    }

    public LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("lifesupport", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F,
                -8.0F, 16.0F, 32.0F, 16.0F, new CubeDeformation(-0.01F)), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart main() {
        return this.lifesupport;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return new MachinaRL("textures/rocket/tri_rocket.png");
    }
}