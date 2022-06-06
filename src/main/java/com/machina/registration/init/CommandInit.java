package com.machina.registration.init;

import java.util.ArrayList;

import com.machina.Machina;
import com.machina.command.BaseCommand;
import com.machina.command.impl.DebugCommand;
import com.machina.command.impl.GoToPlanetCommand;
import com.machina.command.impl.ListTraitsCommand;
import com.machina.command.impl.PlanetTraitsCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;

public final class CommandInit {

	private static final ArrayList<BaseCommand> commands = new ArrayList<>();

	public static void registerCommands(final RegisterCommandsEvent event) {
		CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

		commands.add(new ListTraitsCommand(PermissionLevels.GIVE_CLEAR, true));

		commands.add(new DebugCommand(4, Machina.isDevEnvironment()));
		commands.add(new GoToPlanetCommand(4, true));

		commands.forEach(command -> {
			if (command.isEnabled()) {
				LiteralArgumentBuilder<CommandSource> builder = Commands.literal(command.getName());
				builder.requires(sender -> sender.hasPermission(command.getPermissionLevel()));
				command.build(builder);
				if (command instanceof PlanetTraitsCommand) {
					dispatcher.register(
							Commands.literal(Machina.MOD_ID).then(Commands.literal("planet_traits").then(builder)));
				} else {
					dispatcher.register(Commands.literal(Machina.MOD_ID).then(builder));
				}
			}
		});

	}

	public static class PermissionLevels {

		public static final int ALL_PLAYERS = 0;
		public static final int NO_COMMANDS = 1;
		public static final int GIVE_CLEAR = 2;
		public static final int BAN_KICK_OP = 3;
		public static final int STOP_THE_SERVER = 4;

	}

}
