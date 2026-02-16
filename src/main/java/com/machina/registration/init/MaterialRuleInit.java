package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.world.functions.PlanetSurfaceRule.PlanetBiomeSecondBlockRuleSource;
import com.machina.world.functions.PlanetSurfaceRule.PlanetBiomeThirdBlockRuleSource;
import com.machina.world.functions.PlanetSurfaceRule.PlanetBiomeTopBlockRuleSource;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MaterialRuleInit {
    public static final DeferredRegister<MapCodec<? extends RuleSource>> MATERIAL_RULES = DeferredRegister
            .create(Registries.MATERIAL_RULE, Machina.MOD_ID);

    public static final Supplier<MapCodec<PlanetBiomeTopBlockRuleSource>> PLANET_TOP = MATERIAL_RULES
            .register("planet_top_block", PlanetBiomeTopBlockRuleSource.CODEC::codec);

    public static final Supplier<MapCodec<PlanetBiomeSecondBlockRuleSource>> PLANET_SECOND = MATERIAL_RULES
            .register("planet_second_block", PlanetBiomeSecondBlockRuleSource.CODEC::codec);

    public static final Supplier<MapCodec<PlanetBiomeThirdBlockRuleSource>> PLANET_THIRD = MATERIAL_RULES
            .register("planet_third_block", PlanetBiomeThirdBlockRuleSource.CODEC::codec);
}
