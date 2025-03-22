package com.machina.compat.jei;

import org.jetbrains.annotations.NotNull;

import com.machina.api.util.MachinaRL;
import com.machina.client.screen.menu.connector.FluidPipeScreen;
import com.machina.client.screen.menu.item.AdvancedItemFilterScreen;
import com.machina.client.screen.menu.item.FluidFilterScreen;
import com.machina.client.screen.menu.item.ItemFilterScreen;
import com.machina.compat.jei.base.MachinaGhostHandler;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class MachinaJei implements IModPlugin {

	public static final ResourceLocation UID = new MachinaRL("machina");

	@Override
	public @NotNull ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration reg) {
		//@formatter:off
		reg.addGhostIngredientHandler(ItemFilterScreen.class, new MachinaGhostHandler<ItemFilterScreen>());
		reg.addGhostIngredientHandler(AdvancedItemFilterScreen.class, new MachinaGhostHandler<AdvancedItemFilterScreen>());
		reg.addGhostIngredientHandler(FluidFilterScreen.class, new MachinaGhostHandler<FluidFilterScreen>());
		reg.addGhostIngredientHandler(FluidPipeScreen.class, new MachinaGhostHandler<FluidPipeScreen>());
		//@formatter:on
	}
}
