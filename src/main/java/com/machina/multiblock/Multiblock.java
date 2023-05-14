package com.machina.multiblock;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.util.math.vector.Vector3i;

public class Multiblock {
	public Vector3i size;
	public BlockState controller;
	public String controller_replacable;
	public Map<String, List<BlockState>> map;
	public Map<String, BlockState> renderMap;
	public List<BlockState> allowed;
	public Set<Block> allowedBlock;
	public String[][][] structure;
	public String[][][] renderStructure;

	public class MultiblockJsonInfo {
		public List<Integer> size;
		public String controller;
		public String controller_replacable;
		public Map<String, List<String>> blocks;
		public Map<String, String> render_blocks;
		public List<List<String>> structure;
		public List<List<String>> render;

		public Multiblock cast() {
			Multiblock mb = new Multiblock();
			mb.size = new Vector3i(size.get(0), size.get(1), size.get(2));
			try {
				mb.controller = new BlockStateParser(new StringReader(controller), true).parse(false).getState();
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
				mb.controller = null;
			}
			mb.map = blocks.entrySet().stream()
					.collect(Collectors.toMap(Entry::getKey, s -> s.getValue().stream().map(b -> {
						try {
							return new BlockStateParser(new StringReader(b), true).parse(false).getState();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
							return Blocks.AIR.defaultBlockState();
						}
					}).collect(Collectors.toList())));
			mb.renderMap = render_blocks.entrySet().stream().collect(Collectors.toMap(Entry::getKey, s -> {
				try {
					return new BlockStateParser(new StringReader(s.getValue()), true).parse(false).getState();
				} catch (CommandSyntaxException e) {
					e.printStackTrace();
					return Blocks.AIR.defaultBlockState();
				}
			}));

			mb.controller_replacable = controller_replacable;
			mb.structure = structure.stream()
					.map(l1 -> l1.stream().map(l2 -> l2.split("(?!^)")).toArray(String[][]::new))
					.toArray(String[][][]::new);
			mb.renderStructure = render.stream()
					.map(l1 -> l1.stream().map(l2 -> l2.split("(?!^)")).toArray(String[][]::new))
					.toArray(String[][][]::new);
			mb.allowed = mb.map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
			mb.allowedBlock = mb.allowed.stream().map(b -> b.getBlock()).collect(Collectors.toSet());
			return mb;
		}
	}

	public BlockState getRenderAtPos(Vector3i pos) {
		try {
			BlockState bs = renderMap.get(renderStructure[pos.getX()][pos.getY()][pos.getZ()]);
			return bs == null ? Blocks.AIR.defaultBlockState() : bs;
		} catch (IndexOutOfBoundsException e) {
			return Blocks.AIR.defaultBlockState();
		}
	}

	@Override
	public String toString() {
		return "Multiblock {" + "\n\t size = " + size.toShortString() + "\n\t controller = " + controller.toString()
				+ "\n\t controller_replacable = " + controller_replacable + "\n\t map = "
				+ Joiner.on(",").withKeyValueSeparator("=")
						.join(map.entrySet().stream().collect(Collectors.toMap(Entry::getKey,
								s -> s.getValue().stream().map(b -> b.toString()).collect(Collectors.toList()))))
				+ "\n\t structure = " + Arrays.deepToString(structure) + '}';
	}
}