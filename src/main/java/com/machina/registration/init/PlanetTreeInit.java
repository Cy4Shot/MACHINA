package com.machina.registration.init;

import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.world.feature.tree.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PlanetTreeInit {
    public static final DeferredRegister<TreeMaker> TREES = RegistryInit.TREES;

    //@formatter:off
	public static final RegistryObject<RadialBaobabTree> RADIAL_BAOBAB = TREES.register("radial_baobab", RadialBaobabTree::new);
	public static final RegistryObject<DeadRadialBaobabTree> DEAD_RADIAL_BAOBAB = TREES.register("dead_radial_baobab", DeadRadialBaobabTree::new);
	public static final RegistryObject<ArchTree> ARCH = TREES.register("arch", ArchTree::new);
	public static final RegistryObject<FirTree> FIR = TREES.register("fir", FirTree::new);
	public static final RegistryObject<SmallFirTree> SMALL_FIR = TREES.register("small_fir", SmallFirTree::new);
	public static final RegistryObject<ConeTree> CONE = TREES.register("cone", ConeTree::new);
	public static final RegistryObject<LollipopTree> LOLLIPOP = TREES.register("lollipop", LollipopTree::new);
	public static final RegistryObject<AcaciaTree> ACACIA = TREES.register("acacia", AcaciaTree::new);
	public static final RegistryObject<BellMushroomTree> BELL_MUSHROOM = TREES.register("bell_mushroom", BellMushroomTree::new);
	public static final RegistryObject<BranchFunnelMushroomTree> BRANCH_FUNNEL_MUSHROOM = TREES.register("branch_funnel_mushroom", BranchFunnelMushroomTree::new);
	//@formatter:on
}
