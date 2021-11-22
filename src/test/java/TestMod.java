import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegistryAnnotationProcessor;
import com.cy4.machina.api.annotation.registries.RegistryHolder;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("test_mod")
@RegistryHolder(modid = "test_mod")
public class TestMod {

	public TestMod() {
		RegistryAnnotationProcessor annHelper = new RegistryAnnotationProcessor("test_mod");
		annHelper.register(FMLJavaModLoadingContext.get().getModEventBus());
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@RegisterItem("test_item")
	public static final Item TEST = new Item(new Item.Properties().tab(ItemGroup.TAB_COMBAT));
}
