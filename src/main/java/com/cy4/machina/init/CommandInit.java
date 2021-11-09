package com.cy4.machina.init;

import static net.minecraft.command.Commands.literal;

import java.util.ArrayList;

import com.cy4.machina.Machina;
import com.cy4.machina.command.BaseCommand;
import com.cy4.machina.command.impl.DebugCommand;
import com.cy4.machina.command.impl.GoToPlanetCommand;
import com.cy4.machina.command.impl.traits.AddTraitCommand;
import com.cy4.machina.command.impl.traits.ListTraitsCommand;
import com.cy4.machina.command.impl.traits.PlanetTraitsCommand;
import com.cy4.machina.command.impl.traits.RemoveTraitCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;

import net.minecraftforge.event.RegisterCommandsEvent;

/**
 * Permission levels
 * 
 * 0 - All Players
 * 1 - No Commands
 * 2 - /give, /clear, /difficulty
 * 3 - /ban, /kick, /op
 * 4 - /stop (to stop the server)
 * 
 * @author matyrobbrt
 *
 */
public class CommandInit {
	
	private static final ArrayList<BaseCommand> commands = new ArrayList<>();
	private static final ArrayList<PlanetTraitsCommand> planetTraitsCommands = new ArrayList<>();

	public static void registerCommands(final RegisterCommandsEvent event) {
		CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
		
		planetTraitsCommands.add(new AddTraitCommand(PermissionLevels.GIVE_CLEAR, true));
		planetTraitsCommands.add(new ListTraitsCommand(PermissionLevels.GIVE_CLEAR, true));
		planetTraitsCommands.add(new RemoveTraitCommand(PermissionLevels.GIVE_CLEAR, true));
		
		commands.add(new DebugCommand(4, true));
		commands.add(new GoToPlanetCommand(4, true));
		
		commands.forEach(command -> {
			if (command.isEnabled()) {
				LiteralArgumentBuilder<CommandSource> builder = literal(command.getName());
	            builder.requires(sender -> sender.hasPermission(command.getPermissionLevel()));
	            command.build(builder);
	            dispatcher.register(literal(Machina.MOD_ID).then(builder));
			}
		});
		
		planetTraitsCommands.forEach(command -> {
			if (command.isEnabled()) {
				LiteralArgumentBuilder<CommandSource> builder = literal(command.getName());
				builder.requires(sender -> sender.hasPermission(command.getPermissionLevel()));
				command.build(builder);
				dispatcher.register(literal("planet_traits").then(builder));
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
