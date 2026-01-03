package com.machina.registration.init;

import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.world.feature.rock.FloatingIslandRock;
import com.machina.world.feature.rock.ShroomRock;
import com.machina.world.feature.rock.WispRock;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PlanetRockInit {
    public static final DeferredRegister<RockMaker> ROCKS = RegistryInit.ROCKS;

    //@formatter:off
	public static final RegistryObject<WispRock> WISP = ROCKS.register("wisp", WispRock::new);
	public static final RegistryObject<ShroomRock> SHROOM = ROCKS.register("shroom", ShroomRock::new);
    public static final RegistryObject<FloatingIslandRock> FLOATING_ISLAND = ROCKS.register("floating_island", FloatingIslandRock::new);
	//@formatter:on
}
