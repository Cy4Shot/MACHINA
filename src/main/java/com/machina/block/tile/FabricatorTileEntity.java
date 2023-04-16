package com.machina.block.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.machina.block.container.FabricatorContainer;
import com.machina.block.tile.base.BaseLockableTileEntity;
import com.machina.blueprint.Blueprint;
import com.machina.item.BlueprintItem;
import com.machina.recipe.FabricatorRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityType;

public class FabricatorTileEntity extends BaseLockableTileEntity {

	public FabricatorTileEntity(TileEntityType<?> type) {
		super(type, 18);
	}

	public FabricatorTileEntity() {
		this(TileEntityInit.FABRICATOR.get());
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new FabricatorContainer(id, player, this);
	}

	public void fabricate() {
		if (getReqItems().size() == 0) {
			Blueprint bp = BlueprintItem.get(this.getItem(0));
			String id = bp.getId();
			for (IRecipe<?> r : RecipeInit.getRecipes(RecipeInit.FABRICATOR_RECIPE, this.level.getRecipeManager())
					.values()) {
				FabricatorRecipe recipe = (FabricatorRecipe) r;
				if (recipe.blueprintId().equals(id)) {
					recipe.items().forEach(item -> {
						this.remove(item.getItem(), item.getCount());
					});
					this.setItem(17, bp.getItem());
					return;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<ItemStack> getReqItems() {
		if (this.getItem(0).isEmpty())
			return Collections.EMPTY_LIST;

		if (!(this.getItem(0).getItem() instanceof BlueprintItem))
			return Collections.EMPTY_LIST;

		Blueprint bp = BlueprintItem.get(this.getItem(0));
		String id = bp.getId();

		for (IRecipe<?> r : RecipeInit.getRecipes(RecipeInit.FABRICATOR_RECIPE, this.level.getRecipeManager())
				.values()) {
			FabricatorRecipe recipe = (FabricatorRecipe) r;
			if (recipe.blueprintId().equals(id)) {
				List<ItemStack> req = new ArrayList<>();
				for (ItemStack stack : recipe.items()) {
					int count = count(stack.getItem());
					if (count < stack.getCount()) {
						req.add(new ItemStack(stack.getItem(), stack.getCount() - count));
					}
				}
				return req;
			}
		}

		return Collections.EMPTY_LIST;
	}

	public int count(Item item) {
		int total = 0;
		for (ItemStack stack : this.items.subList(1, 17).stream().filter(stack -> stack.getItem().equals(item))
				.collect(Collectors.toList())) {
			total += stack.getCount();
		}
		return total;
	}

	public void remove(Item item, int count) {
		int total = 0;
		for (int i = 1; i < 17; i++) {
			ItemStack stack = this.items.get(i);
			if (!stack.getItem().equals(item))
				continue;
			int rem = count - total;
			if (stack.getCount() < rem) {
				total += stack.getCount();
				this.removeItem(i, stack.getCount());
				if (count - total == 0)
					return;
			} else {
				this.removeItem(i, rem);
				return;
			}
		}
	}
}