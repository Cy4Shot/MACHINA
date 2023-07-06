package com.machina.weather.event;

import com.machina.Machina;
import com.machina.weather.WeatherManagerClient;
import com.machina.weather.wind.WindReader;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WeatherClientEvents {
	public static final WeatherClientEvents INSTANCE = new WeatherClientEvents();

	public static World lastWorld;
	public static WeatherManagerClient weatherManager;

	public float smoothAngle = 0;
	public float smoothAngleRotationalVelAccel = 0;
	public float smoothAngleAdj = 0.1F;
	public int prevDir = 0;

	private WeatherClientEvents() {
	}

	@SubscribeEvent
	public static void tick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			INSTANCE.onTickInGame();
		}
	}

	public void onTickInGame() {
		Minecraft mc = Minecraft.getInstance();
		World world = mc.level;

		if (world != null) {
			checkClientWeather();

			weatherManager.tick();

			// TODO: evaluate if best here
			float windDir = WindReader.getWindAngle(world);
			float windSpeed = WindReader.getWindSpeed(world);

			// windDir = 0;

			float diff = Math.abs(windDir - smoothAngle)/* - 180 */;

			if (true && diff > 10/* && (smoothAngle > windDir - give || smoothAngle < windDir + give) */) {

				if (smoothAngle > 180)
					smoothAngle -= 360;
				if (smoothAngle < -180)
					smoothAngle += 360;

				float bestMove = MathHelper.wrapDegrees(windDir - smoothAngle);

				smoothAngleAdj = windSpeed;// 0.2F;

				if (Math.abs(bestMove) < 180/* - (angleAdjust * 2) */) {
					float realAdj = smoothAngleAdj;// Math.max(smoothAngleAdj, Math.abs(bestMove));

					if (realAdj * 2 > windSpeed) {
						if (bestMove > 0) {
							smoothAngleRotationalVelAccel -= realAdj;
							if (prevDir < 0) {
								smoothAngleRotationalVelAccel = 0;
							}
							prevDir = 1;
						} else if (bestMove < 0) {
							smoothAngleRotationalVelAccel += realAdj;
							if (prevDir > 0) {
								smoothAngleRotationalVelAccel = 0;
							}
							prevDir = -1;
						}
					}

					if (smoothAngleRotationalVelAccel > 0.3 || smoothAngleRotationalVelAccel < -0.3) {
						smoothAngle += smoothAngleRotationalVelAccel * 0.3F;
					} else {
						// smoothAngleRotationalVelAccel *= 0.9F;
					}

					smoothAngleRotationalVelAccel *= 0.80F;
				}
			}
		} else {
			resetClientWeather();
		}

	}

	public static void resetClientWeather() {
		weatherManager = null;
	}

	@SuppressWarnings("resource")
	public static void checkClientWeather() {

		try {
			World world = Minecraft.getInstance().level;
			if (weatherManager == null || world != lastWorld) {
				init(world);
			}
		} catch (Exception ex) {
		}
	}

	public static void init(World world) {
		lastWorld = world;
		weatherManager = new WeatherManagerClient(world.dimension());
	}
}
