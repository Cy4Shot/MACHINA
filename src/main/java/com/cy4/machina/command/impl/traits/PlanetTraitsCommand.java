package com.cy4.machina.command.impl.traits;

import com.cy4.machina.command.BaseCommand;

public abstract class PlanetTraitsCommand extends BaseCommand {

	public PlanetTraitsCommand(int permissionLevel, boolean enabled) {
		super(permissionLevel, enabled);
	}

}
