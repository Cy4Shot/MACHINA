package com.machina.item.loot;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.machina.item.TintedItem;
import com.machina.util.server.PlanetHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class TintedLootModifier extends LootModifier {

	public TintedLootModifier(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		for (ItemStack stack : generatedLoot) {
			if (stack.getItem() instanceof TintedItem) {
				RegistryKey<World> dim = context.getLevel().dimension();
				if (PlanetHelper.isDimensionPlanet(dim)) {
					TintedItem.applyToStack(stack, PlanetHelper.getId(dim));
				} else {
					TintedItem.applyToStack(stack, -1);
				}
			}
		}
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<TintedLootModifier> {

		@Override
		public TintedLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
			return new TintedLootModifier(conditionsIn);
		}

		@Override
		public JsonObject write(TintedLootModifier instance) {
			return new JsonObject();
		}
	}
}