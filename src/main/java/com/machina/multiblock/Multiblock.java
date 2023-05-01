package com.machina.multiblock;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.util.math.vector.Vector3i;

public class Multiblock {
	public Vector3i size;
	public BlockState controller;
	public String controller_replacable;
	public Map<String, List<BlockState>> map;
	public List<BlockState> allowed;
	public String[][][] structure;

	public class MultiblockJsonInfo {
		public List<Integer> size;
		public String controller;
		public String controller_replacable;
		public Map<String, List<String>> blocks;
		public List<List<String>> structure;

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
							return null;
						}
					}).collect(Collectors.toList())));
			mb.controller_replacable = controller_replacable;
			mb.structure = structure.stream()
					.map(l1 -> l1.stream().map(l2 -> l2.split("(?!^)")).toArray(String[][]::new))
					.toArray(String[][][]::new);
			mb.allowed = mb.map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
			return mb;
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