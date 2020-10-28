package com.bohdloss.fuckunclejack.components;

import static com.bohdloss.fuckunclejack.render.CMath.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.BlockTexture;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public class BlockLayer implements Tickable{

private Block background;
private Block top;
private int x;
private int worldx;
private int y;
private World world;
private Chunk chunk;

private Texture hovered;
private Model model;

//cache
private static Matrix4f res;
private static Shader blockS;
private static Texture empty;
private static FloatBuffer lightBuffer;
//end

static {
	res = new Matrix4f();
	blockS = Assets.shaders.get("block");
	empty = Assets.textures.get("empty");
	lightBuffer = BufferUtils.createFloatBuffer(9);
}

public static float grayf=0;

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
	s.setProjection(res);
	if(top.opaque) {
		background.render(s, res, BlockTexture.NOSIDES);
	}
	top.render(s, res, chunk.lightmap.sides[x][y]);
	if(ClientState.hovx==worldx&ClientState.hovy==y) {
		hovered.bind(0);
		model.render();
	}
	
	//First fill the light buffer with data from
	//the lightmap
	
	fillLightBuffer();
	
	//Now bind block shader and draw black shader
	//This simulates shadow using interpolation
	//of the alpha channel
	
	blockS.bind();
	blockS.setProjection(res);
	blockS.setUniform("light_uniform", lightBuffer);
	
	empty.bind(0);
	model.render();
	
	s.bind();
}

private void fillLightBuffer() {
	
	lightBuffer.rewind();
	
	lightBuffer.put(chunk.lightmap.get(x-1, y-1));
	lightBuffer.put(chunk.lightmap.get(x, y-1));
	lightBuffer.put(chunk.lightmap.get(x+1, y-1));
	
	lightBuffer.put(chunk.lightmap.get(x-1, y));
	lightBuffer.put(chunk.lightmap.get(x, y));
	lightBuffer.put(chunk.lightmap.get(x+1, y));
	
	lightBuffer.put(chunk.lightmap.get(x-1, y+1));
	lightBuffer.put(chunk.lightmap.get(x, y+1));
	lightBuffer.put(chunk.lightmap.get(x+1, y+1));
	
	lightBuffer.rewind();
	
}

public void tick(float delta) {
	background.tick(delta);
	top.tick(delta);
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
