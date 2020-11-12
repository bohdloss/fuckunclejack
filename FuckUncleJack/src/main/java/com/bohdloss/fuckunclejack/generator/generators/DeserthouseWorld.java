package com.bohdloss.fuckunclejack.generator.generators;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.HouseEntity;
import com.bohdloss.fuckunclejack.generator.dungeoncommon.Dungeon;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.Camera;
import com.bohdloss.fuckunclejack.render.Mesh;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public class DeserthouseWorld extends World {
	
protected static Matrix4f translate = new Matrix4f();
protected Mesh[] background = new Mesh[3];
private static final float width = 25f;
private static final float height = 17.5f;

protected Dungeon dungeon;
public HouseEntity house;

	public DeserthouseWorld(long seed, String name) {
		super(seed, name);
		generator = new HouseGenerator(this, seed);
		dungeon = new Dungeon(this);
		commonInit();
	}

	public DeserthouseWorld(String name) {
		super(name);
		commonInit();
		for(int i=0;i<background.length;i++) {
			background[i] = new Mesh(getTexture(), Assets.models.get("square"));
			background[i].setXScale(width);;
			background[i].setYScale(height);
			background[i].setX((i-1)*width);
		}
	}

	@Override
	public void tick(float delta) {
		super.tick(delta);
		//if(dungeon!=null) dungeon.tick(delta);
	}
	
	private void commonInit() {
		needsLightmap=true;
	}
	
	@Override
	public int getID() {
		return 1;
	}

	@Override
	protected Texture getTexture() {
		return Assets.textures.get("deserthouse_world_sky");
	}
	
	@Override
	protected void renderBackground(Shader s, Matrix4f matrix) {
		Camera c = Game.camera;
		c.unTransformedProjection().mul(Game.scale, res);
		
		float movFactor = (-c.getX()/Game.scaleAmount)/1.25f;
		
		translate.identity().translate(movFactor, 0, 0);
		res.mul(translate);
		
		//Check if meshes have to be moved
		
		float centerMeshX = background[1].getX();
		float halfWidth = width/2;
		
		if(-movFactor>centerMeshX+halfWidth) {
			
			//shift everything to the right
			
			for(int i=0;i<background.length;i++) {
				Mesh current = background[i];
				current.setX(current.getX()+width);
			}
			
		} else if(-movFactor<centerMeshX-halfWidth) {
			
			//shift everything to the left
			
			for(int i=0;i<background.length;i++) {
				Mesh current = background[i];
				current.setX(current.getX()-width);
			}
			
		}
		
		for(int i=0;i<background.length;i++) {
			
			background[i].render(s, res);
			
		}
	}
	
}
