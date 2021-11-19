package com.cy4.machina.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;

public class MachinaRegistries {
	
	public static final Map<String, List<Item>> ITEMS = new HashMap<>();
	public static final Map<String, List<Block>> BLOCKS = new HashMap<>();
	public static final Map<String, List<Fluid>> FLUIDS = new HashMap<>();
	
	public static final Map<String, List<Effect>> EFFECTS = new HashMap<>();
	public static final Map<String, List<PlanetTrait>> PLANET_TRAITS = new HashMap<>();
	public static final Map<String, List<AdvancedCraftingFunctionSerializer>> ADVANCED_CRAFTING_FUNCTION_SERIALIZERS = new HashMap<>();
	
	public static final Map<String, List<TileEntityType<?>>> TILE_ENTITY_TYPES = new HashMap<>();
	public static final Map<String, List<ContainerType<?>>> CONTAINER_TYPES = new HashMap<>();
	public static final Map<String, List<ParticleType<?>>> PARTICLE_TYPES = new HashMap<>();
	
	public static final Map<String, List<IRecipeType<?>>> RECIPE_TYPES = new HashMap<>();
	public static final Map<String, List<IRecipeSerializer<?>>> RECIPE_SERIALIZERS = new HashMap<>();

}
