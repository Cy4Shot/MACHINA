package com.cy4.machina.api.capability.planet_data;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.PlanetUtils;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.api.util.helper.StarchartHelper;
import com.cy4.machina.util.MachinaRL;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Class containing the {@link CapabilityPlanetData#PLANET_DATA_CAPABILITY}.
 * Calls to the capability <strong>should</strong> be put through
 * {@link StarchartHelper}
 * 
 * @author matyrobbrt
 *
 */
public final class CapabilityPlanetData {

	/**
	 * Do not directly use. Calls to the capability <strong>should</strong> be put
	 * through {@link StarchartHelper}
	 */
	@CapabilityInject(IPlanetDataCapability.class)
	public static Capability<IPlanetDataCapability> PLANET_DATA_CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IPlanetDataCapability.class, new Storage(),
				DefaultPlanetDataCapability::new);
	}

	public static final class Storage implements IStorage<IPlanetDataCapability> {

		@Override
		public INBT writeNBT(Capability<IPlanetDataCapability> capability, IPlanetDataCapability instance,
				Direction side) {
			return serialize(instance);
		}

		@Override
		public void readNBT(Capability<IPlanetDataCapability> capability, IPlanetDataCapability instance,
				Direction side, INBT inbt) {
			CompoundNBT nbt = (CompoundNBT) inbt;
			deserialize(nbt, instance);

		}

		public static CompoundNBT serialize(IPlanetDataCapability instance) {
			CompoundNBT tag = new CompoundNBT();

			CompoundNBT traitsNBT = new CompoundNBT();
			traitsNBT.putInt("size", instance.getTraits().size());
			for (int i = 0; i < instance.getTraits().size(); i++) {
				traitsNBT.putString(String.valueOf(i), instance.getTraits().get(i).getRegistryName().toString());
			}

			tag.put("traits", traitsNBT);
			tag.putString("name", instance.getName());
			return tag;

		}

		public static void deserialize(CompoundNBT nbt, IPlanetDataCapability instance) {
			if (nbt.contains("traits")) {
				CompoundNBT traitsNbt = nbt.getCompound("traits");
				int size = traitsNbt.getInt("size");
				for (int i = 0; i < size; i++) {
					if (traitsNbt.contains(String.valueOf(i))) {
						instance.addTrait(PlanetTrait.REGISTRY
								.getValue(new ResourceLocation(traitsNbt.getString(String.valueOf(i)))));
					}
				}
			}
			if (nbt.contains("name")) {
				instance.setName(nbt.getString("name"));
			}
		}

		public static void transferData(IPlanetDataCapability provider, IPlanetDataCapability destination) {
			destination.clear();
			deserialize(serialize(provider), destination);
		}

	}

	@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
	public static final class EventHandler {

		@SubscribeEvent
		public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
			// pretty sure that it is only put on the server if i don't check for client
			// side
			if (event.getObject().isClientSide()) {
				attachPlanetTraitCap(event);
			} else {
				attachPlanetTraitCap(event);
			}
		}

		private static void attachPlanetTraitCap(AttachCapabilitiesEvent<World> event) {
			if (PlanetUtils.isDimensionPlanet(event.getObject().dimension())) {
				PlanetDataCapabilityProvider provider = new PlanetDataCapabilityProvider();
				event.addCapability(new MachinaRL("planet_data"), provider);
				event.addListener(provider::invalidate);
			}
		}
	}

}
