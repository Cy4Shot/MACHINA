package com.machina.datagen.client.builder;

import com.machina.api.util.MachinaRL;

import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BEWLRModelLoaderBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	public BEWLRModelLoaderBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(new MachinaRL("bewlr"), parent, existingFileHelper);
	}

}
