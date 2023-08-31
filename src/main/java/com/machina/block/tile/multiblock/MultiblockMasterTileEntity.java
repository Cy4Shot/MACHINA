package com.machina.block.tile.multiblock;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.machina.block.tile.MachinaTileEntity;
import com.machina.multiblock.Multiblock;
import com.machina.multiblock.MultiblockLoader;
import com.machina.util.StringUtils;
import com.machina.util.helper.BlockHelper;
import com.machina.util.math.VecUtil;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

public abstract class MultiblockMasterTileEntity extends MachinaTileEntity {

	public Multiblock mb;
	public boolean formed = false;
	public Set<BlockPos> parts = new HashSet<>();

	public MultiblockMasterTileEntity(TileEntityType<?> type) {
		super(type);

		this.mb = MultiblockLoader.INSTANCE.getMultiblock(getMultiblock());
	}

	public abstract ResourceLocation getMultiblock();

	public TranslationTextComponent getName() {
		return StringUtils.translateMultiblockComp(getMultiblock().getPath());
	}

	public void update() {

		deform();

		ValidateResult res = valid();
		this.formed = res.valid;
		if (res.valid) {
			this.parts = res.pos;
			for (BlockPos pos : parts) {
				BlockHelper.doWithTe(level, pos, MultiblockPartTileEntity.class, te -> {
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
			BlockHelper.doWithTe(level, pos, MultiblockPartTileEntity.class, te -> {
				te.deform();
			});
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		ListNBT poss = new ListNBT();
		this.parts.forEach(pos -> {
			poss.add(NBTUtil.writeBlockPos(pos));
		});

		tag.put("parts", poss);
		tag.putBoolean("formed", formed);
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag) {
		this.parts.clear();
		ListNBT cons = tag.getList("parts", Constants.NBT.TAG_COMPOUND);
		for (int j = 0; j < cons.size(); j++) {
			parts.add(NBTUtil.readBlockPos(cons.getCompound(j)));
		}
		this.formed = tag.getBoolean("formed");
		super.load(state, tag);
	}

	private ValidateResult valid() {
		int s = (int) Math.ceil((double) VecUtil.max(mb.size));
		BlockPos corner = findCorner(worldPosition, b -> mb.allowedBlock.contains(level.getBlockState(b).getBlock()),
				s * s * s);
		return validateAll(corner, mb.size);

	}

	// Loops aren't real they don't exist.
	private ValidateResult validateAll(BlockPos corner, Vector3i size) {
		ValidateResult res = validateDirection(corner, mb.size, Direction.SOUTH);
		if (!res.valid) {
			res = validateDirection(corner, mb.size, Direction.WEST);
			if (!res.valid) {
				res = validateDirection(corner, mb.size, Direction.NORTH);
				if (!res.valid) {
					res = validateDirection(corner, mb.size, Direction.EAST);
					if (!res.valid) {
						return ValidateResult.REJECT;
					}
				}
			}
		}
		return res;
	}

	private ValidateResult validateDirection(BlockPos corner, Vector3i size, Direction rotation) {
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

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (!this.formed)
			return LazyOptional.empty();
		return super.getCapability(cap, side);
	}
}