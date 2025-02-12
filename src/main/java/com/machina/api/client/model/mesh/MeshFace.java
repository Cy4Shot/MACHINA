package com.machina.api.client.model.mesh;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MeshFace {
	public final Direction cullForDirection;
	public final Material texture;
	public final BlockFaceUV uv;
	@Nullable
	FacedMesh parent;

	public MeshFace(@Nullable Direction d, Material tex, BlockFaceUV uv) {
		this.cullForDirection = d;
		this.texture = tex;
		this.uv = uv;
	}
}