package com.machina.client.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.machina.block.container.PuzzleContainer;
import com.machina.client.screen.base.TerminalScreen;
import com.machina.config.ClientConfig;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SCompletePuzzle;
import com.machina.util.StringUtils;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class PuzzleScreen extends TerminalScreen<PuzzleContainer> {

	public PuzzleScreen(PuzzleContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	public List<TerminalCommand> createCommands() {
		List<TerminalCommand> list = new ArrayList<>();

		// Clear
		list.add(new TerminalCommand("clear", t -> {
			clear();
		}));

		// Neofetch
		list.add(new TerminalCommand("neofetch", t -> {
			space();
			add("\u2588\u2581\u2581\u2581\u2588\u2581\u2588\u2588\u2588\u2581\u2588\u2588\u2588\u2581\u2588\u2581\u2588");
			add("\u2588\u2588\u2581\u2588\u2588\u2581\u2588\u2581\u2588\u2581\u2588\u2581\u2581\u2581\u2588\u2581\u2588");
			add("\u2588\u2581\u2588\u2581\u2588\u2581\u2588\u2588\u2588\u2581\u2588\u2581\u2581\u2581\u2588\u2588\u2588");
			add("\u2588\u2581\u2581\u2581\u2588\u2581\u2588\u2581\u2588\u2581\u2588\u2581\u2581\u2581\u2588\u2581\u2588");
			add("\u2588\u2581\u2581\u2581\u2588\u2581\u2588\u2581\u2588\u2581\u2588\u2588\u2588\u2581\u2588\u2581\u2588");
			space();
			add(t, "ship_os");
			add(t, "ship_cpu");
			add(t, "ship_status");
		}));

		// Unlock
		list.add(new TerminalCommand("unlock", t -> {
			if (this.menu.te.unlocked) {
				space();
				add(t, "already_complete");
				return;
			}

			createTimer(ClientConfig.puzzleLoadDuration.get(), () -> {
				Map<String, String> code = new HashMap<>();
				Random r = new Random();
				for (int i = 0; i < 10; i++) {
					code.put(String.valueOf(i), StringUtils.random());
				}

				StringBuilder original = new StringBuilder();
				StringBuilder result = new StringBuilder();
				for (int i = 0; i < r.nextInt(ClientConfig.maxPuzzleSize.get() - ClientConfig.minPuzzleSize.get())
						+ ClientConfig.minPuzzleSize.get(); i++) {
					String ch = code.keySet().stream().collect(Collectors.toList())
							.get(r.nextInt(code.values().size()));
					original.append(ch);
					result.append(code.get(ch));
				}

				StringBuilder def = new StringBuilder();
				for (Map.Entry<String, String> c : code.entrySet()) {
					def.append(c.getKey() + ":" + c.getValue() + " ");
				}

				space();
				add(t, "verification_needed");
				add(def.toString());
				add(t.getFeedback("translate") + original.toString());
				space();

				awaitResponse(s -> {
					boolean correct = s.equals(result.toString());
					if (correct) {
						add(t, "permission_granted");
						MachinaNetwork.CHANNEL.sendToServer(new C2SCompletePuzzle(this.menu.te.getBlockPos()));
					} else {
						add(t, "incorrect");
						space();
						add(t, "verification_needed");
						add(def.toString());
						add(t.getFeedback("translate") + original.toString());
						this.space();
					}

					return correct;
				});
			});
		}));

		return list;
	}
}
