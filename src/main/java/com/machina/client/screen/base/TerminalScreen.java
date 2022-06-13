package com.machina.client.screen.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.machina.client.screen.element.CustomTextField;
import com.machina.client.util.ClientTimer;
import com.machina.client.util.UIHelper;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

public abstract class TerminalScreen<T extends Container> extends NoJeiContainerScreen<T> {

	private List<TerminalCommand> instructionSet;
	private CustomTextField input;
	public List<String> history = new ArrayList<>();

	private float timer = 0;
	private boolean progress;
	private Runnable onComplete;
	private float ticksNeeded;
	
	private boolean awaitingResponse;
	private Function<String, Boolean> checkResponse;

	public TerminalScreen(T pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - 176) / 2;
		int xSize = 237, ySize = 184;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;

		this.input = new CustomTextField(this.font, x + 8, y + 166, 108, 18,
				StringUtils.translateComp("machina.terminal.hint"));
		this.input.setBordered(false);
		this.input.setMaxLength(16);
		this.input.setCompletion(s -> inputSubmitted(s));
		this.addWidget(input);
		this.setInitialFocus(this.input);

		this.history.clear();
		this.history.add("Type \"help\" to see a list of commands.");

		this.instructionSet = createCommands();
	}

	public abstract List<TerminalCommand> createCommands();

	@Override
	public void tick() {
		this.input.tick();
		super.tick();
	}

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float pPartialTicks) {
		if (this.progress) {
			this.timer += ClientTimer.deltaTick;
			int occupied = (int) Math.min((this.timer / this.ticksNeeded) * 24, 24);
			this.history.set(this.history.size() - 1,
					"[" + StringUtils.repeat("█", occupied) + StringUtils.repeat(" ", 24 - occupied) + "]");
			if (this.timer > this.ticksNeeded) {
				this.onComplete.run();
				this.input.setEditable(true);
				this.timer = 0;
				this.progress = false;
			}
		}

		RenderSystem.color4f(1f, 1f, 1f, 1f);

		// Darker background
		this.renderBackground(stack);
		this.renderBackground(stack);

		// Render
		super.render(stack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(stack, pMouseX, pMouseY);
	}

	@Override
	protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {
	}

	@Override
	protected void renderBg(MatrixStack stack, float pPartialTicks, int pX, int pY) {
		UIHelper.bindTrmnl();

		// Back
		int xSize = 237, ySize = 184;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 0, 0, xSize, ySize);
		this.blit(stack, x + 4, y + 162, 0, 184, 135, 18);

		if (pX > x + 124 && pX < x + 124 + 11 && pY > y + 165 && pY < y + 165 + 11) {
			this.blit(stack, x + 124, y + 165, 237, 45, 11, 11);
		} else {
			this.blit(stack, x + 124, y + 165, 237, 34, 11, 11);
		}

		if (pX > x + 215 && pX < x + 215 + 17 && pY > y + 4 && pY < y + 4 + 17 && !this.progress) {
			this.blit(stack, x + 215, y + 4, 237, 17, 17, 17);
		} else {
			this.blit(stack, x + 215, y + 4, 237, 0, 17, 17);
		}
		if (!this.progress)
			this.input.render(stack, pX, pY, pPartialTicks);

		for (int i = 0; i < Math.min(12, this.history.size()); i++) {
			UIHelper.drawStringWithBorder(stack, this.history.get(this.history.size() - i - 1), x + 6, y + 150 - i * 12,
					0xFF_00fefe, 0xFF_0e0e0e);
		}
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {
		if (pButton == 0) {
			int xSize = 237, ySize = 184;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;

			if (pX > x + 215 && pX < x + 215 + 17 && pY > y + 4 && pY < y + 4 + 17) {
				this.onClose();
				UIHelper.click();
			}

			if (pX > x + 124 && pX < x + 124 + 11 && pY > y + 165 && pY < y + 165 + 11) {
				inputSubmitted(this.input.getValue());
			}
		}
		return super.mouseReleased(pX, pY, pButton);
	}

	@Override
	public boolean charTyped(char pCodePoint, int pModifiers) {
		return this.input.charTyped(pCodePoint, pModifiers);
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		if (this.input.keyPressed(pKeyCode, pScanCode, pModifiers))
			return true;

		return this.input.isFocused() && this.input.isVisible() && pKeyCode != 256 ? true
				: super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}

	public void inputSubmitted(String input) {
		UIHelper.click();
		this.input.setValue("");
		String command = input.toLowerCase().trim();
		history.add("> " + command);
		if (awaitingResponse) {
			if(checkResponse.apply(command))
				awaitingResponse = false;
			return;
		}
		
		if (command.equals("help")) {
			history.add("");
			history.add("The following is a list of commands:");
			history.add(" - \"help\" - Shows a list of commands.");
			this.instructionSet.forEach(comm -> {
				history.add(comm.help());
			});
			history.add("");
		} else {
			for (TerminalCommand comm : this.instructionSet) {
				if (comm.execute(command))
					return;
			}
			history.add("Unrecognised command: " + command);
		}
	}

	public void createTimer(float ticks, Runnable onComplete) {
		this.history.add("");
		this.timer = 0;
		this.progress = true;
		this.onComplete = onComplete;
		this.ticksNeeded = ticks;
		this.input.setEditable(false);
	}
	
	public void awaitResponse(Function<String, Boolean> isCorrect) {
		this.awaitingResponse = true;
		this.checkResponse = isCorrect;
	}

	@Override
	public int getXSize() {
		return 176;
	}

	public class TerminalCommand {
		String command;
		String description;
		Runnable execute;

		public TerminalCommand(String comm, String desc, Runnable exe) {
			this.command = comm;
			this.description = desc;
			this.execute = exe;
		}

		public String help() {
			return " - \"" + command + "\" - " + description;
		}

		public boolean execute(String c) {
			if (command.equals(c)) {
				execute.run();
				return true;
			}

			return false;
		}
	}

}
