package com.bohdloss.fuckunclejack.components;

import static com.bohdloss.fuckunclejack.render.CMath.fastFloor;
import static com.bohdloss.fuckunclejack.render.CMath.lerp;
import static com.bohdloss.fuckunclejack.render.CMath.limit;
import static com.bohdloss.fuckunclejack.render.CMath.reverseLerp;

import java.util.Random;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public class BlockLayer {

private Block background;
private Block top;
private int x;
private int worldx;
private int y;
private World world;
private Chunk chunk;

private Texture hovered;
private Model model;

//faster GC
private Matrix4f res=new Matrix4f();
//end

public BlockLayer(Block background, Block top) {
	this.background=background;
	this.background.setBackground(true);
	this.top=top;
	this.top.setBackground(false);
	worldx=top.getWorldx();
	x=top.getChunkx();
	y=top.getY();
	world=top.getChunk().getWorld();
	chunk=top.getChunk();
	hovered=Assets.textures.get("hovered_block");
	model=Assets.models.get("square");
}
	
public void render(Shader s, Matrix4f matrix) {
				
	res = matrix.translate(worldx, y, 0f, res);
	s.setUniform("projection", res);
	
	if(top.opaque) {
		background.render(s, matrix, chunk.lightmap.values[x][y]);
	}
	top.render(s, matrix, chunk.lightmap.values[x][y]);
	if(ClientState.hovx==worldx&ClientState.hovy==y) {
		hovered.bind(0);
		model.render();
	}
}

public void tick() {
	background.tick();
	top.tick();
}

public Block getBackground() {
	return background;
}

public void setBackground(Block background) {
	this.background = background;
	this.background.setBackground(true);
}

public Block getTop() {
	return top;
}

public void setTop(Block top) {
	this.top = top;
	this.top.setBackground(false);
}

public int getX() {
	return worldx;
}

public int getChunkX() {
	return x;
}

public int getY() {
	return y;
}

public World getWorld() {
	return world;
}

}
