package com.bohdloss.fuckunclejack.hud;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.client.Client;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.editor.Editor;

import static com.bohdloss.fuckunclejack.logic.ClientState.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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
public Bar health;
public Bar armour;
public Hotbar hotbar;
public InteractionDisplay interact;
public InteractionDisplayExit exit;

//cache
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
		health=new Bar(Assets.textures.get("icon_health"), new Color(100, 0, 0), -10.5f, -4f, 20, 20);
		armour=new Bar(Assets.textures.get("icon_health"), new Color(100, 100, 0), -10.5f, -6f, 100, 25);
		hotbar=new Hotbar();
		interact = new InteractionDisplay();
		exit = new InteractionDisplayExit();
	}
	
	public void render(Shader s, Matrix4f matrix) {
		
		health.setCurrent(lPlayer.getHealth());
		health.render(s, matrix);
		armour.render(s, matrix);
		hotbar.render(s, matrix);
		interact.render(s, matrix);
		exit.render(s, matrix);
		
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
