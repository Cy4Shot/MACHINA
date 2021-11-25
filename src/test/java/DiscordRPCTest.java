/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod("discord_rpc")
@Mod.EventBusSubscriber(modid = "discord_rpc", bus = Mod.EventBusSubscriber.Bus.MOD)
public class DiscordRPCTest {
	
	public DiscordRPCTest() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public static void clientSetup(final FMLClientSetupEvent event) {
		DiscordEventHandlers dcHandlers = new DiscordEventHandlers.Builder()
                .setReadyEventHandler(user -> System.out.println(user.username + "#" + user.discriminator))
                .build();
		DiscordRPC.discordInitialize("912371459328974848", dcHandlers, true);
        DiscordRPC.discordRunCallbacks();
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("YES").build());
	}
	
}
