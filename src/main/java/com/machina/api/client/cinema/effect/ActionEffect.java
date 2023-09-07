package com.machina.api.client.cinema.effect;

import java.util.function.Consumer;

public record ActionEffect(Consumer<Integer> run) implements CameraEffect {

	@Override
	public void tickEffect(int tick) {
		run.accept(tick);
	}
}