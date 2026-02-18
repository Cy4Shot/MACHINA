package com.machina.api.multiblock;

import com.machina.api.multiblock.Multiblock.MultiblockJsonInfo;
import com.machina.api.util.loader.JsonLoader;

public class MultiblockLoader extends JsonLoader<Multiblock> {

	public static final MultiblockLoader INSTANCE = new MultiblockLoader();

	public MultiblockLoader() {
		super("multiblock", MultiblockJsonInfo.class);
	}
}