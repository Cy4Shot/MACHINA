package com.machina.block.entity.machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.item.RocketPartItem;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.util.PlayerHelper;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.RocketPartBenchMenu;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.RecipeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class RocketPartBenchBlockEntity extends MachinaBlockEntity implements RecipeInput {

	private static final Map<RocketPart<?>, RecipeHolder<? extends MachinaRecipe<RocketPartBenchBlockEntity>>> RECIPE_CACHE = new HashMap<>();

	private int progress = 0;
	private RecipeHolder<? extends MachinaRecipe<RocketPartBenchBlockEntity>> recipe = null;

	public RocketPartBenchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public RocketPartBenchBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ROCKET_PART_BENCH.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
	}

	@Override
	public boolean hasItemIO() {
		return false;
	}

	@Override
	public boolean activeModel() {
		return true;
	}

	public boolean hasPower(MachinaRecipe<RocketPartBenchBlockEntity> r) {
		return this.getEnergy() >= r.getPowerRate();
	}

	public Optional<RecipeHolder<? extends MachinaRecipe<RocketPartBenchBlockEntity>>> getRecipe(RocketPart<?> part) {
		if (RECIPE_CACHE.containsKey(part))
			return Optional.of(RECIPE_CACHE.get(part));
		else {
			Optional<RecipeHolder<? extends MachinaRecipe<RocketPartBenchBlockEntity>>> x = RecipeInit.ROCKET_PART_BENCH
					.maps().findRecipe(r -> r.value().getOutputItems().get(0).getItem().equals(part.getItem()));
			x.ifPresent(rocketPartBenchBlockEntityMachinaRecipe -> RECIPE_CACHE.put(part,
					rocketPartBenchBlockEntityMachinaRecipe));
			return x;
		}
	}

	public int getProgress() {
		return progress;
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
		tag.putInt("progress", progress);
		tag.putString("recipe", this.recipe == null ? "" : this.recipe.id().toString());
		super.saveAdditional(tag, registries);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
		this.progress = tag.getInt("progress");
		String r = tag.getString("recipe");
		this.recipe = r.isEmpty() ? null : RecipeInit.ROCKET_PART_BENCH.maps().getRecipe(ResourceLocation.parse(r));
		super.loadAdditional(tag, registries);
	}

	@Override
	public int getMaxEnergy() {
		// TODO: Config
		return 1_000_000;
	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return RocketPartBenchMenu::new;
	}

	public boolean isCrafting() {
		return this.recipe != null && this.progress > 0;
	}

	public RocketPart<?> output() {
		if (this.recipe != null) {
			Item i = this.recipe.value().getOutputItems().get(0).getItem();
			if (i instanceof RocketPartItem rpi) {
				return rpi.getRocketPart();
			}
		}
		return null;
	}

	@Override
	public void tick() {
		if (this.level == null || this.level.isClientSide())
			return;
		if (!isCrafting())
			return;

		this.progress--;
		if (this.progress == 0) {

			Vec3 pos = this.getBlockPos().above().getCenter();
			ItemEntity itementity = new ItemEntity(level, pos.x, pos.y, pos.z, recipe.value().getOutputItems().get(0));
			itementity.setDeltaMovement(level.random.triangle(0.0D, 0.11485000171139836D),
					level.random.triangle(0.2D, 0.11485000171139836D),
					level.random.triangle(0.0D, 0.11485000171139836D));
			level.addFreshEntity(itementity);

			this.recipe = null;
		}
		this.setChanged();
	}

	public void startCrafting(ServerPlayer player, RocketPart<?> part) {
		RecipeHolder<? extends MachinaRecipe<RocketPartBenchBlockEntity>> recipe = getRecipe(part).orElse(null);
		if (recipe == null)
			return;
		if (!hasPower(recipe.value()))
			return;
		if (!PlayerHelper.hasAll(player, recipe.value().getInputItems()))
			return;

		consumeEnergy(recipe.value().getPowerRate());
		PlayerHelper.consumeAll(player, recipe.value().getInputItems());

		this.recipe = recipe;
		this.progress = getMaxProgress();
		this.setChanged();
	}

	public int getMaxProgress() {
		return 500;
	}

	public float getProgressPercent() {
		if (this.recipe == null || this.progress <= 0)
			return 0f;
		return 1f - ((float) this.progress / (float) getMaxProgress());
	}

	@Override
	public int size() {
		return 4;
	}
}