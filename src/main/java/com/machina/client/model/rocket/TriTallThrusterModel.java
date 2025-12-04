package com.machina.client.model.rocket;

import com.machina.api.client.model.rocket.RocketPartModel;
import com.machina.api.util.MachinaRL;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class TriTallThrusterModel extends RocketPartModel {
    private final ModelPart thrusters;

    public TriTallThrusterModel() {
        ModelPart root = this.createBodyLayer().bakeRoot();
        this.thrusters = root.getChild("thrusters");
    }

    @Override
    public LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("thrusters",
                CubeListBuilder.create().texOffs(80, 87)
                        .addBox(6.0F, 0.0F, -11.0F, 6.0F, 26.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(80, 87)
                        .addBox(-12.0F, 0.0F, -11.0F, 6.0F, 26.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(80, 87)
                        .addBox(-3.0F, 0.0F, 6.0F, 6.0F, 26.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(100, 0)
                        .addBox(-2.5F, 14.0F, -6.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(100, 0)
                        .addBox(-6.0F, 14.0F, 0.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(100, 0)
                        .addBox(1.0F, 14.0F, 0.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart main() {
        return this.thrusters;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return new MachinaRL("textures/rocket/tri_rocket.png");
    }
}