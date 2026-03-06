package com.machina.command;

import com.machina.registration.init.RegistryInit;
import com.machina.weather.WeatherEvent;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

public class WeatherEventArgument extends ResourceArgument<WeatherEvent> {
	protected WeatherEventArgument(CommandBuildContext context) {
		super(context, RegistryInit.WEATHER_EVENT.key());
	}

	public static WeatherEventArgument weather(CommandBuildContext context) {
		return new WeatherEventArgument(context);
	}

	@SuppressWarnings("unchecked")
	public static <S> Holder.Reference<WeatherEvent> get(CommandContext<S> context, String name)
			throws CommandSyntaxException {
		Holder.Reference<WeatherEvent> reference = context.getArgument(name, Holder.Reference.class);
		ResourceKey<?> resourcekey = reference.key();
		if (resourcekey.isFor(RegistryInit.WEATHER_EVENT.key())) {
			return reference;
		} else {
			throw ERROR_INVALID_RESOURCE_TYPE.create(resourcekey.location(), resourcekey.registry(),
					RegistryInit.WEATHER_EVENT.key().location());
		}
	}
}