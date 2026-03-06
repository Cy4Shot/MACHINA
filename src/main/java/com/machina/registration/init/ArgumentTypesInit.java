package com.machina.registration.init;

import java.util.function.Function;

import com.machina.Machina;
import com.machina.command.WeatherEventArgument;
import com.mojang.brigadier.arguments.ArgumentType;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArgumentTypesInit {
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister
			.create(Registries.COMMAND_ARGUMENT_TYPE, Machina.MOD_ID);

	public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<WeatherEventArgument>> WEATHER = register(
			"weather", WeatherEventArgument.class, WeatherEventArgument::weather);

	public static <A extends ArgumentType<?>, T extends ArgumentTypeInfo<A, ?>> DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<A>> register(
			String name, Class<A> clazz, Function<CommandBuildContext, A> supplier) {
		return ARGUMENT_TYPES.register(name,
				() -> ArgumentTypeInfos.registerByClass(clazz, SingletonArgumentInfo.contextAware(supplier)));
	}
}
