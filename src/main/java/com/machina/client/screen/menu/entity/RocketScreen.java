package com.machina.client.screen.menu.entity;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

import com.machina.api.client.ClientStarchart;
import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.network.c2s.C2SRocketLaunch;
import com.machina.api.network.c2s.C2SRocketSetDestination;
import com.machina.api.rocket.RocketCosts;
import com.machina.api.rocket.RocketProps;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.PlanetHelper;
import com.machina.api.util.StringUtils;
import com.machina.client.screen.StarchartRenderable;
import com.machina.rocket.RocketEntity;
import com.machina.rocket.RocketMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class RocketScreen extends MachinaMenuScreen<RocketMenu> {

	private static interface RocketTabDisplay {
		int getIconX();

		Component getName();

		void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j);

		default boolean starchartVisible() {
			return false;
		}
	}

	private final RocketTabDisplay INFO = new RocketTabDisplay() {
		@Override
		public void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j) {
			RocketProps props = entity.getProps();
			Component c = Component.literal(": ");
			MUI.drawString(gui,
					MUI.uistr("rocket.info.mass").append(c)
							.append(Component.literal(StringUtils.formatMass(props.mass()))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
					i + 6, j + 6);
			MUI.drawString(gui,
					MUI.uistr("rocket.info.fuel_type").append(c).append(StringUtils.fluid(props.fuelStack(), true)),
					i + 6, j + 16);
			MUI.drawString(gui,
					MUI.uistr("rocket.info.coolant_efficiency").append(c)
							.append(Component.literal(StringUtils.formatPercent(props.coolantEfficiency()))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
					i + 6, j + 26);
			MUI.drawString(gui,
					MUI.uistr("rocket.info.fuel_capacity").append(c)
							.append(Component.literal(StringUtils.formatFluid(props.fuelStorage()))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
					i + 6, j + 36);
			MUI.drawString(gui,
					MUI.uistr("rocket.info.coolant_capacity").append(c)
							.append(Component.literal(StringUtils.formatFluid(props.coolantStorage()))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
					i + 6, j + 46);
			MUI.drawString(gui, MUI.uistr("rocket.info.coolant_type").append(c)
					.append(StringUtils.fluid(props.coolantStack(), true)), i + 6, j + 56);
			MUI.drawString(gui,
					MUI.uistr("rocket.info.coolant_efficiency").append(c)
							.append(Component.literal(StringUtils.formatPercent(props.coolantEfficiency()))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
					i + 6, j + 66);
			MUI.drawString(gui, MUI.uistr("rocket.info.storage").append(c).append(Component
					.literal(String.valueOf(props.slots())).withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
					i + 6, j + 76);
			MUI.drawString(gui,
					MUI.uistr("rocket.info.max_pressure").append(c)
							.append(Component.literal(StringUtils.formatPressure(props.maxPressure()))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
					i + 6, j + 86);
		}

		@Override
		public Component getName() {
			return MUI.uistr("rocket.tab.info");
		}

		@Override
		public int getIconX() {
			return 0;
		}
	};

	private final RocketTabDisplay FUELING = new RocketTabDisplay() {

		@Override
		public void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j) {
			RocketCosts costs = entity.getCosts();

			int fuel = entity.getFluidMB(0);
			int fuelCap = entity.getTankCapacity(0);

			int cool = entity.getFluidMB(1);
			int coolCap = entity.getTankCapacity(1);

			Component c = Component.literal(": ");

			// ---------- STATUS HEADER ----------
			if (!entity.isPossible()) {
				MUI.drawCenteredString(gui,
						MUI.uistr("rocket.fueling.too_far").withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED)),
						i + 112, j + 6);
			} else if (entity.fuelSatisfied()) {
				MUI.drawCenteredString(gui,
						MUI.uistr("rocket.fueling.ready").withStyle(Style.EMPTY.withBold(true).withColor(MUI.GREEN)),
						i + 112, j + 6);
			} else {
				MUI.drawCenteredString(gui, MUI.uistr("rocket.fueling.insufficient")
						.withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED)), i + 112, j + 6);
			}

			// ---------- FUEL INFO ----------
			MUI.drawString(gui, MUI.uistr("rocket.fueling.fuel_stored").append(c).append(Component
					.literal(StringUtils.formatFluid(fuel)).withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
					i + 6, j + 20);

			MUI.drawString(gui,
					MUI.uistr("rocket.fueling.fuel_capacity").append(c).append(
							Component.literal(StringUtils.formatFluid(fuelCap)).withStyle(Style.EMPTY.withBold(true))),
					i + 6, j + 30);

			MUI.drawString(gui, MUI.uistr("rocket.fueling.fuel_required").append(c)
					.append(Component.literal(StringUtils.formatFluid(costs.fuelRequired())).withStyle(
							Style.EMPTY.withBold(true).withColor(fuel >= costs.fuelRequired() ? MUI.GREEN : MUI.RED))),
					i + 6, j + 40);

			// ---------- COOLANT INFO ----------
			MUI.drawString(gui, MUI.uistr("rocket.fueling.cool_stored").append(c).append(Component
					.literal(StringUtils.formatFluid(cool)).withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
					i + 6, j + 60);

			MUI.drawString(gui,
					MUI.uistr("rocket.fueling.cool_capacity").append(c).append(
							Component.literal(StringUtils.formatFluid(coolCap)).withStyle(Style.EMPTY.withBold(true))),
					i + 6, j + 70);

			MUI.drawString(gui,
					MUI.uistr("rocket.fueling.cool_required").append(c)
							.append(Component.literal(StringUtils.formatFluid(costs.coolantRequired()))
									.withStyle(Style.EMPTY.withBold(true)
											.withColor(cool >= costs.coolantRequired() ? MUI.GREEN : MUI.RED))),
					i + 6, j + 80);

			// ---------- SLOTS ----------
			MUI.drawSlot(gui, i + 172, j + 82, mx, my, false, true);
			MUI.drawSlot(gui, i + 194, j + 82, mx, my, false, true);

			drawFluidBarVert(gui, 177, -20, 0);
			drawFluidBarVert(gui, 199, -20, 1);
		}

		@Override
		public Component getName() {
			return MUI.uistr("rocket.tab.fueling");
		}

		@Override
		public int getIconX() {
			return 16;
		}
	};

	private final RocketTabDisplay STORAGE = new RocketTabDisplay() {
		@Override
		public void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j) {
			MUI.drawCenteredString(gui, MUI.uistr("rocket.storage.soon"), i + 112, j + 46);
		}

		@Override
		public Component getName() {
			return MUI.uistr("rocket.tab.storage");
		}

		@Override
		public int getIconX() {
			return 64;
		}
	};

	private final RocketTabDisplay DESTINATION = new RocketTabDisplay() {
		@Override
		public void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j) {
			RocketProps props = entity.getProps();
			RocketCosts costs = entity.getCosts();
			ResourceKey<Level> destination = entity.getDestination();

			if (destination.equals(Level.OVERWORLD)) {
				MUI.drawCenteredString(gui, MUI.uistr("rocket.destination.invalid")
						.withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED)), i + 112, j + 20);
				MUI.drawCenteredString(gui,
						MUI.uistr("rocket.destination.hint").withStyle(Style.EMPTY.withColor(MUI.CYAN)), i + 112,
						j + 36);
				return;
			}

			drawButton(gui, mx, my, 204, -43, MuiSlot.CROSS, () -> !destination.equals(Level.OVERWORLD), () -> {
				PacketDistributor.sendToServer(new C2SRocketSetDestination(entity.getId(), Level.OVERWORLD));
			}, () -> MUI.uistr("rocket.destination.clear"));

			Planet dst = ClientStarchart.system.planets().get(PlanetHelper.getIdLevel(destination));

			Component c = Component.literal(": ");
			Component ob = Component.literal(" (");
			Component cb = Component.literal(")");
			MUI.drawString(gui,
					MUI.uistr("rocket.destination.destination").append(c).append(
							Component.literal(dst.name()).withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
					i + 6, j + 6);
			MUI.drawString(gui,
					MUI.uistr("rocket.destination.distance").append(c)
							.append(Component.literal(StringUtils.formatDistanceAU(costs.distance()))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
					i + 6, j + 16);
			MUI.drawString(gui,
					MUI.uistr("rocket.destination.max_temperature").append(c)
							.append(Component.literal(StringUtils.formatTemp(costs.maxTemp()))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
					i + 6, j + 26);

			int storedFuel = entity.getFluidMB(0);
			int requiredFuel = costs.fuelRequired();

			MutableComponent fuelLine = Component.literal(StringUtils.formatFluid(requiredFuel))
					.append(Component.literal(" / ")).append(Component.literal(StringUtils.formatFluid(storedFuel)));

			MUI.drawString(gui,
					MUI.uistr("rocket.destination.fuel").append(c).append(fuelLine.withStyle(
							Style.EMPTY.withBold(true).withColor(storedFuel >= requiredFuel ? MUI.GREEN : MUI.RED))),
					i + 6, j + 36);

			int storedCool = entity.getFluidMB(1);
			int requiredCool = costs.coolantRequired();

			MutableComponent coolLine = Component.literal(StringUtils.formatFluid(requiredCool))
					.append(Component.literal(" / ")).append(Component.literal(StringUtils.formatFluid(storedCool)));

			MUI.drawString(gui,
					MUI.uistr("rocket.destination.coolant").append(c).append(coolLine.withStyle(
							Style.EMPTY.withBold(true).withColor(storedCool >= requiredCool ? MUI.GREEN : MUI.RED))),
					i + 6, j + 46);

			if (costs.maxPres() < props.maxPressure()) {
				MUI.drawString(gui,
						MUI.uistr("rocket.destination.max_pressure").append(c)
								.append(Component.literal(StringUtils.formatPressure(costs.maxPres()))
										.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
						i + 6, j + 56);
			} else {
				Component invalid = Component.literal(StringUtils.formatPressure(costs.maxPres())).append(ob)
						.append(Component.literal(StringUtils.formatPressure(props.maxPressure()))).append(cb);
				MUI.drawString(gui,
						MUI.uistr("rocket.destination.max_pressure").append(c)
								.append(invalid.copy().withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED))),
						i + 6, j + 56);
			}

			// Launch Button
			boolean possible = entity.isPossible();
			boolean fueled = entity.fuelSatisfied();

			if (possible && fueled) {
				drawButton(gui, mx, my, 112, 31, MuiSlot.TICK, () -> true,
						() -> PacketDistributor.sendToServer(new C2SRocketLaunch(entity.getId())),
						() -> MUI.uistr("rocket.destination.launch"));
				MUI.blitCommon(gui, i + 134, j + 84, 405, 13, 17, 6);
				MUI.blitCommon(gui, i + 82, j + 84, 422, 13, 17, 6);
			} else {
				Component reason;
				if (!possible) {
					reason = MUI.uistr("rocket.destination.out_of_range")
							.withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED));
				} else {
					reason = MUI.uistr("rocket.destination.insufficient_fuel")
							.withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED));
				}
				MUI.drawCenteredString(gui, reason, i + 112, j + 84);
			}
		}

		@Override
		public Component getName() {
			return MUI.uistr("rocket.tab.destination");
		}

		@Override
		public int getIconX() {
			return 32;
		}
	};

	private final RocketTabDisplay STARMAP = new RocketTabDisplay() {

		@Override
		public void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j) {
			int x = i + 1;
			int y = j + 1;
			int w = 225;
			int h = 112;
			MUI.enableClipping(x, y, w, h);
			starchart.render(gui, x, y, width / 2 - (x + w / 2), height / 2 - (y + h / 2), w, h);
			MUI.disableClipping();

			MUI.drawWithScale(gui, 0.5f, t -> {

				// Draw Help
				MUI.blitCommon(gui, t.apply(i + 4f).intValue(), t.apply(j + 4f).intValue(), 448, 160, 16, 16);
				MUI.blitCommon(gui, t.apply(i + 4f).intValue(), t.apply(j + 14f).intValue(), 464, 160, 16, 16);
				MUI.blitCommon(gui, t.apply(i + 4f).intValue(), t.apply(j + 24f).intValue(), 496, 160, 16, 16);

				MUI.drawString(gui, MUI.uistr("rocket.starmap.pan"), t.apply(i + 14f).intValue(),
						t.apply(j + 6f).intValue());
				MUI.drawString(gui, MUI.uistr("rocket.starmap.rotate"), t.apply(i + 14f).intValue(),
						t.apply(j + 16f).intValue());
				MUI.drawString(gui, MUI.uistr("rocket.starmap.zoom"), t.apply(i + 14f).intValue(),
						t.apply(j + 26f).intValue());

				if (starchart.tracked != null) {
					Planet planet = (Planet) starchart.tracked.celestial();
					Vector2d uiPos = starchart.tracked.screenPos();

					// Draw Tracking Box
					MUI.blitRocket(gui, t.apply(i + 56f).intValue(), t.apply(j + 5f).intValue(), 253, 26, 227, 114);
					MUI.drawLine(gui, t.apply((float) uiPos.x), t.apply((float) uiPos.y), t.apply(i + 56f),
							t.apply(j + 5f) + 114f, 1, MUI.CYAN | 0xA0000000);
					MUI.drawLine(gui, t.apply((float) uiPos.x), t.apply((float) uiPos.y), t.apply(i + 56f) + 227f,
							t.apply(j + 5f) + 114f, 1, MUI.CYAN | 0xA0000000);
					float parts = 67;
					for (int inc = 1; inc < parts; inc++) {
						MUI.drawLine(gui, t.apply((float) uiPos.x), t.apply((float) uiPos.y),
								t.apply(i + 56f) + (227f / parts) * inc, t.apply(j + 5f) + 114f, 2,
								MUI.CYAN | 0x50000000);
					}

					// Draw Planet Title
					MUI.drawCenteredString(gui, planet.getName(), t.apply(i + 56f).intValue() + 112,
							t.apply(j + 5f).intValue() + 10);
					MUI.blitCommon(gui, t.apply(i + 56f).intValue() + 55, t.apply(j + 5f).intValue() + 22, 308, 245,
							115, 6);

					// Draw Planet Info
					Component c = Component.literal(": ");
					MUI.drawString(gui,
							MUI.uistr("rocket.starmap.planet_type").append(c)
									.append(Component.literal("Earthlike")
											.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
							t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 32);
					MUI.drawString(gui,
							MUI.uistr("rocket.starmap.day_length").append(c)
									.append(Component.literal(StringUtils.formatHours((float) planet.day()))
											.withStyle(Style.EMPTY.withBold(true))),
							t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 42);
					if (planet.gas_giant()) {
						MUI.drawString(gui,
								MUI.uistr("rocket.starmap.gas_giant").append(c).append(StringUtils.formatBool(true)),
								t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 52);
					} else {
						MUI.drawString(gui,
								MUI.uistr("rocket.starmap.gravity").append(c)
										.append(Component.literal(StringUtils.formatGravity((float) planet.surf_grav()))
												.withStyle(Style.EMPTY.withBold(true))),
								t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 52);
					}
					MUI.drawString(gui,
							MUI.uistr("rocket.starmap.breathable_atmosphere").append(c)
									.append(StringUtils.formatBool(planet.breathable())),
							t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 62);
				}
			});
		}

		@Override
		public Component getName() {
			return MUI.uistr("rocket.tab.starmap");
		}

		@Override
		public int getIconX() {
			return 48;
		}

		@Override
		public boolean starchartVisible() {
			return true;
		};
	};

	private final List<RocketTabDisplay> TABS = List.of(INFO, FUELING, STORAGE, STARMAP, DESTINATION);

	private final StarchartRenderable starchart;
	private int selected = 0;

	public RocketScreen(RocketMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.starchart = new StarchartRenderable(ClientStarchart.system, true);
		this.starchart.addSelectListener(selected -> {
			ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION,
					MachinaRL.create(String.valueOf(selected)));
			PacketDistributor.sendToServer(new C2SRocketSetDestination(this.menu.entity.getId(), dim));
		});
	}

	@Override
	protected void init() {
		super.init();
		menu.rebuildSlotPositions(this.selected);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawRocketBackground(gui);

		int i = midWidth();
		int j = midHeight();

		final RocketTabDisplay sel = getSelected();

		MUI.blitRocket(gui, i + 26, j - 72, 253, 0, 121, 26);
		MUI.drawCenteredString(gui, sel.getName().copy().setStyle(Style.EMPTY.withBold(true)), i + 190, j - 63);

		for (int x = 0; x < TABS.size(); x++) {
			final RocketTabDisplay tab = TABS.get(x);
			if (selected != x
					&& (mx < i + 35 + x * 21 || mx > i + 35 + x * 21 + 18 || my < j - 71 || my > j - 71 + 18)) {
				MUI.blitRocket(gui, i + 35 + x * 21, j - 71, 235, 0, 18, 18);
			} else {
				MUI.blitRocket(gui, i + 35 + x * 21, j - 71, 235, 18, 18, 18);
			}

			MUI.blitRocket(gui, i + 36 + x * 21, j - 70, 128 + tab.getIconX(), 288, 16, 16);

			final int x1 = x;
			clickAndHover("tab_" + x, i + 35 + x * 21, j - 71, i + 53 + x * 21, j - 53, () -> true, tab::getName,
					() -> {
						if (this.selected != x1) {
							this.selected = x1;
							this.init();
							MUI.click();
						}
					});
		}

		MUI.blitRocket(gui, i + 4, j - 47, 253, 26, 227, 114);
		sel.render(gui, this.menu.entity, mx, my, i + 4, j - 47);
		drawOverlay(gui);
	}

	@Override
	protected void drawFluidBarVert(GuiGraphics gui, int x, int y, int tank) {
		RocketEntity rocket = this.menu.entity;
		drawBarVert(gui, x, y, StringUtils::formatFluid,
				() -> StringUtils.fluid(rocket.getFluid(tank), true)
						.append(Component.literal(": ").withStyle(Style.EMPTY)),
				() -> rocket.getFluidMB(tank), () -> rocket.getTankCapacity(tank), () -> rocket.getFluidF(tank),
				(i, j, p) -> {
					float prop = rocket.getFluidF(tank);
					MUI.renderFluid(gui, rocket.getFluid(tank), i + 1, j + 41, 14, (int) (40 * prop), 0);
				});
	}

	@Override
	public boolean mouseClicked(double mX, double mY, int button) {
		if (inStarchart(mX, mY) && starchart.mouseClicked(mX, mY, button)) {
			return true;
		}
		return super.mouseClicked(mX, mY, button);
	}

	@Override
	public boolean mouseDragged(double mX, double mY, int button, double dX, double dY) {
		if (inStarchart(mX, mY) && starchart.mouseDragged(button, dX, dY, width, height)) {
			return true;
		}
		return super.mouseDragged(mX, mY, button, dX, dY);
	}

	@Override
	public boolean mouseScrolled(double mX, double mY, double deltaX, double deltaY) {
		if (inStarchart(mX, mY) && starchart.mouseScrolled(deltaY)) {
			return true;
		}
		return super.mouseScrolled(mX, mY, deltaX, deltaY);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private RocketTabDisplay getSelected() {
		return TABS.get(selected);
	}

	private boolean inStarchart(double mX, double mY) {
		int i = midWidth();
		int j = midHeight();
		return getSelected().starchartVisible() && mX > i + 4 && mY > j - 46 && mX < i + 4 + 225 && mY < j - 46 + 112;
	}
}
