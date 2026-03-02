package com.machina.datagen.server;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.machina.Machina;
import com.machina.datagen.server.base.DatagenRecipeProvider;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.OreFamily;
import com.machina.registration.init.FamiliesInit.StoneFamily;
import com.machina.registration.init.FamiliesInit.WoodFamily;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RocketPartInit;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

public class DatagenRecipes extends DatagenRecipeProvider implements IConditionBuilder {

	public DatagenRecipes(PackOutput po, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(po, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput gen) {
		ore(gen, List.of(BlockInit.ANTHRACITE), ItemInit.COAL_CHUNK, 0.05f, 40, "anthracite");

		//@formatter:off
		
		// Resources (TEMPORARY)
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.CONSTANTAN_DUST)
			.requires(ItemInit.COPPER_DUST)
			.requires(ItemInit.NICKEL_DUST)
			.unlockedBy(getHasName(ItemInit.COPPER_DUST), has(ItemInit.COPPER_DUST))
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(ItemInit.CONSTANTAN_DUST));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.STEEL_DUST)
			.requires(ItemInit.IRON_DUST)
			.requires(ItemInit.COAL_DUST)
			.unlockedBy(getHasName(ItemInit.IRON_DUST), has(ItemInit.IRON_DUST))
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(ItemInit.STEEL_DUST));
		
		// Building blocks
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockInit.ANTHRACITE)
			.requires(Blocks.STONE)
			.requires(ItemInit.COAL_CHUNK)
			.unlockedBy(getHasName(ItemInit.COAL_CHUNK), has(ItemInit.COAL_CHUNK))
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ANTHRACITE));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockInit.MIGMATITE)
			.pattern("SG")
			.pattern("GS")
			.define('S', Blocks.STONE)
			.define('G', Blocks.GRANITE)
			.unlockedBy(getHasName(Blocks.GRANITE), has(Blocks.GRANITE))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.MIGMATITE));
		
		// Miscellaneous Crafting Items
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.COPPER_COIL)
			.pattern("NNN")
			.pattern("NSN")
			.pattern("NNN")
			.define('S', Items.STICK)
			.define('N', ItemInit.COPPER_NUGGET)
			.unlockedBy(getHasName(ItemInit.COPPER_NUGGET), has(ItemInit.COPPER_NUGGET))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(ItemInit.COPPER_COIL));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.BASIC_CASING)
			.pattern("RLR")
			.pattern("IGI")
			.pattern("RLR")
			.define('G', Blocks.GLASS)
			.define('R', Items.REDSTONE)
			.define('I', Items.IRON_INGOT)
			.define('L', ItemInit.LEAD_INGOT)
			.unlockedBy(getHasName(ItemInit.LEAD_INGOT), has(ItemInit.LEAD_INGOT))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.BASIC_CASING));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.LIGHTWEIGHT_CASING)
			.pattern("RAR")
			.pattern("AGA")
			.pattern("RAR")
			.define('G', Blocks.GLASS)
			.define('R', Items.REDSTONE)
			.define('A', ItemInit.ALUMINUM_INGOT)
			.unlockedBy(getHasName(ItemInit.ALUMINUM_INGOT), has(ItemInit.ALUMINUM_INGOT))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.LIGHTWEIGHT_CASING));
		
		// Capacitors
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.BASIC_CAPACITOR)
			.pattern(" P ")
			.pattern("ARA")
			.pattern("III")
			.define('P', Items.PAPER)
			.define('R', Items.REDSTONE)
			.define('I', Items.IRON_NUGGET)
			.define('A', ItemInit.ALUMINUM_INGOT)
			.unlockedBy(getHasName(ItemInit.ALUMINUM_INGOT), has(ItemInit.ALUMINUM_INGOT))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(ItemInit.BASIC_CAPACITOR));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.ADVANCED_CAPACITOR)
			.pattern(" I ")
			.pattern("BCB")
			.pattern(" I ")
			.define('C', ItemInit.COPPER_COIL)
			.define('B', ItemInit.BASIC_CAPACITOR)
			.define('I', ItemInit.CONSTANTAN_INGOT)
			.unlockedBy(getHasName(ItemInit.CONSTANTAN_INGOT), has(ItemInit.CONSTANTAN_INGOT))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(ItemInit.ADVANCED_CAPACITOR));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.SUPREME_CAPACITOR)
			.pattern(" I ")
			.pattern("ACA")
			.pattern(" I ")
			.define('C', ItemInit.COPPER_COIL)
			.define('A', ItemInit.ADVANCED_CAPACITOR)
			.define('I', ItemInit.PALLADIUM_INGOT)
			.unlockedBy(getHasName(ItemInit.PALLADIUM_INGOT), has(ItemInit.PALLADIUM_INGOT))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(ItemInit.SUPREME_CAPACITOR));
		
		// Cables
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ENERGY_CABLE, 8)
			.pattern("ITI")
			.define('T', Items.REDSTONE)
			.define('I', ItemInit.LEAD_INGOT)
			.unlockedBy(getHasName(ItemInit.LEAD_INGOT), has(ItemInit.LEAD_INGOT))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ENERGY_CABLE));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ITEM_CONDUIT, 8)
			.pattern("ITI")
			.define('T', Items.HOPPER)
			.define('I', ItemInit.ALUMINUM_INGOT)
			.unlockedBy(getHasName(ItemInit.ALUMINUM_INGOT), has(ItemInit.ALUMINUM_INGOT))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ITEM_CONDUIT));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.FLUID_PIPE, 8)
			.pattern("ITI")
			.define('T', Items.BUCKET)
			.define('I', Items.IRON_INGOT)
			.unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.FLUID_PIPE));
		
		// Machine
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.TANK)
			.pattern("RAR")
			.pattern("AGA")
			.pattern("RAR")
			.define('G', Blocks.GLASS)
			.define('R', Items.REDSTONE)
			.define('A', Items.IRON_INGOT)
			.unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.TANK));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.FURNACE_GENERATOR)
			.pattern("RYR")
			.pattern("XCX")
			.pattern("IZI")
			.define('X', ItemInit.COPPER_COIL)
			.define('Y', Blocks.FURNACE)
			.define('Z', ItemInit.BASIC_CAPACITOR)
			.define('I', ItemInit.ALUMINUM_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.LIGHTWEIGHT_CASING)
			.unlockedBy(getHasName(BlockInit.LIGHTWEIGHT_CASING), has(BlockInit.LIGHTWEIGHT_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.FURNACE_GENERATOR));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.CHEMICAL_GENERATOR)
			.pattern("RYR")
			.pattern("XCX")
			.pattern("IZI")
			.define('X', Items.BUCKET)
			.define('Y', ItemInit.COPPER_COIL)
			.define('Z', ItemInit.BASIC_CAPACITOR)
			.define('I', ItemInit.ALUMINUM_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.LIGHTWEIGHT_CASING)
			.unlockedBy(getHasName(BlockInit.LIGHTWEIGHT_CASING), has(BlockInit.LIGHTWEIGHT_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.CHEMICAL_GENERATOR));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.COMPOSTER_VAT)
			.pattern("RYR")
			.pattern("XCX")
			.pattern("IZI")
			.define('X', Blocks.COMPOSTER)
			.define('Y', Items.BUCKET)
			.define('Z', BlockInit.TANK)
			.define('I', ItemInit.ALUMINUM_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.LIGHTWEIGHT_CASING)
			.unlockedBy(getHasName(BlockInit.LIGHTWEIGHT_CASING), has(BlockInit.LIGHTWEIGHT_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.COMPOSTER_VAT));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ATMOSPHERIC_SEPARATOR)
			.pattern("RPR")
			.pattern("XCX")
			.pattern("IZI")
			.define('X', ItemInit.DIAMOND_DUST)
			.define('P', ItemInit.SILVER_PLATE)
			.define('Z', BlockInit.TANK)
			.define('I', ItemInit.ALUMINUM_ROD)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.LIGHTWEIGHT_CASING)
			.unlockedBy(getHasName(BlockInit.LIGHTWEIGHT_CASING), has(BlockInit.LIGHTWEIGHT_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ATMOSPHERIC_SEPARATOR));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ELECTRIC_SMELTER)
			.pattern("RYR")
			.pattern("XCX")
			.pattern("IZI")
			.define('X', ItemInit.COPPER_COIL)
			.define('Y', Blocks.FURNACE)
			.define('Z', Blocks.REDSTONE_BLOCK)
			.define('I', ItemInit.LEAD_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ELECTRIC_SMELTER));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.GRINDER)
			.pattern("RYR")
			.pattern("XCW")
			.pattern("IZI")
			.define('W', BlockInit.LEAD_BLOCK)
			.define('X', Blocks.IRON_BLOCK)
			.define('Y', Items.IRON_PICKAXE)
			.define('Z', ItemInit.COPPER_COIL)
			.define('I', ItemInit.LEAD_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.GRINDER));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.COMPRESSOR)
			.pattern("RYR")
			.pattern("XCX")
			.pattern("IZI")
			.define('X', Blocks.PISTON)
			.define('Y', Blocks.IRON_BLOCK)
			.define('Z', ItemInit.COPPER_COIL)
			.define('I', ItemInit.LEAD_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.COMPRESSOR));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.SAWMILL)
			.pattern("RYR")
			.pattern("XCX")
			.pattern("IZI")
			.define('X', Items.IRON_AXE)
			.define('Y', Blocks.IRON_BARS)
			.define('Z', Blocks.REDSTONE_BLOCK)
			.define('I', ItemInit.LEAD_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.SAWMILL));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.MELTER)
			.pattern("RYR")
			.pattern("XCW")
			.pattern("IZI")
			.define('W', Blocks.HOPPER)
			.define('X', BlockInit.TANK)
			.define('Y', Blocks.FURNACE)
			.define('Z', ItemInit.COPPER_COIL)
			.define('I', ItemInit.LEAD_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.MELTER));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.SOLIDIFIER)
			.pattern("RYR")
			.pattern("XCW")
			.pattern("IZI")
			.define('W', BlockInit.TANK)
			.define('X', Blocks.HOPPER)
			.define('Y', Blocks.CAULDRON)
			.define('Z', ItemInit.COPPER_COIL)
			.define('I', ItemInit.LEAD_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.SOLIDIFIER));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.REACTION_CHAMBER)
			.pattern("RYR")
			.pattern("XCW")
			.pattern("IZI")
			.define('W', Blocks.PISTON)
			.define('X', Blocks.CAULDRON)
			.define('Y', Blocks.FURNACE)
			.define('Z', ItemInit.COPPER_COIL)
			.define('I', ItemInit.LEAD_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.REACTION_CHAMBER));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ELECTROLYZER)
			.pattern("RYR")
			.pattern("XCX")
			.pattern("IZI")
			.define('X', ItemInit.LEAD_ROD)
			.define('Y', ItemInit.LEAD_PLATE)
			.define('Z', ItemInit.COPPER_COIL)
			.define('I', ItemInit.LEAD_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ELECTROLYZER));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ELECTRIC_PUMP)
			.pattern("RCR")
			.pattern("IZI")
			.pattern("I I")
			.define('Z', Items.BUCKET)
			.define('I', ItemInit.LEAD_INGOT)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ELECTRIC_PUMP));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.BATTERY)
			.pattern("RSR")
			.pattern("SCS")
			.pattern("RSR")
			.define('S', ItemInit.COPPER_COIL)
			.define('R', Items.REDSTONE)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.BATTERY));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ROCKET_PART_BENCH)
			.pattern("ISI")
			.pattern("SCS")
			.pattern("RSR")
			.define('S', Blocks.CRAFTING_TABLE)
			.define('I', ItemInit.STEEL_PLATE)
			.define('R', ItemInit.COPPER_COIL)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ROCKET_PART_BENCH));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ROCKET_ASSEMBLY_STATION)
			.pattern("BPB")
			.pattern("SCS")
			.pattern("RZR")
			.define('P', ItemInit.PALLADIUM_PLATE)
			.define('B', ItemInit.BORON_ROD)
			.define('S', ItemInit.CONSTANTAN_ROD)
			.define('R', ItemInit.STEEL_PLATE)
			.define('Z', ItemInit.COPPER_COIL)
			.define('C', BlockInit.BASIC_CASING)
			.unlockedBy(getHasName(BlockInit.BASIC_CASING), has(BlockInit.BASIC_CASING))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ROCKET_ASSEMBLY_STATION));
		
		// Moulds
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.MOULD_BASE)
			.pattern(" I ")
			.pattern("IBI")
			.pattern(" I ")
			.define('B', ItemInit.BORON_DUST)
			.define('I', ItemInit.STEEL_INGOT)
			.unlockedBy(getHasName(ItemInit.STEEL_INGOT), has(ItemInit.STEEL_INGOT))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(ItemInit.STEEL_INGOT));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.MOULD_PLATE)
			.requires(ItemInit.MOULD_BASE)
			.requires(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
			.unlockedBy(getHasName(ItemInit.MOULD_BASE), has(ItemInit.MOULD_BASE))
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(ItemInit.MOULD_PLATE));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.MOULD_ROD)
			.requires(ItemInit.MOULD_BASE)
			.requires(Items.STICK)
			.unlockedBy(getHasName(ItemInit.MOULD_BASE), has(ItemInit.MOULD_BASE))
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(ItemInit.MOULD_ROD));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.MOULD_WIRE)
			.requires(ItemInit.MOULD_BASE)
			.requires(Items.REDSTONE)
			.unlockedBy(getHasName(ItemInit.MOULD_BASE), has(ItemInit.MOULD_BASE))
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(ItemInit.MOULD_WIRE));
		
		// Reaction Chamber
		reactff_f(gen, FluidInit.MOLTEN_LEAD, 1, FluidInit.MOLTEN_BISMUTH, 1, FluidInit.LEAD_BISMUTH_EUTECTIC, 2, 10);
		reactff_f(gen, FluidInit.SULPHUR_DIOXIDE, 2, FluidInit.OXYGEN, 1, FluidInit.SULPHUR_TRIOXIDE, 2, 10);
		reactff_f(gen, FluidInit.SULPHUR_TRIOXIDE, 1, FluidObject.WATER, 1, FluidInit.SULPHURIC_ACID, 1, 7);
		reactfi_fi(gen, FluidInit.SULPHURIC_ACID, 1, ItemInit.NITER, 1, FluidInit.NITRIC_ACID, 1, ItemInit.POTASSIUM_BISULFATE, 1, 10, 144);
		reactff_i(gen, FluidInit.AMMONIA, 1, FluidInit.NITRIC_ACID, 1, ItemInit.AMMONIUM_NITRATE, 1, 17, 144);
		
		// Electrolysis Chamber
		electrolysis_f_ff(gen, FluidObject.WATER, 2, FluidInit.HYDROGEN, 2, FluidInit.OXYGEN, 1, 38);
		electrolysis_f_ff(gen, FluidInit.AMMONIA, 2, FluidInit.HYDROGEN, 3, FluidInit.NITROGEN, 1, 84);
		electrolysis_f_ff(gen, FluidInit.METHANOL, 1, FluidInit.HYDROGEN, 2, FluidInit.CARBON_MONOXIDE, 1, 42);
		electrolysis_f_ff(gen, FluidInit.HYDROCHLORIC_ACID, 2, FluidInit.HYDROGEN, 1, FluidInit.CHLORINE, 1, 42);
		electrolysis_f_fff(gen, FluidInit.SULPHURIC_ACID, 2, FluidInit.SULPHUR_DIOXIDE, 2, FluidInit.OXYGEN, 1,FluidObject.WATER, 1, 42);
		electrolysis_f_fff(gen, FluidInit.NITRIC_ACID, 4, FluidInit.NITROGEN_DIOXIDE, 4, FluidInit.OXYGEN, 1, FluidObject.WATER, 2, 42);
		electrolysis_f_fff(gen, FluidInit.ETHANOL, 1, FluidInit.ETHYLENE, 1, FluidInit.HYDROGEN, 1, FluidObject.WATER, 1, 63);
		electrolysis_ff_fff(gen, FluidInit.BENZYL_CHLORIDE, 2, FluidObject.WATER, 2, FluidInit.HYDROCHLORIC_ACID, 2, FluidInit.BENZENE, 2, FluidInit.OXYGEN, 1, 104);
		electrolysis_ff_ffi(gen, FluidInit.BRINE, 2, FluidObject.WATER, 2, FluidInit.CHLORINE, 1, FluidInit.HYDROGEN, 1, ItemInit.SODIUM_HYDROXIDE, 72, 38);
		electrolysis_fi_f(gen, FluidInit.BRINE, 2, ItemInit.SODIUM_HYDROXIDE, 3, FluidInit.CHLORINE, 2, 72, 72);
		electrolysis_fi_fi(gen, FluidInit.CARBON_DIOXIDE, 1, ItemInit.SODIUM_HYDROXIDE, 1, FluidObject.WATER, 1, ItemInit.SODIUM_CARBONATE, 144, 72);
		electrolysis_f_fi(gen, FluidInit.HYDROGEN_SULPHIDE, 2, FluidInit.HYDROGEN, 1, ItemInit.SULFUR, 500, 72);
		electrolysis_i_ff_c(gen, ItemInit.HEXAMINE, 1, FluidInit.METHANE, 6, FluidInit.HYDROGEN, 2, ItemInit.PALLADIUM_ON_CARBON, 60, 72);
		
		// Rocket Parts
		rocket_part(gen, RocketPartInit.SIMPLE_CHASSIS, 100_000,
				new ItemStack(ItemInit.IRON_PLATE.get(), 60),
				new ItemStack(ItemInit.CONSTANTAN_ROD.get(), 8),
				new ItemStack(BlockInit.TANK.get(), 2),
				new ItemStack(ItemInit.COPPER_COIL.get(), 3));
		rocket_part(gen, RocketPartInit.ADVANCED_CHASSIS, 150_000,
				new ItemStack(ItemInit.STEEL_PLATE.get(), 96),
				new ItemStack(ItemInit.CONSTANTAN_ROD.get(), 12),
				new ItemStack(BlockInit.TANK.get(), 4),
				new ItemStack(ItemInit.COPPER_COIL.get(), 4));
		
		rocket_part(gen, RocketPartInit.SIMPLE_FUEL_TANK, 100_000,
				new ItemStack(ItemInit.IRON_PLATE.get(), 80),
				new ItemStack(ItemInit.NICKEL_ROD.get(), 12),
				new ItemStack(BlockInit.TANK.get(), 2),
				new ItemStack(ItemInit.COPPER_COIL.get(), 3));
		rocket_part(gen, RocketPartInit.PRESSURIZED_FUEL_TANK, 150_000,
				new ItemStack(ItemInit.STEEL_PLATE.get(), 128),
				new ItemStack(ItemInit.NICKEL_ROD.get(), 24),
				new ItemStack(BlockInit.TANK.get(), 4),
				new ItemStack(ItemInit.COPPER_COIL.get(), 4));
		
		rocket_part(gen, RocketPartInit.SIMPLE_LIFE_SUPPORT, 100_000,
				new ItemStack(ItemInit.ALUMINUM_PLATE.get(), 48),
				new ItemStack(ItemInit.LEAD_INGOT.get(), 6),
				new ItemStack(ItemInit.IRON_ROD.get(), 32),
				new ItemStack(BlockInit.TANK.get(), 1));
		rocket_part(gen, RocketPartInit.REINFORCED_LIFE_SUPPORT, 150_000,
				new ItemStack(ItemInit.ALUMINUM_PLATE.get(), 64),
				new ItemStack(ItemInit.LEAD_INGOT.get(), 12),
				new ItemStack(ItemInit.IRON_ROD.get(), 48),
				new ItemStack(BlockInit.TANK.get(), 1));
		
		rocket_part(gen, RocketPartInit.SIMPLE_SHIELD, 100_000,
				new ItemStack(ItemInit.IRON_PLATE.get(), 12),
				new ItemStack(ItemInit.NICKEL_ROD.get(), 2),
				new ItemStack(ItemInit.COPPER_COIL.get(), 8));
		rocket_part(gen, RocketPartInit.CONE_SHIELD, 150_000,
				new ItemStack(ItemInit.IRON_PLATE.get(), 68),
				new ItemStack(ItemInit.NICKEL_ROD.get(), 4),
				new ItemStack(ItemInit.COPPER_COIL.get(), 8));
		
		rocket_part(gen, RocketPartInit.SIMPLE_THRUSTER, 100_000,
				new ItemStack(ItemInit.STEEL_PLATE.get(), 128),
				new ItemStack(ItemInit.COPPER_COIL.get(), 32));
		rocket_part(gen, RocketPartInit.TRI_TALL_THRUSTER, 150_000,
				new ItemStack(ItemInit.STEEL_ROD.get(), 96),
				new ItemStack(ItemInit.ALUMINUM_DUST.get(), 32),
				new ItemStack(ItemInit.GOLD_WIRE.get(), 96),
				new ItemStack(ItemInit.COPPER_COIL.get(), 48));
		//@formatter:on

		FamiliesInit.ORES.forEach(x -> oreFamily(gen, x));
		FamiliesInit.STONES.forEach(x -> stoneFamily(gen, x));
		FamiliesInit.WOODS.forEach(x -> woodFamily(gen, x));
	}

	protected static void oreFamily(RecipeOutput gen, OreFamily family) {
		// Crafting ingot
		family.getIngot().ifPresent(ingot -> {
			family.ore().ifPresent(ore -> ore(gen, List.of(ore), ingot, 0.7f, 200, family.name()));
			family.raw().ifPresent(raw -> ore(gen, List.of(raw), ingot, 0.7f, 200, family.name()));
			family.dust().ifPresent(dust -> oreSmelting(gen, List.of(dust), ingot, 0, 200, family.name()));
		});

		// Crafting block
		family.getBlock().ifPresent(block -> {
			family.ingot().ifPresent(ingot -> compact(gen, block, ingot));
			family.rawBlock().ifPresent(raw -> ore(gen, List.of(raw), block, 2.7f, 200, family.name()));
		});

		// Crafting nugget
		family.getNugget().ifPresent(nugget -> family.ingot().ifPresent(ingot -> compact(gen, ingot, nugget)));
	}

	protected static void woodFamily(RecipeOutput gen, WoodFamily family) {
		//@formatter:off
		log_and_plank(gen, family.log(), family.wood(), family.planks());
		log_and_plank(gen, family.stripped_log(), family.stripped_wood(), family.planks());
		slab(gen, family.planks(), family.slab());
		stair(gen, family.planks(), family.stairs());
		door(gen, family.planks(), family.door());
		trapdoor(gen, family.planks(), family.trapdoor());
		pressure_plate(gen, family.planks(), family.pressure_plate());
		button(gen, family.planks(), family.button());
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, family.fence(), 3)
			.pattern("BSB")
			.pattern("BSB")
			.define('B', family.planks())
			.define('S', Items.STICK)
			.unlockedBy(getHasName(family.planks()), has(family.planks()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.fence()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, family.fencegate())
			.pattern("SBS")
			.pattern("SBS")
			.define('B', family.planks())
			.define('S', Items.STICK)
			.unlockedBy(getHasName(family.planks()), has(family.planks()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.fencegate()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, family.sign(), 3)
			.pattern("BBB")
			.pattern("BBB")
			.pattern(" S ")
			.define('B', family.planks())
			.define('S', Items.STICK)
			.unlockedBy(getHasName(family.planks()), has(family.planks()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.sign()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, family.hangingsign(), 6)
			.pattern("C C")
			.pattern("BBB")
			.pattern("BBB")
			.define('B', family.stripped_log())
			.define('C', Items.CHAIN)
			.unlockedBy(getHasName(family.stripped_log()), has(family.stripped_log()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.hangingsign()));
		//@formatter:on
	}

	protected static void log_and_plank(RecipeOutput gen, ItemLike log, ItemLike wood, ItemLike planks) {
		//@formatter:off
		// LOG -> WOOD
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood, 3)
			.pattern("BB")
			.pattern("BB")
			.define('B', log)
			.unlockedBy(getHasName(log), has(log))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(wood));
		
		// LOG -> PLANKS
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
			.requires(log)
			.unlockedBy(getHasName(log), has(log))
			.save(gen, Machina.MOD_ID + ":" + getItemName(planks) + "_from_" + getItemName(log));
		
		// WOOD -> PLANKS
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
			.requires(wood)
			.unlockedBy(getHasName(wood), has(wood))
			.save(gen, Machina.MOD_ID + ":" + getItemName(planks) + "_from_" + getItemName(wood));
		//@formatter:on
	}

	protected static void stoneFamily(RecipeOutput gen, StoneFamily family) {
		//@formatter:off
		slab(gen, family.base(), family.slab());
		stair(gen, family.base(), family.stairs());
		pressure_plate(gen, family.base(), family.pressure_plate());
		button(gen, family.base(), family.button());
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, family.wall(), 6)
			.pattern("BBB")
			.pattern("BBB")
			.define('B', family.base())
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.wall()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, family.base(), 1)
			.pattern("PP")
			.pattern("PP")
			.define('P', family.pebbles())
			.unlockedBy(getHasName(family.pebbles()), has(family.pebbles()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_base_from_" + getItemName(family.pebbles()));
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(family.base()), RecipeCategory.BUILDING_BLOCKS, family.slab(), 2)
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.save(gen, Machina.MOD_ID + ":stonecutting_" + getItemName(family.slab()));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(family.base()), RecipeCategory.BUILDING_BLOCKS, family.stairs())
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.save(gen, Machina.MOD_ID + ":stonecutting_" + getItemName(family.stairs()));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(family.base()), RecipeCategory.BUILDING_BLOCKS, family.wall())
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.save(gen, Machina.MOD_ID + ":stonecutting_" + getItemName(family.wall()));
		//@formatter:on
	}
}
