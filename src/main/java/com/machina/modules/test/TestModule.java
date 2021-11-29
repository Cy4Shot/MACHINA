package com.machina.modules.test;

import static com.machina.api.ModIDs.MACHINA;

import com.machina.api.annotation.RL;
import com.machina.api.module.IModule;
import com.machina.api.module.Module;
import com.machina.api.module.ModuleHelper;
import com.machina.api.registry.annotation.AutoBlockItem;
import com.machina.api.registry.annotation.RegisterBlock;
import com.machina.api.registry.annotation.RegisterContainerType;
import com.machina.api.registry.annotation.RegisterTileEntityType;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;

import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Module(id = @RL(modid = MACHINA, path = "test"))
public class TestModule extends ModuleHelper implements IModule {

	@AutoBlockItem
	@RegisterBlock("test_block")
	public static final TestBlock TEST_BLOCK = new TestBlock();
	
	@RegisterTileEntityType("test_tile")
	public static final TileEntityType<TestTileEntity> TEST_TE_TYPE = TileEntityType.Builder.of(TestTileEntity::new, TEST_BLOCK).build(null);
	
	@RegisterContainerType("test_container")
	public static final ContainerType<TestContainer> TEST_CONTAINER_TYPE = IForgeContainerType.create(TestContainer::new);
	
	@Override
	public void onClientSetup(FMLClientSetupEvent event) {
		registerScreen(TEST_CONTAINER_TYPE, TestScreen::new);
	}
}
