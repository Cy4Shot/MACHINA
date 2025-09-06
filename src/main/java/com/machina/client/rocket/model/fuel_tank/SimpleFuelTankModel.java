package com.machina.client.rocket.model.fuel_tank;

import com.machina.api.util.MachinaRL;
import com.machina.client.rocket.model.RocketPartModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SimpleFuelTankModel extends RocketPartModel {
    private final ModelPart Reactor;

    public SimpleFuelTankModel() {
        ModelPart root = this.createBodyLayer().bakeRoot();
        this.Reactor = root.getChild("Reactor");
    }

    @Override
    public LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Reactor = partdefinition.addOrReplaceChild("Reactor", CubeListBuilder.create().texOffs(0, 192)
                .addBox(-12.0F, 12.0F, -12.0F, 24.0F, 24.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        PartDefinition Sides = Reactor.addOrReplaceChild("Sides",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-24.0F, 0F, -24.0F, 48.0F, 4.0F, 48.0F, new CubeDeformation(0.0F)).texOffs(0, 52)
                        .addBox(-24.0F, 44F, -24.0F, 48.0F, 4.0F, 48.0F, new CubeDeformation(0.0F)).texOffs(0, 104)
                        .addBox(20.0F, 4F, -24.0F, 4.0F, 40.0F, 48.0F, new CubeDeformation(0.0F)).texOffs(104, 104)
                        .addBox(-24.0F, 4F, -24.0F, 4.0F, 40.0F, 48.0F, new CubeDeformation(0.0F)).texOffs(192, 0)
                        .addBox(-20.0F, 4F, -24.0F, 40.0F, 40.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(192, 44)
                        .addBox(-20.0F, 4F, 20.0F, 40.0F, 40.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

        PartDefinition Corner1 = Sides.addOrReplaceChild("Corner1", CubeListBuilder.create(), PartPose.ZERO);

        Corner1.addOrReplaceChild("Angle2_r1",
                CubeListBuilder.create().texOffs(112, 192).addBox(-2.0F, -22.0F, -37.0F, 4.0F, 30.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(22.0F, 44F, 22.0F, -1.0036F, 0.7854F, 0.0F));

        Corner1.addOrReplaceChild("Angle1_r1",
                CubeListBuilder.create().texOffs(96, 192).addBox(-2.0F, -30.0F, -3.0F, 4.0F, 30.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(22.0F, 44F, 22.0F, 1.0036F, 0.7854F, 0.0F));

        PartDefinition Corner2 = Sides.addOrReplaceChild("Corner2", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        Corner2.addOrReplaceChild("Angle2_r2",
                CubeListBuilder.create().texOffs(144, 192).addBox(-2.0F, -22.0F, -37.0F, 4.0F, 30.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(22.0F, 44F, 22.0F, -1.0036F, 0.7854F, 0.0F));

        Corner2.addOrReplaceChild("Angle1_r2",
                CubeListBuilder.create().texOffs(128, 192).addBox(-2.0F, -30.0F, -3.0F, 4.0F, 30.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(22.0F, 44F, 22.0F, 1.0036F, 0.7854F, 0.0F));

        PartDefinition Corner3 = Sides.addOrReplaceChild("Corner3", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        Corner3.addOrReplaceChild("Angle2_r3",
                CubeListBuilder.create().texOffs(176, 192).addBox(-2.0F, -22.0F, -37.0F, 4.0F, 30.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(22.0F, 44F, 22.0F, -1.0036F, 0.7854F, 0.0F));

        Corner3.addOrReplaceChild("Angle1_r3",
                CubeListBuilder.create().texOffs(160, 192).addBox(-2.0F, -30.0F, -3.0F, 4.0F, 30.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(22.0F, 44F, 22.0F, 1.0036F, 0.7854F, 0.0F));

        PartDefinition Corner4 = Sides.addOrReplaceChild("Corner4", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        Corner4.addOrReplaceChild("Angle2_r4",
                CubeListBuilder.create().texOffs(208, 88).addBox(-2.0F, -22.0F, -37.0F, 4.0F, 30.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(22.0F, 44F, 22.0F, -1.0036F, 0.7854F, 0.0F));

        Corner4.addOrReplaceChild("Angle1_r4",
                CubeListBuilder.create().texOffs(192, 192).addBox(-2.0F, -30.0F, -3.0F, 4.0F, 30.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(22.0F, 44F, 22.0F, 1.0036F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    protected ModelPart main() {
        return this.Reactor;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return new MachinaRL("textures/rocket/simple_fuel_tank.png");
    }
}