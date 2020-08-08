package com.bohdloss.fuckunclejack.generator.generators;

import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.Texture;

public class OverworldWorld extends World {

	public OverworldWorld(long seed, String name) {
		super(seed, name);
	}

	public OverworldWorld(String name) {
		super(name);
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	protected Texture getTexture() {
		return Assets.textures.get("overworld_world_sky");
	}
	
}
