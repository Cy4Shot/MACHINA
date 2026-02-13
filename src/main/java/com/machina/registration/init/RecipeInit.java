package com.machina.registration.init;

import com.machina.Machina;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipe.MachinaRecipeSerializer;
import com.machina.api.recipe.MachinaRecipe.RecipeFactory;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.api.recipe.MachinaRecipeType;
import com.machina.api.util.MachinaRL;
import com.machina.block.entity.machine.*;
import com.machina.compat.jei.JeiRecipeRegistrar;
import com.machina.recipe.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeInit {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister
            .create(ForgeRegistries.RECIPE_TYPES, Machina.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
            .create(ForgeRegistries.RECIPE_SERIALIZERS, Machina.MOD_ID);
    public static final List<MachinaRecipeMaps<?>> MAPS = new ArrayList<>();
    public static final List<RecipeRegistryObject<?>> RECIPES = new ArrayList<>();

    //@formatter:off
	public static final RecipeRegistryObject<GrinderBlockEntity> GRINDER =
			register("grinder", BlockInit.GRINDER, GrinderRecipeMaps.INSTANCE, 0, 160);
	public static final RecipeRegistryObject<CompressorBlockEntity> COMPRESSOR =
			register("compressor", BlockInit.COMPRESSOR, CompressorRecipeMaps.INSTANCE, 16, 160);
	public static final RecipeRegistryObject<MelterBlockEntity> MELTER =
			register("melter", BlockInit.MELTER, MelterRecipeMaps.INSTANCE, 32, 160);
	public static final RecipeRegistryObject<SolidifierBlockEntity> SOLIDIFIER =
			register("solidifier", BlockInit.SOLIDIFIER, SolidifierRecipeMaps.INSTANCE, 48, 160);
	public static final RecipeRegistryObject<ReactionChamberBlockEntity> REACTION_CHAMBER =
			register("reaction_chamber", BlockInit.REACTION_CHAMBER, ReactionChamberRecipeMaps.INSTANCE, 64, 160);
	public static final RecipeRegistryObject<ComposterVatBlockEntity> COMPOSTER_VAT =
			register("composter_vat", BlockInit.COMPOSTER_VAT, ComposterVatRecipeMaps.INSTANCE, 80, 160);
	public static final RecipeRegistryObject<SawmillBlockEntity> SAWMILL =
			register("sawmill", BlockInit.SAWMILL, SawmillRecipeMaps.INSTANCE, 96, 160);
	public static final RecipeRegistryObject<ElectrolyzerBlockEntity> ELECTROLYZER =
			register("electrolyzer", BlockInit.ELECTROLYZER, ElectrolyzerRecipeMaps.INSTANCE, 112, 160);
	public static final RecipeRegistryObject<RocketPartBenchBlockEntity> ROCKET_PART_BENCH =
			register("rocket_part_bench", BlockInit.ROCKET_PART_BENCH, RocketPartBenchRecipeMaps.INSTANCE, 128, 160);
	//@formatter:on

    public static class RecipeRegistryObject<C extends Container> {

        private final ResourceLocation id;
        private final RegistryObject<MachinaRecipeType<C>> type;
        private final RecipeFactory<MachinaRecipe<C>> factory;
        private final RegistryObject<MachinaRecipeSerializer<C>> serializer;
        private final MachinaRecipeMaps<C> mapInstance;
        private final RegistryObject<? extends Block> block;
        private final JeiRecipeRegistrar<C> jei;

        public RecipeRegistryObject(ResourceLocation id, RegistryObject<MachinaRecipeType<C>> type,
                                    RecipeFactory<MachinaRecipe<C>> factory, RegistryObject<MachinaRecipeSerializer<C>> serializer,
                                    MachinaRecipeMaps<C> mapInstance, RegistryObject<? extends Block> block, int x, int y) {
            this.id = id;
            this.type = type;
            this.factory = factory;
            this.serializer = serializer;
            this.mapInstance = mapInstance;
            this.block = block;
            if (ModList.get().isLoaded("jei")) {
                this.jei = new JeiRecipeRegistrar<>(this, block, x, y);
            } else {
                this.jei = null;
            }
        }

        public ResourceLocation id() {
            return id;
        }

        public RegistryObject<MachinaRecipeType<C>> type() {
            return type;
        }

        public RecipeFactory<MachinaRecipe<C>> factory() {
            return factory;
        }

        public RegistryObject<MachinaRecipeSerializer<C>> serializer() {
            return serializer;
        }

        public MachinaRecipeMaps<C> maps() {
            return mapInstance;
        }

        public RegistryObject<? extends Block> block() {
            return block;
        }

        public JeiRecipeRegistrar<C> jei() {
            return jei;
        }

        public String getTranslationKey() {
            return id.getNamespace() + ".recipe." + id.getPath();
        }
    }

    private static <C extends Container> RecipeRegistryObject<C> register(String name,
                                                                          RegistryObject<? extends Block> block, MachinaRecipeMaps<C> mapInstance, int x, int y) {
        ResourceLocation id = new MachinaRL(name);
        RegistryObject<MachinaRecipeType<C>> type = RECIPE_TYPES.register(name,
                () -> new MachinaRecipeType<>(id, mapInstance.getFlags()));

        // Create an anonymous factory for the recipe
        RecipeFactory<MachinaRecipe<C>> factory = (loc, energy, time, pressure, temperature, periodicConsumption, inputItems, inputFluids, outputItems, outputFluids) -> new MachinaRecipe<>(loc, energy, time, pressure, temperature, periodicConsumption, inputItems,
                inputFluids, outputItems, outputFluids) {
            @Override
            public @NotNull RecipeType<MachinaRecipe<C>> getType() {
                return type.get();
            }
        };

        RegistryObject<MachinaRecipeSerializer<C>> serializer = RECIPE_SERIALIZERS.register(name,
                () -> new MachinaRecipeSerializer<>(type));
        RecipeRegistryObject<C> obj = new RecipeRegistryObject<>(id, type, factory, serializer, mapInstance, block, x,
                y);

        MAPS.add(mapInstance);
        RECIPES.add(obj);
        return obj;
    }
}
