package com.machina.datagen.client.builder;

import com.machina.api.util.MachinaRL;

import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BEWLRModelLoaderBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	public BEWLRModelLoaderBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(MachinaRL.create("bewlr"), parent, existingFileHelper, true);
	}

}
