package com.bohdloss.fuckunclejack.hud;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.client.Client;
import com.bohdloss.fuckunclejack.components.Inventory;
import static com.bohdloss.fuckunclejack.logic.ClientState.*;

import java.util.concurrent.Callable;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.BlockTexture;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.CRectanglef;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Point2f;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;
import com.bohdloss.fuckunclejack.client.ChunkRequest;

public class HUD {
private Inventory inventory;

public Bar health;
public Bar armour;
public InvDisplay invdisplay;
public Hotbar hotbar;
public InteractionDisplay interact;

private boolean invopen;

//cache
public static Point2f mpoint;
static Model square;
static Matrix4f res=new Matrix4f();
static Matrix4f translate=new Matrix4f();

static int i=0;

static Texture red;
static Texture green;
static Texture yellow;
static BlockTexture leaves;
//end

static {
	square=Assets.models.get("square");
	
	red=Assets.textures.get("bar_health");
	green=Assets.textures.get("green");
	yellow=Assets.textures.get("bar_armour");
	leaves=Assets.blocks.get("dirt");
}

	public HUD() {
		health=new Bar(Assets.textures.get("icon_health"), Assets.textures.get("bar_bg"), Assets.textures.get("bar_health"), -10.5f, -4f, 20, 20);
		armour=new Bar(Assets.textures.get("icon_health"), Assets.textures.get("bar_bg"), Assets.textures.get("bar_armour"), -10.5f, -6f, 100, 25);
		invdisplay=new InvDisplay();
		hotbar=new Hotbar();
		interact = new InteractionDisplay();
		new Button("fuck off :D",0,0).setAction(new Callable<Integer>() {
			public Integer call() {
				System.out.println("Clicked!");
				return 0;
			}
		});
	}
	
	public void toggleInventory() {
		invopen=!invopen;
	}
	
	public boolean isInvOpen() {
		return invopen;
	}
	
	public void render(Shader s, Matrix4f matrix) {
		mpoint=CMath.mGLCoord(Game.guiScale);
		
		health.current=lPlayer.getHealth();
		health.render(s, matrix);
		armour.render(s, matrix);
		if(invopen) {
			invdisplay.render(s, matrix);
		} else {
			hotbar.render(s, matrix);
		}
		if(grabbed!=null) {
			
			grabbed.render(s, matrix, mpoint.x, mpoint.y);
		}
		interact.render(s, matrix);
		
		Button.buttons.forEach(v->{
			if(v.status!=Button.DISABLED) {
				if(v.bounds.pIntersects(mpoint)) {
					if(v.status==Button.IDLE) {
						v.status=Button.HOVERED;
					}
				} else {
					if(v.status==Button.HOVERED) {
						v.status=Button.IDLE;
					}
				}
			}
			v.render(s, matrix);
		});
		/*
		try {
			i=0;
			Client.chunkrequest.forEach((k,v)->{
				translate.identity().translate(12, 6-(i++), 0);
				res=matrix.mul(translate, res);
				s.setUniform("projection", res);
				Texture t = null;
				switch(v.status) {
				case ChunkRequest.UNSENT:
					t=red;
				break;
				case ChunkRequest.ELABORATING:
					t=yellow;
				break;
				case ChunkRequest.READY:
					t=green;
				break;
				}
				if(t!=null) t.bind(0);
				square.render();
				FontManager.renderString(12, 6-(i)+1, Assets.sheets.get("font"), Assets.shaders.get("gui"), matrix, square, v.x+"");
				s.bind();
			});
			
			translate.identity().translate(-12, 5, 0);
			res=matrix.mul(translate, res);
			s.setUniform("projection", res);
			leaves.txt[leaves.txt.length-1].bind(0);
			square.render();
			FontManager.renderString(-12, 6, Assets.sheets.get("font"), Assets.shaders.get("gui"), matrix, square, lPlayer.getChunk()+"");
			s.bind();
			
		} catch(Exception e) {
			//e.printStackTrace();
		}
		*/
		
	}
	
}
