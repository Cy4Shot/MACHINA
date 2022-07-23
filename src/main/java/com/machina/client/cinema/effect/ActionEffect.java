package com.machina.client.cinema.effect;

import java.util.function.Consumer;

public class ActionEffect extends CameraEffect {
	
	Consumer<Integer>  run;
	
	public ActionEffect(Consumer<Integer> toRun) {
		this.run = toRun;
	}

	@Override
	public void tickEffect(int tick) {
		run.accept(tick);
	}

}
