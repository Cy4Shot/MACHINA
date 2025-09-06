package com.machina.registration.init;

import com.machina.Machina;
import com.machina.world.functions.PlanetSurfaceRule.PlanetBiomeSecondBlockRuleSource;
import com.machina.world.functions.PlanetSurfaceRule.PlanetBiomeThirdBlockRuleSource;
import com.machina.world.functions.PlanetSurfaceRule.PlanetBiomeTopBlockRuleSource;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MaterialRuleInit {
    public static final DeferredRegister<Codec<? extends RuleSource>> MATERIAL_RULES = DeferredRegister
            .create(Registries.MATERIAL_RULE, Machina.MOD_ID);

    public static final RegistryObject<Codec<PlanetBiomeTopBlockRuleSource>> PLANET_TOP = MATERIAL_RULES
            .register("planet_top_block", PlanetBiomeTopBlockRuleSource.CODEC::codec);

    public static final RegistryObject<Codec<PlanetBiomeSecondBlockRuleSource>> PLANET_SECOND = MATERIAL_RULES
            .register("planet_second_block", PlanetBiomeSecondBlockRuleSource.CODEC::codec);

    public static final RegistryObject<Codec<PlanetBiomeThirdBlockRuleSource>> PLANET_THIRD = MATERIAL_RULES
            .register("planet_third_block", PlanetBiomeThirdBlockRuleSource.CODEC::codec);

}
