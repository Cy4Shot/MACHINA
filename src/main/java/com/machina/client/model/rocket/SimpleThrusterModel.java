package com.machina.client.model.rocket;

import com.machina.api.client.model.rocket.RocketPartModel;
import com.machina.api.util.MachinaRL;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SimpleThrusterModel extends RocketPartModel {
    private final ModelPart Thrusters;

    public SimpleThrusterModel() {
        ModelPart root = this.createBodyLayer().bakeRoot();
        this.Thrusters = root.getChild("Thrusters");
    }

    @SuppressWarnings("unused")
    @Override
    public LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Thrusters = partdefinition.addOrReplaceChild("Thrusters",
                CubeListBuilder.create().texOffs(96, 152), PartPose.ZERO);

        PartDefinition Thruster1 = Thrusters.addOrReplaceChild("Thruster1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-22.0F, 0f, 2.0F, 20.0F, 7.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(0, 27)
                        .addBox(-22.0F, 0f, 2.0F, 20.0F, 7.0F, 20.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

        PartDefinition SmallThruster = Thruster1.addOrReplaceChild("SmallThruster",
                CubeListBuilder.create().texOffs(0, 108)
                        .addBox(-19.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(24, 108)
                        .addBox(-19.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(48, 108)
                        .addBox(-11.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(72, 108)
                        .addBox(-11.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(96, 108)
                        .addBox(-19.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 119)
                        .addBox(-19.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(24, 119)
                        .addBox(-11.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(48, 119)
                        .addBox(-11.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition Thruster2 = Thrusters.addOrReplaceChild("Thruster2",
                CubeListBuilder.create().texOffs(0, 54)
                        .addBox(-22.0F, 0f, 2.0F, 20.0F, 7.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(80, 0)
                        .addBox(-22.0F, 0f, 2.0F, 20.0F, 7.0F, 20.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -24.0F));

        PartDefinition SmallThruster2 = Thruster2.addOrReplaceChild("SmallThruster2",
                CubeListBuilder.create().texOffs(72, 119)
                        .addBox(-19.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(96, 119)
                        .addBox(-19.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(120, 108)
                        .addBox(-11.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(120, 119)
                        .addBox(-11.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 130)
                        .addBox(-19.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(24, 130)
                        .addBox(-19.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(48, 130)
                        .addBox(-11.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(72, 130)
                        .addBox(-11.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition Thruster3 = Thrusters.addOrReplaceChild("Thruster3",
                CubeListBuilder.create().texOffs(80, 27)
                        .addBox(2.0F, 0f, 2.0F, 20.0F, 7.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(80, 54)
                        .addBox(2.0F, 0f, 2.0F, 20.0F, 7.0F, 20.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

        PartDefinition SmallThruster3 = Thruster3.addOrReplaceChild("SmallThruster3",
                CubeListBuilder.create().texOffs(96, 130)
                        .addBox(5.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(120, 130)
                        .addBox(5.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 141)
                        .addBox(13.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(24, 141)
                        .addBox(13.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(48, 141)
                        .addBox(5.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(72, 141)
                        .addBox(5.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(96, 141)
                        .addBox(13.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(120, 141)
                        .addBox(13.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition Thruster4 = Thrusters.addOrReplaceChild("Thruster4",
                CubeListBuilder.create().texOffs(0, 81)
                        .addBox(2.0F, 0f, 2.0F, 20.0F, 7.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(80, 81)
                        .addBox(2.0F, 0f, 2.0F, 20.0F, 7.0F, 20.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -24.0F));

        PartDefinition SmallThruster4 = Thruster4.addOrReplaceChild("SmallThruster4",
                CubeListBuilder.create().texOffs(144, 108)
                        .addBox(5.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(144, 119)
                        .addBox(5.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(144, 130)
                        .addBox(13.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(144, 141)
                        .addBox(13.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 152)
                        .addBox(5.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(24, 152)
                        .addBox(5.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(48, 152)
                        .addBox(13.0F, 4F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(72, 152)
                        .addBox(13.0F, 4F, 13.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public ModelPart main() {
        return this.Thrusters;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return new MachinaRL("textures/rocket/simple_thruster.png");
    }
}