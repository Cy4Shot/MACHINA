package com.machina.multiblock;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.Registry;

public class Multiblock {

	public Vector3i size;
	public Block controller;
	public List<Block> controller_replacable;
	public Map<String, List<Block>> map;
	public List<List<List<String>>> structure;

	public class MultiblockData {
		public List<Integer> size;
		public String controller;
		public String controller_replacable;
		public Map<String, List<String>> blocks;
		public List<List<String>> structure;

		public Multiblock cast() {
			Multiblock mb = new Multiblock();
			mb.size = new Vector3i(size.get(0), size.get(1), size.get(2));
			mb.controller = Registry.BLOCK.get(new ResourceLocation(controller));
			mb.map = blocks.entrySet().stream().collect(Collectors.toMap(Entry::getKey, s -> s.getValue().stream()
					.map(b -> Registry.BLOCK.get(new ResourceLocation(b))).collect(Collectors.toList())));
			mb.controller_replacable = mb.map.get(controller_replacable);
			mb.structure = structure.stream()
					.map(l1 -> l1.stream().map(l2 -> Arrays.asList(l2.split("(?!^)"))).collect(Collectors.toList()))
					.collect(Collectors.toList());
			return mb;
		}
	}

	@Override
	public String toString() {
		return "Multiblock {" + "\n\t size = " + size.toShortString() + "\n\t controller = " + controller.toString()
				+ "\n\t controller_replacable = "
				+ Arrays.toString(controller_replacable.stream().map(c -> c.toString()).toArray()) + "\n\t map = "
				+ Joiner.on(",").withKeyValueSeparator("=")
						.join(map.entrySet().stream().collect(Collectors.toMap(Entry::getKey,
								s -> s.getValue().stream().map(b -> b.toString()).collect(Collectors.toList()))))
				+ "\n\t structure = "
				+ Arrays.deepToString(structure.stream()
						.map(u1 -> u1.stream().map(u2 -> u2.toArray(new String[0])).toArray(String[][]::new))
						.toArray(String[][][]::new))
				+ '}';
	}
}
