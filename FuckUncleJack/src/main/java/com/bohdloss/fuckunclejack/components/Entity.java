package com.bohdloss.fuckunclejack.components;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.bohdloss.fuckunclejack.components.entities.PlayerEntity;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.logic.events.DamageEvent;
import com.bohdloss.fuckunclejack.logic.events.HitEvent;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.CRectanglef;
import com.bohdloss.fuckunclejack.render.Shader;

public abstract class Entity implements Tickable{
	
public static Timer timer = new Timer();
	
//client only
public boolean red=false;
//

public boolean dying = false;
public static boolean debugHitboxes=false;
	
public static int randID=0;

public boolean cooldown=false;
public boolean bouncy=true;
public boolean invulnerable=false;
public float x;
public float y;
public float width;
public float height;
public boolean physics=true;
public boolean collision=false;
public boolean ignoreCollision=false;
public boolean forcePhysics=false;
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

public boolean flight;

//cache
protected Vector2f vel=new Vector2f(0,0);

protected static Matrix4f res=new Matrix4f();
protected static Vector2f airCheck=new Vector2f(0, -0.01f);

private Vector2f exec=new Vector2f();

private boolean intersect;
private int chunkIndex;
protected List<Block> collisions = new ArrayList<Block>();
protected List<Entity> entCollisions = new ArrayList<Entity>();
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
	inventory=new Inventory(this, 1);
}

public static int genUID() {
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

protected Block[] getSurroundings() {
	int pseudoW = ((int)width+5);
	int pseudoH = ((int)height+5);
	
	Block[] blocks = new Block[pseudoW*pseudoH];
	int bx = CMath.fastFloor(x);
	int by = CMath.fastFloor(y);
	int ii=0;
	
	int subW = (int)((float)pseudoW/2f);
	int subH = (int)((float)pseudoH/2f);
	
	for(int i=-subW;i<pseudoW-subW;i++) {
		for(int j=-subH;j<pseudoH-subH;j++) {
			try {
			blocks[ii]=world.getBlock(bx+i, by+j);
			} catch(Exception e) {}
			ii++;
		}
	}
	return blocks;
}

public boolean move(Vector2f vec, boolean move) {
	if(world==null) return false;
	if(!physics) {
		if(move) {
		execMove(vec);
		}
		return true;
	}
	intersect=false;
	
	chunkIndex = getChunk();
	
	entCollisions.clear();
	collisions.clear();
	
	Block[] blocks = getSurroundings();
	
	for(int i=0;i<blocks.length;i++) {
		if(blocks[i]==null) continue;
		if(Collision.collide(this, blocks[i], vec)) {
			intersect=true;
			collisions.add(blocks[i]);
		}
		
	}
	try {
		world.entities.forEach((k,v)->{
			if(v!=this) {
				if(Collision.collideEnt(this, v, vec)) {
					intersect=true;
					entCollisions.add(v);
				}
			}
		});
		world.player.forEach((k,v)->{
			if(v!=this) {
				if(Collision.collideEnt(this, v, vec)) {
					intersect=true;
					entCollisions.add(v);
				}
			}
		});
	} catch(ConcurrentModificationException e) {}
	
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
		boolean ry=false;
		boolean rx=false;
		
		if(collisions.size()>0) {
		
		Block highest=null;
		Block lowest=null;
		Block mleft=null;
		Block mright=null;
		//WARNING
		//PROGRAMMING HORROR
		
		Block last=null;
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
		
		
		
		if(vec.y<0) {
			exec.y=highest.y+0.5f+0.005f+height/2f;
			ry=true;
		}
		if(vec.y>0) {
			exec.y=lowest.y-0.5f-0.005f-height/2f;
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
		
		}
		
		if(entCollisions.size()>0) {
		
		Entity ehighest=null;
		Entity elowest=null;
		Entity emleft=null;
		Entity emright=null;
		//WARNING
		//PROGRAMMING HORROR
		
		Entity elast=null;
		elast=entCollisions.get(0);
		for(int i=0;i<entCollisions.size();i++) {
			if(entCollisions.get(i).getY()<elast.getY()) {
				elast=entCollisions.get(i);
			}
		}
		elowest=elast;
		elast=entCollisions.get(0);
		for(int i=0;i<entCollisions.size();i++) {
			if(entCollisions.get(i).getY()>elast.getY()) {
				elast=entCollisions.get(i);
			}
		}
		ehighest=elast;
		elast=entCollisions.get(0);
		for(int i=0;i<entCollisions.size();i++) {
			if(entCollisions.get(i).getX()<elast.getX()) {
				elast=entCollisions.get(i);
			}
		}
		emleft=elast;
		elast=entCollisions.get(0);
		for(int i=0;i<entCollisions.size();i++) {
			if(entCollisions.get(i).getX()>elast.getX()) {
				elast=entCollisions.get(i);
			}
		}
		emright=elast;
		
		//END OF FIRST PART OF HORROR
		//PROGRAMMING HORROR PT.2
		
		if(vec.y<0) {
			exec.y=ehighest.y+ehighest.height/2+0.005f+height/2f;
			ry=true;
		}
		if(vec.y>0) {
			exec.y=elowest.y-elowest.height/2-0.005f-height/2f;
			ry=true;
		}
		if(vec.x>0) {
			exec.x=emleft.x-emleft.width/2-0.005f-width/2f;
			rx=true;
		}
		if(vec.x<0) {
			exec.x=emright.x+emright.width/2+0.005f+width/2f;
			rx=true;
		}
		
		}
		
		execPos(exec);
		
		if(rx) velx=0;
		if(ry) {
			//if(!checkFallDamage()) {
				vely=0;
			//}
		}
		
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
	
	float speedAmount = (running?runSpeed:speed)*(flight?2:1);
	
	velx=CMath.limit(velx+(direction*speedAmount)/7f, -speedAmount, speedAmount);
}

public void moveVertical(float direction) {
	if(!flight) return;
	
	float speedAmount = (running?runSpeed:speed)*2;
	
	vely=CMath.limit(vely+(direction*speedAmount)/7f, -speedAmount, speedAmount);
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
	if(physics|forcePhysics) physics();
}

public boolean checkFallDamage() {
	if(invulnerable|vely>0.2f) return false;
	float damage = (float)CMath.remap(vely, 0, -0.5f, 0, 1.5f);
	return damageSelf(damage);
}

public void physics() {
	vel.x=velx;
	vel.y=0;
	move(vel, true);
	vel.x=0;
	vel.y=vely;
	move(vel, true);
	if(!flight) {
		if(inAir()) {
			vely+=gravity.y;
			vely=CMath.limitMin(vely, -0.5f);
		} else {
			vely=0;
		}
	} else {
		if(vely>0) {
			vely-=0.01f;
			vely=CMath.limitMin(vely, 0);
		}
		if(vely<0) {
			vely+=0.01f;
			vely=CMath.limitMax(vely, 0);
		}
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
	s.setUniform("red", red);
	res = input.translate(x, y, 0, res);
	s.setUniform("projection", res);
	Assets.textures.get(texture).bind(0);
	Assets.models.get(model).render();
	s.setUniform("red", false);
	renderHitboxes(s, input);
}

public final void renderHitboxes(Shader s, Matrix4f input) {
	if(debugHitboxes) {
		res = input.translate(x, y, 0, res).scale(width, height, 1, res);
		s.setUniform("projection", res);
		Assets.textures.get("green").bind(0);
		Assets.models.get("square").render();
	}
}

public float stretchyY() {
	return (float)CMath.remap(CMath.fastAbs(vely), 0, 0.5f, 0, 0.5f);
}

public float stretchyX() {
	return (float)CMath.remap(CMath.fastAbs(vely), 0, 0.5f, 0, -0.5f);
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

public Object[] getData() {
	return null;
}

public boolean damageSelf(float damage) {
	if(invulnerable|damage<=0) return false;
	boolean ret=false;
	if(!EventHandler.damagedEntity(!invulnerable, new DamageEvent(this, null, GameEvent.damagedItself)).isCancelled()) {
		ret=true;	
		health-=damage;
		checkDeath();
			
		if(bouncy) {
				
			Random r = new Random();
			
			velx=(float)(r.nextFloat()/4d);
			vely=0.2f;
			
		}
			
		boolean savedVulnerable = invulnerable;
		invulnerable=true;
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				invulnerable=savedVulnerable;
			}
		};
		timer.schedule(task, 1000);
	}
	return ret;
}

public boolean hit(Entity issuer) {
	if(issuer.cooldown|invulnerable) return false;
	
	boolean ret=false;
	
		if(!EventHandler.hitEntity(GameState.isClient.getValue(), new HitEvent(issuer, this, GameEvent.hitEntity)).isCancelled()) {
			ret=true;
			if(!EventHandler.damagedEntity(!invulnerable, new DamageEvent(this, issuer, GameEvent.damagedByEntity)).isCancelled()) {
				Item i = issuer.getInventory().slots[issuer.getInventory().selected].getContent();
				ItemEventProperties prop = null;
				if(i==null) {
					prop = Item.defaultProperties();
				} else {
					prop=i.onLeftClickBegin(CMath.fastFloor(x), CMath.fastFloor(y), this);
				}
				if(!invulnerable) {
					health-=prop.getDamage();
					checkDeath();
					
					if(bouncy) {
					
						double distance = CMath.distance(issuer.getX(), issuer.getY(), x, y);
						
						double cx = (x-issuer.getX())/distance;
						double cy = (y-issuer.getY())/distance;
					
						velx=(float)(cx/4d);
						vely=CMath.limitMin((float)(cy/2d), 0.2f);
					
					}
					
					issuer.cooldown=true;
					boolean savedVulnerable = invulnerable;
					invulnerable=true;
					
					TimerTask task = new TimerTask() {
						@Override
						public void run() {
							issuer.cooldown=false;
							invulnerable=savedVulnerable;
						}
					};
					timer.schedule(task, 1000);
					
				}
			}
		}

	
	return ret;
}

private void checkDeath() {
	if(health<=0f&!invulnerable) die();
}

public void die() {
	dying=true;
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			destroy();
		}
	};
	invulnerable=true;
	
	timer.schedule(task, 500);
}

}
