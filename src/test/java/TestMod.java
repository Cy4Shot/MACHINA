/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

import java.util.UUID;

import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegistryAnnotationProcessor;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.planet.attribute.PlanetAttributeType;

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
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("test_mod")
@RegistryHolder(modid = "test_mod")
@Mod.EventBusSubscriber(modid = "test_mod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class TestMod {

	public static final RegistryAnnotationProcessor ANN_HELPER = new RegistryAnnotationProcessor("test_mod");

	public TestMod() {
		ANN_HELPER.register(FMLJavaModLoadingContext.get().getModEventBus());
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(PlanetAttributeType.class, this::testRegisterAttribute);
		MinecraftForge.EVENT_BUS.addListener(TestMod::onEntityUseTick);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public final void testRegisterAttribute(final RegistryEvent.Register<PlanetAttributeType<?>> event) {
		event.getRegistry().register(new PlanetAttributeType<>("TEST", IntNBT::valueOf, nbt -> 0).setRegistryName(new ResourceLocation("test_mod", "yes")));
	}

	public static void onEntityUseTick(LivingEntityUseItemEvent.Tick event) {
		System.out.println("YES");
	}

	@RegisterItem("test_helmet")
	public static final TestArmourItem TEST_HELMET = new TestArmourItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD,
			new Item.Properties().tab(ItemGroup.TAB_COMBAT));

	@RegisterItem("test")
	public static final Item TEST_ITEM = new TestItem(new Item.Properties().tab(ItemGroup.TAB_BREWING));

	public static final class TestItem extends Item {

		public TestItem(Properties pProperties) {
			super(pProperties);
		}

		@Override
		public int getUseDuration(ItemStack pStack) {
			return 72000;
		};

		@Override
		public void onUseTick(World pLevel, net.minecraft.entity.LivingEntity pLivingEntity, ItemStack pStack,
				int pCount) {
			System.out.println("yes");
		}
		
		@Override
		public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
			pPlayer.startUsingItem(pHand);
			return super.use(pLevel, pPlayer, pHand);
		}

	}

	public static class TestArmourItem extends ArmorItem {

		public TestArmourItem(IArmorMaterial pMaterial, EquipmentSlotType pSlot, Properties pProperties) {
			super(pMaterial, pSlot, pProperties);
		}

		@Override
		public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
			UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[] {
					UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
					UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
					UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
					UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
			};
			pPlayer.getItemInHand(pHand).addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
					ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], () -> "modifier", 1.0, Operation.ADDITION), slot);
			return super.use(pLevel, pPlayer, pHand);
		}

	}
}
