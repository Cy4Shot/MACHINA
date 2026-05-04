package com.machina.datagen.client;

import java.util.function.Function;

import com.google.common.base.Preconditions;
import com.machina.Machina;
import com.machina.api.block.ConnectorBlock;
import com.machina.api.block.LitMachineBlock;
import com.machina.api.block.MachineBlock;
import com.machina.api.client.model.ctm.LayeredRodBlockModel;
import com.machina.api.util.MachinaRL;
import com.machina.block.MachinaWaterlilyBlock;
import com.machina.block.PebbleBlock;
import com.machina.block.SmallFlowerBlock;
import com.machina.datagen.client.builder.CTMBlockStateBuilder;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.DirtFamily;
import com.machina.registration.init.FamiliesInit.OreFamily;
import com.machina.registration.init.FamiliesInit.StoneFamily;
import com.machina.registration.init.FamiliesInit.WoodFamily;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.FruitInit;
import com.machina.registration.init.FruitInit.Fruit;
import com.machina.registration.init.TagInit.BlockTagInit;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.IGeneratedBlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class DatagenBlockStates extends BlockStateProvider {
	public DatagenBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, Machina.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {

		casing(BlockInit.BASIC_CASING);
		casing(BlockInit.LIGHTWEIGHT_CASING);

		connector(BlockInit.ENERGY_CABLE);
		connector(BlockInit.FLUID_PIPE);
		connector(BlockInit.ITEM_CONDUIT);

		machine(BlockInit.CREATIVE_BATTERY);
		machineAllLit(BlockInit.BATTERY, true);
		machineLit(BlockInit.COMPOSTER_VAT, false);
		machineLit(BlockInit.COMPRESSOR, false);
		machineLit(BlockInit.ELECTRIC_SMELTER, true);
		machineLit(BlockInit.FURNACE_GENERATOR, true);
		machineLit(BlockInit.CHEMICAL_GENERATOR, true);
		machineLit(BlockInit.GRINDER, false);
		machineLit(BlockInit.MELTER, false);
		machineLit(BlockInit.REACTION_CHAMBER, false);
		machineLit(BlockInit.SAWMILL, false);
		machineLit(BlockInit.SOLIDIFIER, false);
		machineLit(BlockInit.ELECTROLYZER, false);
		machine(BlockInit.ROCKET_PART_BENCH);
		machine(BlockInit.ROCKET_ASSEMBLY_STATION);
		machine(BlockInit.ROCKET_REFUELING_STATION);

		ctm(BlockInit.GEOTHERMAL_GENERATOR_CASING, BlockTagInit.GEOTHERMAL_GENERATOR_CTM);
		ctm(BlockInit.GEOTHERMAL_GENERATOR_CONTROLLER, BlockTagInit.GEOTHERMAL_GENERATOR_CTM);

		ctm(BlockInit.FISSION_REACTOR_CASING, BlockTagInit.FISSION_REACTOR_CTM);
		ctm(BlockInit.FISSION_REACTOR_ENERGY_PORT, BlockTagInit.FISSION_REACTOR_CTM);
		ctm(BlockInit.FISSION_REACTOR_ITEM_PORT, BlockTagInit.FISSION_REACTOR_CTM);
		ctm(BlockInit.FISSION_REACTOR_WATER_PORT, BlockTagInit.FISSION_REACTOR_CTM);
		ctm(BlockInit.FISSION_REACTOR_STEAM_PORT, BlockTagInit.FISSION_REACTOR_CTM);
		ctm(BlockInit.FISSION_REACTOR_GLASS, BlockTagInit.FISSION_REACTOR_GLASS_CTM);
		ctmLayeredRod(BlockInit.FISSION_FUEL_ROD, BlockTagInit.FISSION_FUEL_ROD_CTM);

		cube(BlockInit.BROWN_MUSHROOM_STALK);
		cube(BlockInit.GREEN_MUSHROOM_STALK);
		cube(BlockInit.PURPLE_MUSHROOM_STALK);
		cube(BlockInit.YELLOW_MUSHROOM_STALK);
		cube(BlockInit.BROWN_MUSHROOM_CAP);
		cube(BlockInit.RED_MUSHROOM_CAP);
		cube(BlockInit.PURPLE_MUSHROOM_CAP);
		cube(BlockInit.YELLOW_MUSHROOM_CAP);
		cube(BlockInit.BROWN_MUSHROOM_GILLS);
		cube(BlockInit.RED_MUSHROOM_GILLS);
		cube(BlockInit.PURPLE_MUSHROOM_GILLS);
		cube(BlockInit.YELLOW_MUSHROOM_GILLS);
		cube(BlockInit.SPECKLED_BROWN_MUSHROOM_CAP);
		cube(BlockInit.SPECKLED_RED_MUSHROOM_CAP);
		cube(BlockInit.SPECKLED_PURPLE_MUSHROOM_CAP);
		cube(BlockInit.SPECKLED_YELLOW_MUSHROOM_CAP);
		cube(BlockInit.IMBUED_BROWN_MUSHROOM_CAP);
		cube(BlockInit.IMBUED_RED_MUSHROOM_CAP);
		cube(BlockInit.IMBUED_PURPLE_MUSHROOM_CAP);
		cube(BlockInit.IMBUED_YELLOW_MUSHROOM_CAP);

		cube(BlockInit.TROPICAL_SAND);
		cube(BlockInit.MOONSAND);
		cube(BlockInit.FERROUS_SAND);
		cube(BlockInit.ASH);
		cube(BlockInit.POLLUTED_SAND);
		cube(BlockInit.TOXIC_SAND);

		unrotatableColumn(BlockInit.SULFUR_GEYSER);

		flower(BlockInit.TROPICAL_GRASS);
		flower(BlockInit.TWISTED_GRASS);
		flower(BlockInit.CONIFEROUS_GRASS);
		flower(BlockInit.SHORT_CONIFEROUS_GRASS);
		flower(BlockInit.WINDSWEPT_GRASS);
		flower(BlockInit.MYCELIAL_GRASS);
		flower(BlockInit.FERROUS_GRASS);
		flower(BlockInit.MOONGRASS);

		flower(BlockInit.SPINDLESPROUT);
		flower(BlockInit.SMALL_FERN);
		flower(BlockInit.DEAD_SMALL_FERN);
		flower(BlockInit.SPRUCE_CUP);
		flower(BlockInit.NEEDLEGRASS);
		tall_flower(BlockInit.NEEDLETHATCH);
		tall_flower(BlockInit.SPINDLEGRASS);

		tall_flower(BlockInit.ORPHEUM);
		flower(BlockInit.DRAGON_PEONY);

		petals(BlockInit.PURPLE_PETALS);
		petals(BlockInit.RED_PETALS);
		petals(BlockInit.ORANGE_PETALS);
		petals(BlockInit.YELLOW_PETALS);
		petals(BlockInit.GREEN_PETALS);
		petals(BlockInit.TURQUOISE_PETALS);
		petals(BlockInit.BLUE_PETALS);

		flower_pot(BlockInit.POTTED_DRAGON_PEONY);
		flower_pot(BlockInit.POTTED_SPINDLESPROUT);
		flower_pot(BlockInit.POTTED_SMALL_FERN);
		flower_pot(BlockInit.POTTED_DEAD_SMALL_FERN);
		flower_pot(BlockInit.POTTED_SPRUCE_CUP);
		flower_pot(BlockInit.POTTED_NEEDLEGRASS);

		groundlily(BlockInit.PURPLE_GROUNDLILY, "purple");
		groundlily(BlockInit.PINK_GROUNDLILY, "pink");
		groundlily(BlockInit.RED_GROUNDLILY, "red");
		groundlily(BlockInit.ORANGE_GROUNDLILY, "orange");
		groundlily(BlockInit.YELLOW_GROUNDLILY, "yellow");
		groundlily(BlockInit.GREEN_GROUNDLILY, "green");
		groundlily(BlockInit.TURQUOISE_GROUNDLILY, "turquoise");
		groundlily(BlockInit.BLUE_GROUNDLILY, "blue");
		waterlily(BlockInit.PURPLE_WATERLILY, "purple");
		waterlily(BlockInit.PINK_WATERLILY, "pink");
		waterlily(BlockInit.RED_WATERLILY, "red");
		waterlily(BlockInit.ORANGE_WATERLILY, "orange");
		waterlily(BlockInit.YELLOW_WATERLILY, "yellow");
		waterlily(BlockInit.GREEN_WATERLILY, "green");
		waterlily(BlockInit.TURQUOISE_WATERLILY, "turquoise");
		waterlily(BlockInit.BLUE_WATERLILY, "blue");

		flower(BlockInit.PURPLE_GLOWSHROOM);
		flower_pot(BlockInit.POTTED_PURPLE_GLOWSHROOM);
		flower(BlockInit.PINK_GLOWSHROOM);
		flower_pot(BlockInit.POTTED_PINK_GLOWSHROOM);
		flower(BlockInit.RED_GLOWSHROOM);
		flower_pot(BlockInit.POTTED_RED_GLOWSHROOM);
		flower(BlockInit.ORANGE_GLOWSHROOM);
		flower_pot(BlockInit.POTTED_ORANGE_GLOWSHROOM);
		flower(BlockInit.YELLOW_GLOWSHROOM);
		flower_pot(BlockInit.POTTED_YELLOW_GLOWSHROOM);
		flower(BlockInit.GREEN_GLOWSHROOM);
		flower_pot(BlockInit.POTTED_GREEN_GLOWSHROOM);
		flower(BlockInit.TURQUOISE_GLOWSHROOM);
		flower_pot(BlockInit.POTTED_TURQUOISE_GLOWSHROOM);
		flower(BlockInit.BLUE_GLOWSHROOM);
		flower_pot(BlockInit.POTTED_BLUE_GLOWSHROOM);

		item(BlockInit.ATMOSPHERIC_SEPARATOR);
		item(BlockInit.ELECTRIC_PUMP);
		item(BlockInit.TANK);
		item(BlockInit.FISSION_FUEL_ROD_ASSEMBLY);

		// Dynamic
		FruitInit.FRUITS.forEach(this::fruit);
		FluidInit.OBJS.forEach(this::fluid);
		FamiliesInit.ORES.forEach(this::oreFamily);
		FamiliesInit.STONES.forEach(this::stoneFamily);
		FamiliesInit.DIRTS.forEach(this::dirtFamily);
		FamiliesInit.WOODS.forEach(this::woodFamily);
	}

	private void oreFamily(OreFamily fam) {
		fam.getBlock().ifPresent(this::cube);
		fam.getRawBlock().ifPresent(this::cube);
		fam.ore().ifPresent(ores -> {
			ores.map().forEach((base, ore) -> ore(base.location(), ore, ores.name()));
		});
		fam.getIngot().ifPresent(item -> {
			if (item instanceof BlockItem ingotBlock) {
				this.cross(ingotBlock.getBlock());
			}
		});
	}

	private void stoneFamily(StoneFamily fam) {
		cube(fam.base());
		slab(fam.slab(), fam.base());
		stairs(fam.stairs(), fam.base());
		wall(fam.wall(), fam.base());
		button(fam.button(), fam.base());
		pressure_plate(fam.pressure_plate(), fam.base());
		pebble(fam.pebbles());
	}

	private void dirtFamily(DirtFamily fam) {
		if (fam.randomRotation()) {
			cubeRandomRotation(fam.dirt());
		} else {
			cube(fam.dirt());
		}
		slab(fam.slab(), fam.dirt());
		stairs(fam.stairs(), fam.dirt());
		fam.grass().ifPresent(this::cubeBottomTopRandomRotation);
	}

	private void woodFamily(WoodFamily fam) {
		cube(fam.planks());
		for (LeavesBlock leaf : fam.leaves()) {
			leaves(leaf);
		}
		log(fam.log());
		log(fam.wood());
		log(fam.stripped_log());
		log(fam.stripped_wood());
		slab(fam.slab(), fam.planks());
		stairs(fam.stairs(), fam.planks());
		button(fam.button(), fam.planks());
		pressure_plate(fam.pressure_plate(), fam.planks());
		fence(fam.fence(), fam.planks());
		fence_gate(fam.fencegate(), fam.planks());
		sign(fam.signblock(), fam.wallsignblock(), fam.planks());
		sign(fam.hangingsignblock(), fam.hangingwallsignblock(), fam.planks());
		trapdoor(fam.trapdoor());
		door(fam.door());
	}

	private void cubeRandomRotation(Block b) {
		ModelFile model = cubeAll(b);
		simpleBlock(b, randomRotation(model));
		simpleBlockItem(b, model);
	}

	private void cubeBottomTopRandomRotation(Block b) {
		ResourceLocation t = blockTexture(b);
		ModelFile model = models().cubeBottomTop(name(b), extend(t, "_side"), extend(t, "_bottom"), extend(t, "_top"));
		simpleBlock(b, randomRotation(model));
		simpleBlockItem(b, model);
	}

	private ConfiguredModel[] randomRotation(ModelFile model) {
		return ConfiguredModel.builder().modelFile(model).nextModel().rotationY(270).modelFile(model).nextModel()
				.rotationY(180).modelFile(model).nextModel().rotationY(90).modelFile(model).build();
	}

	private void item(DeferredBlock<? extends Block> block) {
		ModelFile model = models().getExistingFile(MachinaRL.create("block/" + name(block.get())));
		simpleBlockItem(block.get(), model);
	}

	private void cube(Block block) {
		simpleBlockWithItem(block, cubeAll(block));
	}

	private void cube(DeferredBlock<? extends Block> blockDeferredBlock) {
		cube(blockDeferredBlock.get());
	}

	private void ctm(DeferredBlock<? extends Block> block, TagKey<Block> ctmTag) {
		Block b = block.get();
		ModelFile cube = cubeAll(b);
		//@formatter:off
		CTMBlockStateBuilder builder = getCTMBuilder(b, cube)
			.setLoader(CTMBlockStateBuilder.CTM_LOADER)
			.addCTMTexture("particle", blockTexture(b))
			.addCTMTexture("center", ctmTexture(b, "c"))
			.addCTMTexture("empty", ctmTexture(b, "e"))
			.addCTMTexture("horizontal", ctmTexture(b, "h"))
			.addCTMTexture("vertical", ctmTexture(b, "v"));
		//@formatter:on
		if (ctmTag != null) {
			builder.setCTMTag(ctmTag);
		}
		simpleBlockItem(b, cube);
	}

	private void ctmLayeredRod(DeferredBlock<? extends Block> block, TagKey<Block> ctmTag) {
		Block b = block.get();
		ModelFile rod = cubeAll(b);
		//@formatter:off
		CTMBlockStateBuilder builder = getCTMBuilder(b, rod)
			.setLoader(LayeredRodBlockModel.RL)
			.addCTMTexture("particle", blockTexture(b))
			.addCTMTexture("top", ctmTexture(b, "t"))
			.addCTMTexture("center", ctmTexture(b, "c"))
			.addCTMTexture("bottom", ctmTexture(b, "b"))
			.addCTMTexture("self", ctmTexture(b, "s"))
			.addCTMTexture("inner_top", ctmTexture(b, "it"))
			.addCTMTexture("inner_center", ctmTexture(b, "ic"))
			.addCTMTexture("inner_bottom", ctmTexture(b, "ib"))
			.addCTMTexture("inner_self", ctmTexture(b, "is"));
		//@formatter:on
		if (ctmTag != null) {
			builder.setCTMTag(ctmTag);
		}
		simpleBlockItem(b, rod);
	}

	private void leaves(LeavesBlock leaves) {
		simpleBlockWithItem(leaves, models().cubeAll(name(leaves), blockTexture(leaves)).renderType("translucent"));
	}

	private void slab(SlabBlock slab, Block material) {
		ResourceLocation texture = blockTexture(material);
		slabBlock(slab, texture, texture);
		simpleBlockItem(slab, models().slab(name(slab), texture, texture, texture));
	}

	private void log(RotatedPillarBlock log) {
		ResourceLocation texture = blockTexture(log);
		logBlock(log);
		simpleBlockItem(log, models().cubeColumn(name(log), texture, extend(texture, "_top")));
	}

	private void wall(WallBlock wall, Block material) {
		ResourceLocation texture = blockTexture(material);
		wallBlock(wall, texture);
		simpleBlockItem(wall, models().wallInventory(name(wall) + "_inventory", texture));
	}

	private void stairs(StairBlock stair, Block material) {
		ResourceLocation texture = blockTexture(material);
		stairsBlock(stair, texture);
		simpleBlockItem(stair, models().stairs(name(stair), texture, texture, texture));
	}

	private void button(ButtonBlock button, Block material) {
		ResourceLocation texture = blockTexture(material);
		buttonBlock(button, texture);
		simpleBlockItem(button, models().buttonInventory(name(button) + "_inventory", texture));
	}

	private void pressure_plate(PressurePlateBlock plate, Block material) {
		ResourceLocation texture = blockTexture(material);
		pressurePlateBlock(plate, texture);
		simpleBlockItem(plate, models().pressurePlate(name(plate), texture));
	}

	private void fence(FenceBlock fence, Block material) {
		ResourceLocation texture = blockTexture(material);
		fenceBlock(fence, texture);
		simpleBlockItem(fence, models().fenceInventory(name(fence) + "_inventory", texture));
	}

	private void fence_gate(FenceGateBlock fence, Block material) {
		ResourceLocation texture = blockTexture(material);
		fenceGateBlock(fence, texture);
		simpleBlockItem(fence, models().fenceGate(name(fence), texture));
	}

	private void sign(SignBlock sign, SignBlock wall, Block material) {
		ModelFile mod = models().sign(name(sign), blockTexture(material));
		simpleBlock(sign, mod);
		simpleBlock(wall, mod);
	}

	private void trapdoor(TrapDoorBlock door) {
		ResourceLocation texture = blockTexture(door);
		trapdoorBlockWithRenderType(door, texture, true, "cutout");
		simpleBlockItem(door, models().trapdoorBottom(name(door), texture));
	}

	private void door(DoorBlock door) {
		ResourceLocation texture = blockTexture(door);
		doorBlockWithRenderType(door, extend(texture, "_bottom"), extend(texture, "_top"), "cutout");
		simpleFlatItem(door, itemTexture(door));
	}

	private void fluid(FluidObject obj) {
		getVariantBuilder(obj.block()).partialState().modelForState()
				.modelFile(
						models().cubeAll(name(obj.block()), ResourceLocation.withDefaultNamespace("block/water_still")))
				.addModel();
	}

	private void flower(DeferredBlock<? extends Block> flower) {
		Block f = flower.get();
		ResourceLocation tex = blockTexture(f);
		simpleBlock(f, models().cross(name(f), tex).renderType("cutout"));
		simpleFlatItem(f, tex);
	}

	private void cross(Block b) {
		simpleBlock(b, models().cross(name(b), blockTexture(b)).renderType("cutout"));
		simpleFlatItem(b, itemTexture(b));
	}

	private void fruit(Fruit fruit) {
		Block f = fruit.block().get();
		ResourceLocation tex = blockTexture(f);
		simpleBlock(f, models().cross(name(f), tex).renderType("cutout"));
	}

	private void tall_flower(DeferredBlock<TallFlowerBlock> flower) {
		TallFlowerBlock f = flower.get();
		ResourceLocation tex = blockTexture(f);
		getVariantBuilder(flower.get()).forAllStates(state -> {
			boolean top = state.getValue(TallFlowerBlock.HALF).equals(DoubleBlockHalf.UPPER);
			String name = top ? "_top" : "_bottom";
			ModelFile file = models().cross(name(f) + name, extend(tex, name)).renderType("cutout");
			return ConfiguredModel.builder().modelFile(file).build();
		});
		simpleFlatItem(f, extend(tex, "_top"));
	}

	private void flower_pot(DeferredBlock<FlowerPotBlock> pot) {
		FlowerPotBlock p = pot.get();
		simpleBlock(p, models().withExistingParent(name(p), ModelProvider.BLOCK_FOLDER + "/flower_pot_cross")
				.texture("plant", blockTexture(p.getPotted())).renderType("cutout"));
	}

	private void pebble(PebbleBlock p) {
		Function<Integer, ModelFile> getModel = i -> models()
				.withExistingParent(name(p) + "_" + i, MachinaRL.create("block/pebble" + i))
				.texture("pebbles", blockTexture(p)).renderType("cutout");

		getVariantBuilder(p).forAllStates(
				state -> ConfiguredModel.builder().modelFile(getModel.apply(state.getValue(PebbleBlock.VARIANT)))
						.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
						.build());
		simpleFlatItem(p, itemTexture(p));
	}

	private void groundlily(DeferredBlock<SmallFlowerBlock> lily, String col) {
		SmallFlowerBlock l = lily.get();

		ModelFile m = models().withExistingParent(name(l), MachinaRL.create("block/ground_lillies"))
				.texture("flower", MachinaRL.create("block/" + col + "_lily_flower")).renderType("cutout");

		getVariantBuilder(l).forAllStates(state -> ConfiguredModel.builder().modelFile(m)
				.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
				.build());
		simpleFlatItem(l, itemTexture(l));
	}

	private void waterlily(DeferredBlock<MachinaWaterlilyBlock> lily, String col) {
		MachinaWaterlilyBlock l = lily.get();

		ModelFile m = models().withExistingParent(name(l), MachinaRL.create("block/water_lillies"))
				.texture("flower", MachinaRL.create("block/" + col + "_lily_flower")).renderType("cutout");

		getVariantBuilder(l).forAllStates(state -> ConfiguredModel.builder().modelFile(m)
				.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
				.build());
		simpleFlatItem(l, itemTexture(l));
	}

	private void casing(DeferredBlock<? extends Block> casing) {
		Block p = casing.get();

		ModelFile model = models().withExistingParent(name(p), MachinaRL.create("block/machine_casing"))
				.texture("0", blockTexture(p)).renderType("cutout");

		simpleBlock(p, model);
		simpleBlockItem(p, model);
	}

	private void ore(ResourceLocation base, DeferredBlock<? extends Block> ore, ResourceLocation name) {
		Block o = ore.get();

		ModelFile model = models().withExistingParent(name(o), MachinaRL.create("block/ore"))
				.texture("base", blockTexture(base)).texture("ore", blockTexture(name));

		simpleBlock(o, model);
		simpleBlockItem(o, model);
	}

	public void machineAllLit(DeferredBlock<? extends LitMachineBlock> machine, boolean lit) {
		LitMachineBlock b = machine.get();
		ModelFile unlitm = models().withExistingParent(name(b), MachinaRL.create("block/machine"))
				.texture("side", extend(blockTexture(b), "_side")).texture("front", extend(blockTexture(b), "_front"))
				.texture("top", extend(blockTexture(b), "_top")).texture("bottom", extend(blockTexture(b), "_bottom"));
		ModelFile litm = lit ? models().withExistingParent(name(b) + "_lit", MachinaRL.create("block/machine"))
				.texture("side", extend(blockTexture(b), "_side_lit"))
				.texture("front", extend(blockTexture(b), "_front_lit"))
				.texture("top", extend(blockTexture(b), "_top_lit"))
				.texture("bottom", extend(blockTexture(b), "_bottom_lit")) : unlitm;

		getVariantBuilder(b).forAllStates(
				state -> ConfiguredModel.builder().modelFile(state.getValue(LitMachineBlock.LIT) ? litm : unlitm)
						.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
						.build());
		simpleBlockItem(b, litm);
	}

	public void machineLit(DeferredBlock<? extends LitMachineBlock> machine, boolean lit) {
		LitMachineBlock b = machine.get();
		ModelFile unlitm = models().withExistingParent(name(b), MachinaRL.create("block/machine"))
				.texture("side", extend(blockTexture(b), "_side")).texture("front", extend(blockTexture(b), "_front"))
				.texture("top", extend(blockTexture(b), "_top")).texture("bottom", extend(blockTexture(b), "_bottom"));
		ModelFile litm = lit ? models().withExistingParent(name(b) + "_lit", MachinaRL.create("block/machine"))
				.texture("side", extend(blockTexture(b), "_side"))
				.texture("front", extend(blockTexture(b), "_front_lit")).texture("top", extend(blockTexture(b), "_top"))
				.texture("bottom", extend(blockTexture(b), "_bottom")) : unlitm;

		getVariantBuilder(b).forAllStates(
				state -> ConfiguredModel.builder().modelFile(state.getValue(LitMachineBlock.LIT) ? litm : unlitm)
						.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
						.build());
		simpleBlockItem(b, litm);
	}

	public void machine(DeferredBlock<? extends MachineBlock> machine) {
		MachineBlock b = machine.get();
		ModelFile m = models().withExistingParent(name(b), MachinaRL.create("block/machine"))
				.texture("side", extend(blockTexture(b), "_side")).texture("front", extend(blockTexture(b), "_front"))
				.texture("top", extend(blockTexture(b), "_top")).texture("bottom", extend(blockTexture(b), "_bottom"));

		getVariantBuilder(b).forAllStates(state -> ConfiguredModel.builder().modelFile(m)
				.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
				.build());
		simpleBlockItem(b, m);
	}

	public void unrotatableColumn(DeferredBlock<? extends Block> machine) {
		Block b = machine.get();
		ModelFile m = models().withExistingParent(name(b), MachinaRL.create("block/unrotatable_column"))
				.texture("side", extend(blockTexture(b), "_side")).texture("top", extend(blockTexture(b), "_top"))
				.texture("bottom", extend(blockTexture(b), "_bottom"));

		getVariantBuilder(b).forAllStates(state -> ConfiguredModel.builder().modelFile(m).build());
		simpleBlockItem(b, m);
	}

	private void petals(DeferredBlock<PinkPetalsBlock> petals) {
		PinkPetalsBlock p = petals.get();
		ResourceLocation b = blockTexture(p);

		Function<Integer, ModelFile> m = i -> models()
				.withExistingParent(name(p) + "_" + i, ResourceLocation.withDefaultNamespace("block/flowerbed_" + i))
				.texture("flowerbed", b).texture("stem", MachinaRL.create("block/petals_stem")).renderType("cutout");

		//@formatter:off
		getMultipartBuilder(p)
				.part().modelFile(m.apply(1)).addModel()
					.condition(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
					.condition(BlockStateProperties.FLOWER_AMOUNT, 1, 2, 3, 4).end()
				.part().modelFile(m.apply(1)).rotationY(90).addModel()
					.condition(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
					.condition(BlockStateProperties.FLOWER_AMOUNT, 1, 2, 3, 4).end()
				.part().modelFile(m.apply(1)).rotationY(180).addModel()
					.condition(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 1, 2, 3, 4).end()
                .part().modelFile(m.apply(1)).rotationY(270).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 1, 2, 3, 4).end()
                .part().modelFile(m.apply(2)).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 2, 3, 4).end()
                .part().modelFile(m.apply(2)).rotationY(90).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 2, 3, 4).end()
                .part().modelFile(m.apply(2)).rotationY(180).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 2, 3, 4).end()
                .part().modelFile(m.apply(2)).rotationY(270).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 2, 3, 4).end()
                .part().modelFile(m.apply(3)).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 3, 4).end()
                .part().modelFile(m.apply(3)).rotationY(90).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 3, 4).end()
                .part().modelFile(m.apply(3)).rotationY(180).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 3, 4).end()
                .part().modelFile(m.apply(3)).rotationY(270).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 3, 4).end()
                .part().modelFile(m.apply(4)).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 4).end()
                .part().modelFile(m.apply(4)).rotationY(90).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 4).end()
                .part().modelFile(m.apply(4)).rotationY(180).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 4).end()
                .part().modelFile(m.apply(4)).rotationY(270).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                    .condition(BlockStateProperties.FLOWER_AMOUNT, 4).end();
		//@formatter:on

		simpleFlatItem(p, itemTexture(p));
	}

	private void connector(DeferredBlock<? extends ConnectorBlock> connector) {
		ConnectorBlock p = connector.get();
		ResourceLocation b = blockTexture(p);

		ModelFile main = models().withExistingParent(name(p), MachinaRL.create("block/connector/base"))
				.texture("connector", b).renderType("cutout");

		simpleBlockItem(p, main);
	}

	private void simpleFlatItem(Block block, ResourceLocation tex) {
		itemModels().getBuilder(key(block).getPath()).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", tex);
	}

	private String name(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block).getPath();
	}

	private ResourceLocation extend(ResourceLocation rl, String suffix) {
		return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
	}

	private ResourceLocation key(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	public ResourceLocation itemTexture(Block block) {
		ResourceLocation name = key(block);
		return ResourceLocation.fromNamespaceAndPath(name.getNamespace(),
				ModelProvider.ITEM_FOLDER + "/" + name.getPath());
	}

	public ResourceLocation blockTexture(ResourceLocation name) {
		return ResourceLocation.fromNamespaceAndPath(name.getNamespace(),
				ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
	}

	public ResourceLocation ctmTexture(Block block, String key) {
		ResourceLocation name = key(block);
		return ResourceLocation.fromNamespaceAndPath(name.getNamespace(),
				ModelProvider.BLOCK_FOLDER + "/ctm/" + name.getPath() + "/" + key);
	}

	public CTMBlockStateBuilder getCTMBuilder(Block b, ModelFile model) {
		if (registeredBlocks.containsKey(b)) {
			IGeneratedBlockState old = registeredBlocks.get(b);
			Preconditions.checkState(old instanceof CTMBlockStateBuilder);
			return (CTMBlockStateBuilder) old;
		} else {
			CTMBlockStateBuilder ret = new CTMBlockStateBuilder(new ConfiguredModel(model));
			registeredBlocks.put(b, ret);
			return ret;
		}
	}
}
