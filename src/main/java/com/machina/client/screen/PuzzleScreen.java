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
import com.machina.network.message.C2SCompletePuzzle;
import com.machina.util.text.StringUtils;

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
		list.add(new TerminalCommand("clear", "Clears the terminal.", () -> {
			history.clear();
		}));

		// Unlock
		list.add(new TerminalCommand("unlock", "Lifts security on cargo.", () -> {
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

				history.add("");
				history.add("Security Verification Needed");
				history.add(def.toString());
				history.add("Translate: " + original.toString());
				history.add("");

				awaitResponse(s -> {
					boolean correct = s.equals(result.toString());
					if (correct) {
						history.add("Permission granted.");
						history.add("");
						MachinaNetwork.CHANNEL.sendToServer(new C2SCompletePuzzle(this.menu.te.getBlockPos()));
					} else {
						history.add("Incorrect.");
						history.add("");
						history.add("Security Verification Needed");
						history.add(def.toString());
						history.add("Translate: " + original);
						history.add("");
					}

					return correct;
				});
			});
		}));

		return list;
	}
}
