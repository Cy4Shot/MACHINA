package com.cy4.machina.recipe.advanced_crafting.function;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.annotation.registries.recipe.RegisterACFunctionSerializer;
import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.capability.trait.IPlanetTraitCapability;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunction;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

@RegistryHolder
public class TraitOnlyFunction extends AdvancedCraftingFunction {

	@RegisterACFunctionSerializer("trait_only")
	public static final Serializer SERIALIZER = new Serializer();

	private final List<PlanetTrait> validTraits;
	private final boolean blacklist;
	private final boolean onlyOne;

	public TraitOnlyFunction(List<PlanetTrait> traits, boolean blacklist, boolean onlyOne) {
		this.validTraits = traits;
		this.blacklist = blacklist;
		this.onlyOne = onlyOne;
	}

	@Override
	public boolean matches(CraftingInventory inv, AdvancedCraftingRecipe recipe, World level) {
		if (!super.matches(inv, recipe, level)) { return false; }
		List<Boolean> match = new LinkedList<>();
		AtomicReference<IPlanetTraitCapability> capAtomic = new AtomicReference<>(null);
		level.getCapability(CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY).ifPresent(cap -> {
			capAtomic.set(cap);
			cap.getTraits().forEach(trait -> match.add(validTraits.contains(trait)));
		});
		return checkMatch(match, capAtomic.get());
	}

	private boolean checkMatch(List<Boolean> values, IPlanetTraitCapability cap) {
		if (blacklist) {
			if (onlyOne) {
				return !values.contains(false);
			} else {
				return cap == null || !cap.getTraits().containsAll(validTraits);
			}
		} else {
			if (onlyOne) {
				return values.contains(true);
			} else {
				return cap.getTraits().containsAll(validTraits);
			}
		}
	}

	@Override
	public void addJeiInfo(List<ITextComponent> tooltipList) {
		String msg = "This craft can only occure if %s:";
		if (blacklist) {
			msg = msg.replaceFirst("can only", "cannot");
		}
		msg = msg.replace("%s", onlyOne ? "at least one of the following traits are present" : "all of the following traits are present");
		tooltipList.add(new StringTextComponent(msg));
		validTraits.forEach(trait -> {
			TranslationTextComponent traitName = (TranslationTextComponent) trait.getName();
			traitName.setStyle(traitName.getStyle().withColor(TextFormatting.GOLD));
			tooltipList.add(traitName);
		});
		super.addJeiInfo(tooltipList);
	}

	public static final class Serializer extends AdvancedCraftingFunctionSerializer<TraitOnlyFunction> {

		@Override
		public TraitOnlyFunction deserialize(JsonObject obj) {
			List<PlanetTrait> traits = new LinkedList<>();
			if (obj.has("traits")) {
				JsonArray traitsObj = JSONUtils.getAsJsonArray(obj, "traits");
				traitsObj.forEach(elem -> {
					PlanetTrait trait = PlanetTrait.REGISTRY.getValue(new ResourceLocation(elem.getAsString()));
					if (trait != null) {
						traits.add(trait);
					}
				});
			}
			boolean onlyOne = JSONUtils.getAsBoolean(obj, "only_one", true);
			boolean blacklist = JSONUtils.getAsBoolean(obj, "blacklist", false);
			return new TraitOnlyFunction(traits, blacklist, onlyOne);
		}

	}

}
