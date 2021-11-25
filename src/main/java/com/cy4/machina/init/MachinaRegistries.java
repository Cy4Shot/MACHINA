/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cy4.machina.api.planet.attribute.PlanetAttributeType;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;

/**
 * The maps stored in here contain all the objects (except {@link BlockItem}s)
 * that are registered using registry annotations. The data is stored as it
 * follows: <br>
 * A {@link List} with the generic type {@code <T>} will be put as the value
 * corresponding to the key which is the namespace (mod id) of the object's
 * registry name. {@code <T>} is the type of the object registered.
 *
 * @author matyrobbrt
 *
 */
public final class MachinaRegistries {

	public static final Map<String, List<Item>> ITEMS = new HashMap<>();
	public static final Map<String, List<Block>> BLOCKS = new HashMap<>();
	public static final Map<String, List<Fluid>> FLUIDS = new HashMap<>();

	public static final Map<String, List<Effect>> EFFECTS = new HashMap<>();
	public static final Map<String, List<PlanetTrait>> PLANET_TRAITS = new HashMap<>();
	public static final Map<String, List<PlanetAttributeType<?>>> PLANET_ATTRIBUTE_TYPES = new HashMap<>();
	public static final Map<String, List<SoundEvent>> SOUND_EVENTS = new HashMap<>();
	public static final Map<String, List<AdvancedCraftingFunctionSerializer<?>>> ADVANCED_CRAFTING_FUNCTION_SERIALIZERS = new HashMap<>();

	public static final Map<String, List<TileEntityType<?>>> TILE_ENTITY_TYPES = new HashMap<>();
	public static final Map<String, List<ContainerType<?>>> CONTAINER_TYPES = new HashMap<>();
	public static final Map<String, List<ParticleType<?>>> PARTICLE_TYPES = new HashMap<>();

	public static final Map<String, List<IRecipeType<?>>> RECIPE_TYPES = new HashMap<>();
	public static final Map<String, List<IRecipeSerializer<?>>> RECIPE_SERIALIZERS = new HashMap<>();

}
