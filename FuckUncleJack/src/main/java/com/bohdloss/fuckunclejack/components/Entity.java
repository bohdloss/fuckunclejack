package com.bohdloss.fuckunclejack.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.bohdloss.fuckunclejack.components.entities.Player;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.CRectanglef;
import com.bohdloss.fuckunclejack.render.Shader;

public abstract class Entity implements Tickable{

private static final char[] alphabet="abcdefghijklmnopqrstuvwxyz".toCharArray();
	
public static int randID=0;

public float x;
public float y;
public float width;
public float height;
public boolean collision=true;
public String texture;
public String model;

protected float speed=0.1f;
protected float runSpeed=0.2f;
protected boolean running;
protected CRectanglef bounds;

protected float velx;
protected float vely;

protected Vector2f gravity=new Vector2f(0, -0.03f);

protected World world;
protected Inventory inventory;
protected float health;
protected float maxhealth;

private int uid;

//better GC
private Vector2f vel=new Vector2f(0,0);

private Matrix4f res=new Matrix4f();
private Vector2f airCheck=new Vector2f(0, -0.01f);

private Block highest;
private Block lowest;
private Block mleft;
private Block mright;
private Block last;
private Vector2f exec=new Vector2f();

private boolean intersect;
private int chunkIndex;
private List<Block> collisions = new ArrayList<Block>();
//end

public Entity(String model, String texture, float maxhealth) {
	this.x=0;
	this.y=0;
	this.model=model;
	this.texture=texture;
	this.maxhealth=maxhealth;
	this.health=maxhealth;
	bounds=new CRectanglef(0,0,1f,1f);
	setUID(genUID());
}

public static int genUID() {
	/*
	Random r = new Random();
	String res="";
	for(int i=0;i<20;i++) {
		if(r.nextBoolean()) {
			res=res+r.nextInt(10);
		} else {
			char c = alphabet[r.nextInt(alphabet.length)];
			if(r.nextBoolean()) {
				c=Character.toUpperCase(c);
			} else {
				c=Character.toLowerCase(c);
			}
			res=res+c;
		}
	}
	return res;
	*/
	return randID++;
}

public int getChunk() {
	return CMath.fastFloor(x/16d);
}

public double getChunkX() {
	int chunk = getChunk();
	return CMath.fastFloor(x-(chunk-1)*16-15);
}

public void join(World world, float x, float y) {
	this.world=world;
	this.x=x;
	this.y=y;
	updateBounds();
}

protected abstract Block[] getSurroundings();

public boolean move(Vector2f vec, boolean move) {
	if(world==null) return false;
	if(!collision) {
		if(move) {
		execMove(vec);
		}
		return true;
	}
	intersect=false;
	
	chunkIndex = getChunk();
	
	collisions.clear();
	
	Block[] blocks = getSurroundings();
	
	for(int i=0;i<blocks.length;i++) {
		if(blocks[i]==null) continue;
		if(Collision.collide(this, blocks[i], vec)) {
			intersect=true;
			collisions.add(blocks[i]);
		}
		
	}
	
	if(!intersect) {
		if(move) {
		execMove(vec);
		}
		return true;
	} else if(move) {
		
		/* Basically this tries to
		 * set the position of the
		 * entity as close as possible
		 * to the destination, when
		 * normal collision detection
		 * won't allow it
		 */
		
		exec.x=x;
		exec.y=y;
		
		highest=null;
		lowest=null;
		mleft=null;
		mright=null;
		//WARNING
		//PROGRAMMING HORROR
		
		last=null;
		last=collisions.get(0);
		for(int i=0;i<collisions.size();i++) {
			if(collisions.get(i).getY()<last.getY()) {
				last=collisions.get(i);
			}
		}
		lowest=last;
		last=collisions.get(0);
		for(int i=0;i<collisions.size();i++) {
			if(collisions.get(i).getY()>last.getY()) {
				last=collisions.get(i);
			}
		}
		highest=last;
		last=collisions.get(0);
		for(int i=0;i<collisions.size();i++) {
			if(collisions.get(i).getWorldx()<last.getWorldx()) {
				last=collisions.get(i);
			}
		}
		mleft=last;
		last=collisions.get(0);
		for(int i=0;i<collisions.size();i++) {
			if(collisions.get(i).getWorldx()>last.getWorldx()) {
				last=collisions.get(i);
			}
		}
		mright=last;
		
		//END OF FIRST PART OF HORROR
		//PROGRAMMING HORROR PT.2
		
		boolean ry=false;
		boolean rx=false;
		
		if(vec.y<0) {
			exec.y=highest.y+0.5f+height/2f;
			ry=true;
		}
		if(vec.y>0) {
			exec.y=lowest.y-0.5f-height/2f;
			ry=true;
		}
		if(vec.x>0) {
			exec.x=mleft.worldx-0.505f-width/2f;
			rx=true;
		}
		if(vec.x<0) {
			exec.x=mright.worldx+0.505f+width/2f;
			rx=true;
		}
		
		execPos(exec);
		
		if(rx) velx=0;
		if(ry) vely=0;
		
		//END OF THE HORROR SERIES
	} 
	return false;
}

public void setRunning(boolean running) {
	this.running=running;
}

public void setVelocity(float x, float y) {
	velx=x;
	vely=y;
}

public boolean isRunning() {
	return running;
}

public CRectanglef getBounds() {
	return bounds;
}

public void execMove(Vector2f vec) {
	x+=vec.x;
	y+=vec.y;
	updateBounds();
}

public void execPos(Vector2f vec) {
	x=vec.x;
	y=vec.y;
	updateBounds();
}

public void updateBounds() {
	bounds.setFrame(x-(width/2), y-(height/2), width, height);
}

public boolean inAir() {
	return move(airCheck, false);
}

public void jump() {
	if(!inAir()) {
		vely=0.4f;
	}
}

public void moveLateral(float direction) {
	velx=CMath.limit(velx+(direction*(running?runSpeed:speed))/7f, -(running?runSpeed:speed), (running?runSpeed:speed));
}

private void chunkTest() {
	if(!GameState.isClient.getValue()) return;
	if(this!=ClientState.lPlayer) {return;}
	
	chunkIndex = getChunk();
	if(world.chunks.get(chunkIndex-1)==null) {
		world.getChunk(chunkIndex-1, true);
	}
	if(world.chunks.get(chunkIndex)==null) {
		world.getChunk(chunkIndex, true);
	}
	if(world.chunks.get(chunkIndex+1)==null) {
		world.getChunk(chunkIndex+1, true);
	}
}

public void tick() {
	chunkTest();
	if(collision) physics();
}

public void physics() {
	vel.x=velx;
	vel.y=0;
	move(vel, true);
	vel.x=0;
	vel.y=vely;
	move(vel, true);
	if(inAir()) {
	vely+=gravity.y;
	vely=CMath.limitMin(vely, -0.5f);
	} else {
		vely=0;
	}
if(velx>0) {
	velx-=0.01f;
	velx=CMath.limitMin(velx, 0);
}
if(velx<0) {
	velx+=0.01f;
	velx=CMath.limitMax(velx, 0);
}
}

public void render(Shader s, Matrix4f input) {
	res = input.translate(x, y, 0, res);
	s.setUniform("projection", res);
	Assets.textures.get(texture).bind(0);
	Assets.models.get(model).render();
}

@Override
public void finalize() throws Throwable{
	super.finalize();
}

public void destroy() {
	world.entities.remove(uid);
	try {
		finalize();
	} catch(Throwable t) {
		t.printStackTrace();
	}
}

public String getTexture() {
	return texture;
}

public void setTexture(String texture) {
	this.texture = texture;
}

public float getSpeed() {
	return speed;
}

public void setSpeed(float speed) {
	this.speed = speed;
}

public float getRunSpeed() {
	return runSpeed;
}

public void setRunSpeed(float runSpeed) {
	this.runSpeed = runSpeed;
}

public float getX() {
	return x;
}

public float getY() {
	return y;
}

public float getWidth() {
	return width;
}

public float getHeight() {
	return height;
}

public Vector2f getVelocity() {
	return new Vector2f(velx, vely);
}

public World getWorld() {
	return world;
}

public Inventory getInventory() {
	return inventory;
}

public float getHealth() {
	return health;
}

public void setHealth(float health) {
	this.health = CMath.limit(health, 0, maxhealth);
}

public float getMaxhealth() {
	return maxhealth;
}

public float getPercentHealth() {
	return health/maxhealth;
}

public abstract int getId();

public int getUID() {
	return uid;
}

public void setUID(int uid) {
	this.uid=uid;
}

}
