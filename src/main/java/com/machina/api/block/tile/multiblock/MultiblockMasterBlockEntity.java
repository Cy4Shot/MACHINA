package com.machina.api.block.tile.multiblock;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.tile.MachinaBlockEntity;
import com.machina.api.cap.fluid.MachinaTank;
import com.machina.api.multiblock.Multiblock;
import com.machina.api.multiblock.MultiblockLoader;
import com.machina.api.util.BlockHelper;
import com.machina.api.util.StringUtils;
import com.machina.api.util.math.VecUtil;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

/**
 * The base class for a multiblock master / controller.
 * 
 * Make sure to use the {@code formed} variable to check whether the multiblock has been
 * formed.
 * 
 * @author Cy4Shot
 * @since Machina 0.1.0
 */
public abstract class MultiblockMasterBlockEntity extends MachinaBlockEntity {

	public Multiblock mb;
	public boolean formed = false;
	public Set<BlockPos> parts = new HashSet<>();

	public MultiblockMasterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);

		this.mb = MultiblockLoader.INSTANCE.getMultiblock(getMultiblock());
	}

	/**
	 * Function that returns a resource location pointing towards the multiblock
	 * json. </br>
	 * </br>
	 * For example, {@code machina:test} points to
	 * {@code data/machina/multiblock/test.json}.
	 * 
	 * @return {@link ResourceLocation}: The RL pointing towards the multiblock
	 *         json.
	 */
	public abstract ResourceLocation getMultiblock();

	public Component getName() {
		return StringUtils.translateMultiblockComp(getMultiblock().getPath());
	}

	public void update() {

		deform();

		ValidateResult res = valid();
		this.formed = res.valid;
		if (res.valid) {
			this.parts = res.pos;
			for (BlockPos pos : parts) {
				BlockHelper.doWithTe(level, pos, MultiblockPartBlockEntity.class, te -> {
					te.form(worldPosition);
				});
				postValidate().accept(pos);
			}
		} else {
			this.parts = new HashSet<>();
		}

		this.setChanged();
	}

	public void deform() {
		for (BlockPos pos : parts) {
			BlockHelper.doWithTe(level, pos, MultiblockPartBlockEntity.class, te -> {
				te.deform();
			});
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		ListTag poss = new ListTag();
		this.parts.forEach(pos -> {
			poss.add(NbtUtils.writeBlockPos(pos));
		});

		tag.put("parts", poss);
		tag.putBoolean("formed", formed);
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		this.parts.clear();
		ListTag cons = tag.getList("parts", Tag.TAG_COMPOUND);
		for (int j = 0; j < cons.size(); j++) {
			parts.add(NbtUtils.readBlockPos(cons.getCompound(j)));
		}
		this.formed = tag.getBoolean("formed");
		super.load(tag);
	}

	private ValidateResult valid() {
		int s = (int) Math.ceil((double) VecUtil.max(mb.size));
		BlockPos corner = findCorner(worldPosition, b -> mb.allowedBlock.contains(level.getBlockState(b).getBlock()),
				s * s * s);
		return validateAll(corner, mb.size);

	}

	// Loops aren't real they don't exist.
	private ValidateResult validateAll(BlockPos corner, Vec3i size) {
		ValidateResult res = validateDirection(corner, size, Direction.SOUTH);
		if (!res.valid) {
			res = validateDirection(corner, size, Direction.WEST);
			if (!res.valid) {
				res = validateDirection(corner, size, Direction.NORTH);
				if (!res.valid) {
					res = validateDirection(corner, size, Direction.EAST);
					if (!res.valid) {
						return ValidateResult.REJECT;
					}
				}
			}
		}
		return res;
	}

	private ValidateResult validateDirection(BlockPos corner, Vec3i size, Direction rotation) {
		Set<BlockPos> poss = new HashSet<>();
		for (int x = 0; x < size.getX(); x++) {
			for (int y = 0; y < size.getY(); y++) {
				for (int z = 0; z < size.getZ(); z++) {
					BlockPos pos = corner.offset(x, y, z);
					BlockState state = level.getBlockState(pos);
					String key;
					switch (rotation.get2DDataValue()) {
					case 1: // West
						key = mb.structure[z][y][size.getX() - 1 - x];
						break;
					case 2: // North
						key = mb.structure[size.getX() - 1 - x][y][size.getZ() - 1 - z];
						break;
					case 3: // East
						key = mb.structure[size.getZ() - 1 - z][y][x];
						break;
					default: // South
						key = mb.structure[x][y][z];
					}
					if (pos.equals(worldPosition) && !key.equals(mb.controller_replacable)) {
						return ValidateResult.REJECT;
					}
					if (!key.equals(" ") && !mb.map.get(key).contains(state) && !pos.equals(worldPosition)) {
						return ValidateResult.REJECT;
					}
					if (!key.equals(" ") && !pos.equals(worldPosition))
						poss.add(pos);
				}
			}
		}
		return ValidateResult.accept(poss);
	}

	public Consumer<BlockPos> postValidate() {
		return p -> {
		};
	}

	private static BlockPos findCorner(BlockPos start, Predicate<BlockPos> checker, int maxCount) {
		Queue<BlockPos> openSet = new LinkedList<>();
		Set<BlockPos> traversed = new ObjectOpenHashSet<>();
		openSet.add(start.immutable());
		traversed.add(start.immutable());
		while (!openSet.isEmpty()) {
			BlockPos ptr = openSet.poll();
			if (traversed.size() >= maxCount) {
				return new BlockPos(VecUtil.minAll(traversed));
			}
			for (Direction side : Direction.values()) {
				BlockPos offset = ptr.relative(side);
				if (!traversed.contains(offset) && checker.test(offset)) {
					openSet.add(offset);
					traversed.add(offset);
				}
			}
		}
		return new BlockPos(VecUtil.minAll(traversed));
	}

	public static class ValidateResult {

		public static final ValidateResult REJECT = new ValidateResult(false, null);

		public final boolean valid;
		public final Set<BlockPos> pos;

		private ValidateResult(boolean valid, Set<BlockPos> pos) {
			this.valid = valid;
			this.pos = pos;
		}

		public static ValidateResult accept(Set<BlockPos> pos) {
			return new ValidateResult(true, pos);
		}
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (!this.formed)
			return LazyOptional.empty();
		return super.getCapability(cap, side);
	}

	@Override
	public int getContainerSize() {
		if (!this.formed)
			return 0;
		return super.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		if (!this.formed)
			return true;
		return super.isEmpty();
	}

	@Override
	public @NotNull ItemStack getItem(int pIndex) {
		if (!this.formed)
			return ItemStack.EMPTY;
		return super.getItem(pIndex);
	}

	@Override
	public @NotNull ItemStack removeItem(int pIndex, int pCount) {
		if (!this.formed)
			return ItemStack.EMPTY;
		return super.removeItem(pIndex, pCount);
	}

	@Override
	public @NotNull ItemStack removeItemNoUpdate(int pIndex) {
		if (!this.formed)
			return ItemStack.EMPTY;
		return super.removeItemNoUpdate(pIndex);
	}

	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		if (!this.formed)
			return;
		super.setItem(pIndex, pStack);
	}

	@Override
	public void clearContent() {
		if (!this.formed)
			return;
		super.clearContent();
	}

	@Override
	public int getEnergy() {
		if (!this.formed)
			return 0;
		return super.getEnergy();
	}

	@Override
	public int getMaxEnergy() {
		if (!this.formed)
			return 0;
		return super.getMaxEnergy();
	}

	@Override
	public float getEnergyProp() {
		if (!this.formed)
			return 0;
		return super.getEnergyProp();
	}

	@Override
	public MachinaTank getTank(int id) {
		if (!this.formed)
			return null;
		return super.getTank(id);
	}
}