package com.machina.api.multiblock;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.machina.api.util.loader.JsonInfo;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class Multiblock {
	public Vec3i size;
	public BlockState controller;
	public Vec3i controller_render_pos;
	public Map<String, List<BlockState>> map;
	public Set<Block> allowedBlock;
	public String[][][] structure;

	public static class MultiblockJsonInfo implements JsonInfo<Multiblock> {
		public List<Integer> size;
		public String controller;
		public Map<String, List<String>> blocks;
		public List<List<String>> structure;

		public Multiblock cast() {
			Multiblock mb = new Multiblock();
			mb.size = new Vec3i(size.get(0), size.get(1), size.get(2));
			try {
				mb.controller = parse(controller);
			} catch (CommandSyntaxException e) {
				throw new IllegalArgumentException("No controller found in structure (" + controller + ")", e);
			}
			mb.map = blocks.entrySet().stream().collect(Collectors.toMap(Entry::getKey, s -> s.getValue().stream().map(v -> {
				try {
					return parse(v);
				} catch (CommandSyntaxException e) {
					throw new IllegalArgumentException("Invalid block state for key " + s.getKey() + ": " + v, e);
				}
			}).collect(Collectors.toList())));

			mb.structure = new String[mb.size.getX()][mb.size.getY()][mb.size.getZ()];
			for (int y = 0; y < mb.size.getY(); y++) {
				for (int z = 0; z < mb.size.getZ(); z++) {
					String row = structure.get(y).get(z);
					String[] chars = row.split("(?!^)");
					for (int x = 0; x < mb.size.getX(); x++) {
						mb.structure[x][mb.size.getY() - 1 - y][z] = chars[x];
					}
				}
			}

			mb.controller_render_pos = null;
			for (int x = 0; x < mb.size.getX(); x++) {
				for (int y = 0; y < mb.size.getY(); y++) {
					for (int z = 0; z < mb.size.getZ(); z++) {
						if (mb.structure[x][y][z].equals("!")) {
							mb.controller_render_pos = new Vec3i(x, y, z);
						}
					}
				}
			}
			if (mb.controller_render_pos == null) {
				throw new IllegalArgumentException("No controller render position found in structure (marked with !)");
			}

			mb.allowedBlock = mb.map.values().stream().flatMap(List::stream).map(BlockBehaviour.BlockStateBase::getBlock)
					.collect(Collectors.toSet());
			mb.allowedBlock.add(mb.controller.getBlock());
			return mb;
		}
	}

	private static BlockState parse(String value) throws CommandSyntaxException {
		return BlockStateParser
				.parseForBlock(RegistryLayer.createRegistryAccess().compositeAccess().lookup(Registries.BLOCK).get(),
						value, true)
				.blockState();
	}

	public BlockState getRenderAtPos(Vec3i pos) {
		try {
			if (controller_render_pos != null && controller_render_pos.equals(pos)) {
				return controller;
			}
			String key = structure[pos.getX()][pos.getY()][pos.getZ()];
			List<BlockState> bs = map.get(key);
			return bs == null || bs.isEmpty() ? Blocks.AIR.defaultBlockState() : bs.get(0);
		} catch (IndexOutOfBoundsException e) {
			return Blocks.AIR.defaultBlockState();
		}
	}

	public boolean matches(String key, BlockState state) {
		List<BlockState> expected = map.get(key);
		if (expected == null || expected.isEmpty()) {
			return false;
		}
		return expected.stream().anyMatch(bs -> bs.getBlock().equals(state.getBlock()));
	}

	@Override
	public String toString() {
		return "Multiblock {" + "\n\t size = " + size.toString() + "\n\t map = "
				+ Joiner.on(",").withKeyValueSeparator("=").join(map.entrySet().stream().collect(Collectors
						.toMap(Entry::getKey, s -> s.getValue().stream().map(BlockState::toString).collect(Collectors.toList()))))
				+ "\n\t structure = " + Arrays.deepToString(structure) + '}';
	}
}
