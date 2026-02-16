package com.machina.registration.init;

import com.machina.Machina;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class KeyBindingInit {
    public static final String CATEGORY = "key.categories." + Machina.MOD_ID;

    public static final KeyMapping STARCHART_KEY = new KeyMapping("key." + Machina.MOD_ID + ".starchart_key",
            KeyConflictContext.IN_GAME, InputConstants.getKey(InputConstants.KEY_P, -1), CATEGORY);
}
