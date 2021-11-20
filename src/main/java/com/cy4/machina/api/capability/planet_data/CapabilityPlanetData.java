package com.cy4.machina.api.capability.planet_data;

import java.util.concurrent.atomic.AtomicBoolean;

import com.cy4.machina.Machina;
import com.cy4.machina.api.events.planet.PlanetEvent;
import com.cy4.machina.api.network.BaseNetwork;
import com.cy4.machina.api.planet.PlanetUtils;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.network.MachinaNetwork;
import com.cy4.machina.network.message.to_client.SyncTraitsCapabilityMessage;
import com.cy4.machina.util.MachinaRL;

import net.minecraft.nbt.CollectionNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public final class CapabilityPlanetData {

	@CapabilityInject(IPlanetDataCapability.class)
	public static Capability<IPlanetDataCapability> PLANET_DATA_CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IPlanetDataCapability.class, new Storage(),
				DefaultPlanetDataCapability::new);
	}

	public static boolean worldHasTrait(World world, PlanetTrait trait) {
		AtomicBoolean value = new AtomicBoolean(false);
		world.getCapability(PLANET_DATA_CAPABILITY).ifPresent(cap -> value.set(cap.getTraits().contains(trait)));
		return value.get();
	}

	/**
	 * Call in order to sync the traits from the capability to all the players that
	 * are connected to the server
	 *
	 * @param world
	 */
	public static void syncCapabilityWithClients(World world) {
		world.getCapability(PLANET_DATA_CAPABILITY)
				.ifPresent(cap -> BaseNetwork.sendToAll(MachinaNetwork.CHANNEL,
						new SyncTraitsCapabilityMessage(cap, world.dimension().location())));
	}

	/**
	 * This method adds the specified traits to the world, whilst also calling
	 * {@link #syncCapabilityWithClients(World)}<br>
	 * It is preferred to use this method instead of
	 * {@link IPlanetDataCapability#addTrait(PlanetTrait)}
	 *
	 * @param level
	 * @param traits
	 */
	public static void addTrait(World level, PlanetTrait... traits) {
		level.getCapability(PLANET_DATA_CAPABILITY).ifPresent(cap -> {
			for (PlanetTrait trait : traits) {
				if (!PlanetEvent.onTraitAdded(level, trait)) {
					cap.addTrait(trait);
				}
			}
		});
		syncCapabilityWithClients(level);
	}

	/**
	 * This method removes the specified traits from the world, whilst also calling
	 * {@link #syncCapabilityWithClients(World)} <br>
	 * It is preferred to use this method instead of
	 * {@link IPlanetDataCapability#removeTrait(PlanetTrait)}
	 *
	 * @param level
	 * @param traits
	 */
	public static void removeTrait(World level, PlanetTrait... traits) {
		level.getCapability(PLANET_DATA_CAPABILITY).ifPresent(cap -> {
			for (PlanetTrait trait : traits) {
				if (cap.getTraits().contains(trait)) {
					cap.removeTrait(trait);
				}
			}
		});
		syncCapabilityWithClients(level);
	}

	/**
	 * Weird way of loading/saving, but it works. TODO figure out a better way to do
	 * it. Maybe using a custom {@link CollectionNBT} of {@link String}s?
	 *
	 * @author matyrobbrt
	 *
	 */
	public static final class Storage implements Capability.IStorage<IPlanetDataCapability> {

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
