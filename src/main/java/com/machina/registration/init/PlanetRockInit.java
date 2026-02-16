package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.world.feature.rock.FloatingIslandRock;
import com.machina.world.feature.rock.ShroomRock;
import com.machina.world.feature.rock.WispRock;

import net.neoforged.neoforge.registries.DeferredRegister;

public class PlanetRockInit {
    public static final DeferredRegister<RockMaker> ROCKS = DeferredRegister.create(RegistryInit.ROCK_REGISTRY,
            Machina.MOD_ID);

    //@formatter:off
	public static final Supplier<WispRock> WISP = ROCKS.register("wisp", WispRock::new);
	public static final Supplier<ShroomRock> SHROOM = ROCKS.register("shroom", ShroomRock::new);
    public static final Supplier<FloatingIslandRock> FLOATING_ISLAND = ROCKS.register("floating_island", FloatingIslandRock::new);
	//@formatter:on
}
