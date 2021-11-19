package com.cy4.machina.events;

import static com.cy4.machina.init.MachinaRegistries.*;
import static java.util.Optional.of;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

import com.cy4.machina.Machina;
import com.cy4.machina.api.annotation.registries.RegisterBlock;
import com.cy4.machina.api.annotation.registries.RegisterBlockItem;
import com.cy4.machina.api.annotation.registries.RegisterContainerType;
import com.cy4.machina.api.annotation.registries.RegisterEffect;
import com.cy4.machina.api.annotation.registries.RegisterFluid;
import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegisterParticleType;
import com.cy4.machina.api.annotation.registries.RegisterPlanetTrait;
import com.cy4.machina.api.annotation.registries.RegisterTileEntityType;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.annotation.registries.recipe.RegisterACFunctionSerializer;
import com.cy4.machina.api.annotation.registries.recipe.RegisterRecipeSerializer;
import com.cy4.machina.api.annotation.registries.recipe.RegisterRecipeType;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;
import com.cy4.machina.api.util.MachinaRegistryObject;
import com.cy4.machina.api.util.TriFunction;
import com.cy4.machina.init.BlockItemInit;
import com.cy4.machina.init.MachinaRegistries;
import com.cy4.machina.util.ReflectionHelper;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

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

	private RegistryEvents() {
	}

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
			} catch (ClassNotFoundException e) {
				// Unknown class
			}
		});
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		registerFieldsWithAnnotation(event, RegisterItem.class, RegisterItem::value, of(ITEMS));
		registerFieldsWithAnnotation(event, RegisterBlockItem.class, (classAn, fieldAn, obj) -> {
			if (obj instanceof BlockItem) {
				return ((BlockItem) obj).getBlock().getRegistryName();
			}
			throw new RegistryException("Invalid BlockItem");
		}, Optional.empty());

		for (Block block : BlockItemInit.AUTO_BLOCK_ITEMS) {
			BlockItem item = new BlockItem(block, new Item.Properties().tab(Machina.MACHINA_ITEM_GROUP));
			item.setRegistryName(block.getRegistryName());
			event.getRegistry().register(item);
		}

	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		registerFieldsWithAnnotation(event, RegisterBlock.class, RegisterBlock::value, of(BLOCKS));
	}

	@SubscribeEvent
	public static void registerFluids(final RegistryEvent.Register<Fluid> event) {
		registerFieldsWithAnnotation(event, RegisterFluid.class, RegisterFluid::value, of(FLUIDS));
	}

	@SubscribeEvent
	public static void registerEffects(final RegistryEvent.Register<Effect> event) {
		registerFieldsWithAnnotation(event, RegisterEffect.class, RegisterEffect::value, of(EFFECTS));
	}

	@SubscribeEvent
	public static void registerPlanetTraits(final RegistryEvent.Register<PlanetTrait> event) {
		registerFieldsWithAnnotation(event, RegisterPlanetTrait.class, RegisterPlanetTrait::id, of(PLANET_TRAITS));
	}

	@SubscribeEvent
	public static void registerAdvancedCraftingFunctions(
			final RegistryEvent.Register<AdvancedCraftingFunctionSerializer<?>> event) {
		registerFieldsWithAnnotation(event, RegisterACFunctionSerializer.class,
				RegisterACFunctionSerializer::value, of(ADVANCED_CRAFTING_FUNCTION_SERIALIZERS));
	}

	@SubscribeEvent
	public static void registerTileEntityTypes(final RegistryEvent.Register<TileEntityType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterTileEntityType.class, RegisterTileEntityType::value,
				Optional.of(MachinaRegistries.TILE_ENTITY_TYPES));
	}

	@SubscribeEvent
	public static void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterContainerType.class, RegisterContainerType::value,
				of(CONTAINER_TYPES));
	}

	@SubscribeEvent
	public static void registerParticleTypes(final RegistryEvent.Register<ParticleType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterParticleType.class, RegisterParticleType::value,
				of(PARTICLE_TYPES));
	}

	@SubscribeEvent
	public static void registerRecipeTypes(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
		ReflectionHelper.getFieldsAnnotatedWith(REGISTRY_CLASSES, RegisterRecipeType.class).forEach(field -> {
			if (!field.isAccessible()) {
				return;
			}
			try {
				if (field.get(field.getDeclaringClass()) instanceof IRecipeType<?>) {
					IRecipeType<?> type = (IRecipeType<?>) field.get(field.getDeclaringClass());
					Registry.register(Registry.RECIPE_TYPE,
							new ResourceLocation(field.getDeclaringClass().getAnnotation(RegistryHolder.class).modid(),
									field.getAnnotation(RegisterRecipeType.class).value()),
							type);
					String modid = field.getDeclaringClass().getAnnotation(RegistryHolder.class).modid();
					if (RECIPE_TYPES.containsKey(modid)) {
						List<IRecipeType<?>> oldList = RECIPE_TYPES.get(modid);
						oldList.add(type);
						RECIPE_TYPES.put(modid, oldList);
					} else {
						RECIPE_TYPES.put(modid, Lists.newArrayList(type));
					}
				}
				else {
					//@formatter:off
					throw new RegistryException("The field " + field + " is annotated with @RegisterRecipeType but it is not a recipe type!");
					//@formatter:on
				}
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
				// Exception. Ignore
				e.printStackTrace();
			}
		});

		registerFieldsWithAnnotation(event, RegisterRecipeSerializer.class, RegisterRecipeSerializer::value,
				of(RECIPE_SERIALIZERS));
	}

	@SubscribeEvent
	public static void onNewRegistry(RegistryEvent.NewRegistry event) {
		PlanetTrait.createRegistry(event);
		AdvancedCraftingFunctionSerializer.createRegistry();
	}

	private static <T extends IForgeRegistryEntry<T>, A extends Annotation> void registerFieldsWithAnnotation(
			final RegistryEvent.Register<T> event, Class<A> annotation, Function<A, String> registryName,
			Optional<Map<String, List<T>>> outputMap) {
		registerFieldsWithAnnotation(event, annotation,
				(classAn, fieldAn, obj) -> new ResourceLocation(classAn.modid(), registryName.apply(fieldAn)),
				outputMap);
	}

	/**
	 * Handles registry annotations
	 *
	 * @param <T>          the type of the object being registered
	 * @param <A>          the class of the registry annotation
	 * @param event        the event in which the objects should be registered
	 * @param annotation   the annotation to look for
	 * @param registryName a {@link TriFunction} containing: the
	 *                     {@link RegistryHolder} annotation of the class of the
	 *                     field being registered, the registry annotation of the
	 *                     field and the object (of type <strong>T</strong>) the
	 *                     field contains. This function will return the registry
	 *                     name of the object, based off the inputed data
	 * @param outputMap    optionally, a map in which the processed objects will be
	 *                     put, as following: <br>
	 *                     A {@link List} with the generic type {@code <T>}
	 *                     will be put as the value corresponding to the key which
	 *                     is the namespace (mod id) of the object's registry name.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IForgeRegistryEntry<T>, A extends Annotation> void registerFieldsWithAnnotation(
			final RegistryEvent.Register<T> event, Class<A> annotation,
			TriFunction<RegistryHolder, A, T, ResourceLocation> registryName,
			Optional<Map<String, List<T>>> outputMap) {
		Class<T> objectClass = event.getRegistry().getRegistrySuperType();
		ReflectionHelper.getFieldsAnnotatedWith(REGISTRY_CLASSES, annotation).forEach(field -> {
			if (!field.isAccessible()) {
				return;
			}
			try {
				AtomicReference<T> registry = new AtomicReference<>(null);
				boolean isGood = false;
				boolean isSupplier = false;
				if (objectClass.isInstance(field.get(field.getDeclaringClass()))) {
					registry.set((T) field.get(field.getDeclaringClass()));
					isGood = true;
				} else if (field.get(field.getDeclaringClass()) instanceof MachinaRegistryObject<?>) {
					MachinaRegistryObject<?> regObj = (MachinaRegistryObject<?>) field.get(field.getDeclaringClass());
					Method getDecValueMet = regObj.getClass().getDeclaredMethod("getDeclaredValue", new Class<?>[] {});
					getDecValueMet.setAccessible(true);
					Supplier<?> declaredValue = (Supplier<?>) getDecValueMet.invoke(regObj, new Object[] {});
					if (objectClass.isInstance(declaredValue.get())) {
						registry.set((T) declaredValue.get());
						isGood = true;
						isSupplier = true;
					}
				}
				if (isGood && registry.get() != null) {
					ResourceLocation name = registryName.apply(
							field.getDeclaringClass().getAnnotation(RegistryHolder.class),
							field.getAnnotation(annotation), registry.get());
					registry.get().setRegistryName(name);
					event.getRegistry().register(registry.get());
					outputMap.ifPresent(output -> {
						String modid = field.getDeclaringClass().getAnnotation(RegistryHolder.class).modid();
						if (output.containsKey(modid)) {
							List<T> oldList = output.get(modid);
							oldList.add(registry.get());
							output.put(modid, oldList);
						} else {
							output.put(modid, Lists.newArrayList(registry.get()));
						}
					});
					if (isSupplier) {
						MachinaRegistryObject<?> regObj = ((MachinaRegistryObject<?>) field
								.get(field.getDeclaringClass()));
						Method setNameMethod = regObj.getClass().getDeclaredMethod("setName", ResourceLocation.class);
						setNameMethod.setAccessible(true);
						setNameMethod.invoke(regObj, name);
					}
				}
				else {
					//@formatter:off
					throw new RegistryException("The field " + field + " is annotated with " + annotation + " but it is not a " + objectClass);
					//@formatter:on
				}
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// Exception. Ignore
				e.printStackTrace();
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
