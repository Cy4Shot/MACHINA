package com.cy4.machina.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cy4.machina.api.planet.trait.PlanetTrait;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;

public class MachinaRegistries {
	
	public static final Map<String, List<Item>> ITEMS = new HashMap<>();
	public static final Map<String, List<Block>> BLOCKS = new HashMap<>();
	public static final Map<String, List<Fluid>> FLUIDS = new HashMap<>();
	public static final Map<String, List<Effect>> EFFECTS = new HashMap<>();
	public static final Map<String, List<PlanetTrait>> PLANET_TRAITS = new HashMap<>();
	public static final Map<String, List<TileEntityType<?>>> TILE_ENTITY_TYPES = new HashMap<>();
	public static final Map<String, List<ContainerType<?>>> CONTAINER_TYPES = new HashMap<>();
	public static final Map<String, List<ParticleType<?>>> PARTICLE_TYPES = new HashMap<>();

}
