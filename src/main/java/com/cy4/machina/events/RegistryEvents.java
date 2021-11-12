package com.cy4.machina.events;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

import com.cy4.machina.Machina;
import com.cy4.machina.api.annotation.registries.RegisterBlock;
import com.cy4.machina.api.annotation.registries.RegisterBlockItem;
import com.cy4.machina.api.annotation.registries.RegisterContainerType;
import com.cy4.machina.api.annotation.registries.RegisterEffect;
import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegisterParticleType;
import com.cy4.machina.api.annotation.registries.RegisterPlanetTrait;
import com.cy4.machina.api.annotation.registries.RegisterTileEntityType;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.util.ReflectionHelper;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * @author matyrobbrt
 */
@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.MOD)
public class RegistryEvents {

	@SubscribeEvent
	public static void constructMod(FMLConstructModEvent event) {
		RegistryEvents.init();
	}

	private static final ArrayList<Class<?>> REGISTRY_CLASSES = new ArrayList<>();

	public static void init() {
		final List<ModFileScanData.AnnotationData> annotations = ModList.get().getAllScanData().stream()
				.map(ModFileScanData::getAnnotations).flatMap(Collection::stream)
				.filter(a -> a.getAnnotationType().equals(Type.getType(RegistryHolder.class)))
				.collect(Collectors.toList());

		annotations.stream().filter(a -> Type.getType(RegistryHolder.class).equals(a.getAnnotationType()))
				.filter(a -> a.getTargetType() == ElementType.TYPE).forEach(data -> {
					try {
						REGISTRY_CLASSES.add(Class.forName(data.getClassType().getClassName(), false,
								RegistryEvents.class.getClassLoader()));
					} catch (ClassNotFoundException e) {}
				});
	}

	private RegistryEvents() {
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		registerFieldsWithAnnotation(event, RegisterItem.class, Item.class);

		// registers block items
		ReflectionHelper.getFieldsAnnotatedWith(REGISTRY_CLASSES, RegisterBlockItem.class).forEach(field -> {
			try {
				if (field.isAccessible() && field.get(field.getDeclaringClass()) instanceof BlockItem) {
					BlockItem blockitem = (BlockItem) field.get(field.getDeclaringClass());
					event.getRegistry().register(blockitem.setRegistryName(blockitem.getBlock().getRegistryName()));
				} else
					throw new RegistryException("The field " + field
							+ " is annotated with @RegisterBlockItem but it is not an block item.");
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
				// TODO Do some println()
			}
		});

	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		registerFieldsWithAnnotation(event, RegisterBlock.class, Block.class);
	}

	@SubscribeEvent
	public static void registerTileEntityTypes(final RegistryEvent.Register<TileEntityType<?>> event) {
		// registers TE types
		ReflectionHelper.getFieldsAnnotatedWith(REGISTRY_CLASSES, RegisterTileEntityType.class).forEach(field -> {
			try {
				if (field.isAccessible() && field.get(field.getDeclaringClass()) instanceof TileEntityType<?>) {
					TileEntityType<?> tile = (TileEntityType<?>) field.get(field.getDeclaringClass());
					tile.setRegistryName(
							new ResourceLocation(field.getDeclaringClass().getAnnotation(RegistryHolder.class).modid(),
									field.getAnnotation(RegisterTileEntityType.class).value()));
					event.getRegistry().register(tile);
				} else
					throw new RegistryException("The field " + field
							+ " is annotated with @RegisterTileEntityType but it is not a te type.");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Do some println()
			}
		});
	}

	@SubscribeEvent
	public static void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> event) {
		// registers TE types
		ReflectionHelper.getFieldsAnnotatedWith(REGISTRY_CLASSES, RegisterContainerType.class).forEach(field -> {
			try {
				if (field.isAccessible() && field.get(field.getDeclaringClass()) instanceof ContainerType<?>) {
					ContainerType<?> container = (ContainerType<?>) field.get(field.getDeclaringClass());
					container.setRegistryName(
							new ResourceLocation(field.getDeclaringClass().getAnnotation(RegistryHolder.class).modid(),
									field.getAnnotation(RegisterContainerType.class).value()));
					event.getRegistry().register(container);
				} else
					throw new RegistryException("The field " + field
							+ " is annotated with @RegisterContainerType but it is not a container type.");
			} catch (IllegalArgumentException | IllegalAccessException e) {}
		});
	}
	
	@SubscribeEvent
	public static void registerParticleTypes(final RegistryEvent.Register<ParticleType<?>> event) {
		// registers Particle types
		ReflectionHelper.getFieldsAnnotatedWith(REGISTRY_CLASSES, RegisterParticleType.class).forEach(field -> {
			try {
				if (field.isAccessible() && field.get(field.getDeclaringClass()) instanceof ParticleType<?>) {
					ParticleType<?> particle = (ParticleType<?>) field.get(field.getDeclaringClass());
					particle.setRegistryName(
							new ResourceLocation(field.getDeclaringClass().getAnnotation(RegistryHolder.class).modid(),
									field.getAnnotation(RegisterParticleType.class).value()));
					event.getRegistry().register(particle);
				} else
					throw new RegistryException("The field " + field
							+ " is annotated with @RegisterParticleType but it is not a particle type.");
			} catch (IllegalArgumentException | IllegalAccessException e) {}
		});
	}

	@SubscribeEvent
	public static void registerEffects(final RegistryEvent.Register<Effect> event) {
		registerFieldsWithAnnotation(event, RegisterEffect.class, Effect.class);
	}

	@SubscribeEvent
	public static void registerPlanetTraits(final RegistryEvent.Register<PlanetTrait> event) {
		registerFieldsWithAnnotation(event, RegisterPlanetTrait.class, PlanetTrait.class);
	}

	@SubscribeEvent
	public static void onNewRegistry(RegistryEvent.NewRegistry event) {
		PlanetTrait.createRegistry(event);
	}

	@SuppressWarnings("unchecked")
	private static <T extends IForgeRegistryEntry<T>> void registerFieldsWithAnnotation(
			final RegistryEvent.Register<T> event, Class<? extends Annotation> annotation, Class<T> objectClass) {
		ReflectionHelper.getFieldsAnnotatedWith(REGISTRY_CLASSES, annotation).forEach(field -> {
			try {
				if (field.isAccessible() && objectClass.isInstance(field.get(field.getDeclaringClass()))) {
					T registry = (T) field.get(field.getDeclaringClass());
					String name = "";

					if (annotation == RegisterEffect.class) {
						name = field.getAnnotation(RegisterEffect.class).value();
					} else if (annotation == RegisterItem.class) {
						name = field.getAnnotation(RegisterItem.class).value();
					} else if (annotation == RegisterBlock.class) {
						name = field.getAnnotation(RegisterBlock.class).value();
					} else if (annotation == RegisterPlanetTrait.class) {
						name = field.getAnnotation(RegisterPlanetTrait.class).id();
					}

					registry.setRegistryName(new ResourceLocation(
							field.getDeclaringClass().getAnnotation(RegistryHolder.class).modid(), name));
					event.getRegistry().register(registry);
				} else
					throw new RegistryException("The field " + field + " is annotated with " + annotation
							+ "but it is not a " + objectClass);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Do some println()
			}
		});
	}

	public static class RegistryException extends RuntimeException {

		private static final long serialVersionUID = 1688668579640237515L;

		public RegistryException() {
			super();
		}

		public RegistryException(String message) {
			super(message);
		}

		public RegistryException(String message, Throwable cause) {
			super(message, cause);
		}

		public RegistryException(Throwable cause) {
			super(cause);
		}
	}

}
