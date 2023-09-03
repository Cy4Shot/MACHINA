package com.machina.api;

import com.machina.Machina;
import net.minecraftforge.fml.ModList;

/**
 * @author Swacky
 * -
 * This class is part of the api package and should lay out the standards for other mods building on the Machina mod
 * Defining constants and general rulesets for other API users' use: the {@link MachinaConstants} class
 */
public class MachinaConstants {
    /**
     * The constant used for Mod ID externally (a forwarding to the {@link Machina} class' MOD_ID field)
     */
    public static final String MODID = Machina.MOD_ID;

    /**
     * The version of Machina currently running
     */
    public static final String MOD_VERSION = ModList.get().getModFileById(MODID).versionString();

    /**
     * The version of Machina currently running for display purposes
     */
    public static final String DISPLAY_MOD_VERSION = Machina.getVersion();
}
