package com.machina.registration.init;

import com.machina.Machina;
import com.machina.command.BaseCommand;
import com.machina.command.impl.DebugCommand;
import com.machina.command.impl.GoToPlanetCommand;
import com.machina.command.impl.ListAttributesCommand;
import com.machina.command.impl.ListTraitsCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;

public final class CommandInit {

	public static final int ALL_PLAYERS = 0;
	public static final int NO_COMMANDS = 1;
	public static final int GIVE_CLEAR = 2;
	public static final int BAN_KICK_OP = 3;
	public static final int STOP_THE_SERVER = 4;

	public static void registerCommands(final RegisterCommandsEvent event) {
		registerCommand(event, new GoToPlanetCommand(STOP_THE_SERVER));
		
		if (Machina.isDevEnvironment()) {
			registerCommand(event, new DebugCommand(STOP_THE_SERVER));
			registerCommand(event, new ListTraitsCommand(STOP_THE_SERVER));
			registerCommand(event, new ListAttributesCommand(STOP_THE_SERVER));
		}
	}

	public static void registerCommand(final RegisterCommandsEvent event, BaseCommand command) {
		CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal(command.getName());
		builder.requires(sender -> sender.hasPermission(command.getPermissionLevel()));
		command.build(builder);
		dispatcher.register(command.buildPath(Commands.literal(Machina.MOD_ID), builder));
	}
}
