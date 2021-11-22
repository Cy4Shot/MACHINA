import java.util.UUID;

import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegistryAnnotationProcessor;
import com.cy4.machina.api.annotation.registries.RegistryHolder;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("test_mod")
@RegistryHolder(modid = "test_mod")
@Mod.EventBusSubscriber(modid = "test_mod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class TestMod {

	public TestMod() {
		RegistryAnnotationProcessor annHelper = new RegistryAnnotationProcessor("test_mod");
		annHelper.register(FMLJavaModLoadingContext.get().getModEventBus());
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@RegisterItem("test_helmet")
	public static final TestArmourItem TEST_HELMET = new TestArmourItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT));
	
	public static class TestArmourItem extends ArmorItem {

		public TestArmourItem(IArmorMaterial pMaterial, EquipmentSlotType pSlot, Properties pProperties) {
			super(pMaterial, pSlot, pProperties);
		}
		
		@Override
		public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
			UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
			pPlayer.getItemInHand(pHand).addAttributeModifier(Attributes.ARMOR, new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], () -> "modifier", 1.0, Operation.ADDITION), slot);
			return super.use(pLevel, pPlayer, pHand);
		}
		
	}
}
