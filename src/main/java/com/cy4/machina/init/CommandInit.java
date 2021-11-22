package com.cy4.machina.init;

import static net.minecraft.command.Commands.literal;

import java.util.ArrayList;

import com.cy4.machina.command.BaseCommand;
import com.cy4.machina.command.impl.GoToPlanetCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;

import net.minecraftforge.event.RegisterCommandsEvent;

/**
 * Permission levels
 *
 * 0 - All Players 1 - No Commands 2 - /give, /clear, /difficulty 3 - /ban,
 * /kick, /op 4 - /not_a_democracy (this is NOT a democracy, i decide what to do
 * :kekw:)
 *
 * @author matyrobbrt
 *
 */
public final class CommandInit {

	private static final ArrayList<BaseCommand> commands = new ArrayList<>();

	public static void registerCommands(final RegisterCommandsEvent event) {

		commands.add(new GoToPlanetCommand(4, true));

		commands.forEach(command -> {
			if (command.isEnabled()) {
				LiteralArgumentBuilder<CommandSource> builder = literal(command.getName());
				builder.requires(sender -> sender.hasPermission(command.getPermissionLevel()));
				command.build(builder);
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
