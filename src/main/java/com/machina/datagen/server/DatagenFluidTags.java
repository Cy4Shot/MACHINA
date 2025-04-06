package com.machina.datagen.server;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.machina.Machina;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DatagenFluidTags extends FluidTagsProvider {
	public DatagenFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
			@Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Machina.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(@NotNull Provider pProvider) {
		FluidInit.OBJS.forEach(this::fluidObj);
	}

	private void fluidObj(FluidObject obj) {
		tag(common(obj.name())).add(obj.fluid(), obj.flowing());
	}

	private static TagKey<Fluid> common(String name) {
		return TagKey.create(Registries.FLUID, new ResourceLocation("c", name));
	}
}