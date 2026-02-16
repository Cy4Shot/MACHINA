package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.world.feature.tree.AcaciaTree;
import com.machina.world.feature.tree.ArchTree;
import com.machina.world.feature.tree.BayouTree;
import com.machina.world.feature.tree.BellMushroomTree;
import com.machina.world.feature.tree.BranchFunnelMushroomTree;
import com.machina.world.feature.tree.ConeTree;
import com.machina.world.feature.tree.DeadRadialBaobabTree;
import com.machina.world.feature.tree.FirTree;
import com.machina.world.feature.tree.JungleTree;
import com.machina.world.feature.tree.LollipopTree;
import com.machina.world.feature.tree.RadialBaobabTree;
import com.machina.world.feature.tree.SmallFirTree;

import net.neoforged.neoforge.registries.DeferredRegister;

public class PlanetTreeInit {
    public static final DeferredRegister<TreeMaker> TREES = DeferredRegister.create(RegistryInit.TREE_REGISTRY,
            Machina.MOD_ID);

    //@formatter:off
	public static final Supplier<RadialBaobabTree> RADIAL_BAOBAB = TREES.register("radial_baobab", RadialBaobabTree::new);
	public static final Supplier<DeadRadialBaobabTree> DEAD_RADIAL_BAOBAB = TREES.register("dead_radial_baobab", DeadRadialBaobabTree::new);
	public static final Supplier<ArchTree> ARCH = TREES.register("arch", ArchTree::new);
	public static final Supplier<FirTree> FIR = TREES.register("fir", FirTree::new);
	public static final Supplier<SmallFirTree> SMALL_FIR = TREES.register("small_fir", SmallFirTree::new);
	public static final Supplier<ConeTree> CONE = TREES.register("cone", ConeTree::new);
	public static final Supplier<LollipopTree> LOLLIPOP = TREES.register("lollipop", LollipopTree::new);
	public static final Supplier<AcaciaTree> ACACIA = TREES.register("acacia", AcaciaTree::new);
	public static final Supplier<BellMushroomTree> BELL_MUSHROOM = TREES.register("bell_mushroom", BellMushroomTree::new);
	public static final Supplier<BranchFunnelMushroomTree> BRANCH_FUNNEL_MUSHROOM = TREES.register("branch_funnel_mushroom", BranchFunnelMushroomTree::new);
    public static final Supplier<BayouTree> BAYOU = TREES.register("bayou", BayouTree::new);
    public static final Supplier<JungleTree> JUNGLE = TREES.register("jungle", JungleTree::new);
	//@formatter:on
}
