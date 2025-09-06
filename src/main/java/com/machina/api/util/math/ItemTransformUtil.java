package com.machina.api.util.math;

import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import org.joml.Vector3f;

public class ItemTransformUtil {

    private static final Vector3f ZERO = new Vector3f(0, 0, 0);

    private static Vector3f s(float s) {
        return new Vector3f(s, s, s);
    }

    //@formatter:off
	public static final ItemTransform BLOCK_GUI = new ItemTransform(new Vector3f(30, 225, 0), ZERO, s(0.625f));
	public static final ItemTransform BLOCK_GROUND = new ItemTransform(ZERO, ZERO, s(0.25f));
	public static final ItemTransform BLOCK_FIXED = new ItemTransform(ZERO, ZERO, s(0.5f));
	public static final ItemTransform BLOCK_TP_RH = new ItemTransform(new Vector3f(75, 45, 0), new Vector3f(0, 0, 2.5f / 16f), s(0.375f));
	public static final ItemTransform BLOCK_TP_LH = new ItemTransform(new Vector3f(75, 225, 0), new Vector3f(0, 0, 2.5f / 16f), s(0.375f));
	public static final ItemTransform BLOCK_FP_RH = new ItemTransform(new Vector3f(0, 45, 0), ZERO, s(0.4f));
	public static final ItemTransform BLOCK_FP_LH = new ItemTransform(new Vector3f(0, 225, 0), ZERO, s(0.4f));
	
	public static final ItemTransforms BLOCK = new ItemTransforms(
            BLOCK_TP_LH, BLOCK_TP_RH, BLOCK_FP_LH, BLOCK_FP_RH,
            ItemTransform.NO_TRANSFORM,
            BLOCK_GUI, BLOCK_GROUND, BLOCK_FIXED);
	//@formatter:on

}
