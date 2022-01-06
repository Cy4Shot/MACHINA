package com.machina.api.registry;

import com.machina.api.ModIDs;
import com.machina.api.annotation.ChangedByReflection;
import com.machina.api.planet.attribute.PlanetAttributeType;
import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.helper.ClassHelper;
import com.machina.api.util.helper.CustomRegistryHelper;
import com.machina.api.util.objects.TargetField;
import com.matyrobbrt.lib.registry.annotation.RegisterCustomRegistry;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Optional;

@RegistryHolder(modid = ModIDs.MACHINA)
public class MachinaRegistries {

    @ChangedByReflection(when = "commonSetup (when the registry is built)")
    public static final IForgeRegistry<PlanetAttributeType<?>> PLANET_ATTRIBUTE_TYPES = null;

    @ChangedByReflection(when = "commonSetup (when the registry is built)")
    public static final IForgeRegistry<PlanetTrait> PLANET_TRAITS = null;

    @ChangedByReflection(when = "commonSetup (when the registry is built)")
    public static final IForgeRegistry<AdvancedCraftingFunctionSerializer<?>> ADVANCED_CRAFTING_FUNCTION_SERIALIZERS = null;

    @RegisterCustomRegistry
    public static void createRegistries(RegistryEvent.NewRegistry event) {
        CustomRegistryHelper.<PlanetAttributeType<?>>registerRegistry(
                new TargetField(MachinaRegistries.class, "PLANET_ATTRIBUTE_TYPES"),
                ClassHelper.withWildcard(PlanetAttributeType.class), new MachinaRL("planet_attribute_type"),
                Optional.empty(), Optional.empty());

        CustomRegistryHelper.<PlanetTrait>registerRegistry(new TargetField(MachinaRegistries.class, "PLANET_TRAITS"),
                PlanetTrait.class, new MachinaRL("planet_trait"), Optional.of(new MachinaRL("none")),
                Optional.of(new MachinaRL("planet_trait_registry")));

        CustomRegistryHelper.<AdvancedCraftingFunctionSerializer<?>>registerRegistry(new TargetField(MachinaRegistries.class, "ADVANCED_CRAFTING_FUNCTION_SERIALIZERS"),
                ClassHelper.withWildcard(AdvancedCraftingFunctionSerializer.class),
                new MachinaRL("advanced_crafting_function_serializer"), Optional.of(new MachinaRL("no_function")));
    }
}
