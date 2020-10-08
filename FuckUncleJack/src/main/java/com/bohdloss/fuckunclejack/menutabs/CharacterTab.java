package com.bohdloss.fuckunclejack.menutabs;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.Callable;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.bohdloss.fuckunclejack.api.CharacterInfo;
import com.bohdloss.fuckunclejack.api.FUJApi;
import com.bohdloss.fuckunclejack.api.GamemodeInfo;
import com.bohdloss.fuckunclejack.api.SkinInfo;
import com.bohdloss.fuckunclejack.guicomponents.AnimationPacket;
import com.bohdloss.fuckunclejack.guicomponents.AnimationSystem;
import com.bohdloss.fuckunclejack.guicomponents.GuiButton;
import com.bohdloss.fuckunclejack.guicomponents.GuiStatistics;
import com.bohdloss.fuckunclejack.guicomponents.SmoothAnimationPacket;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.Animation;
import com.bohdloss.fuckunclejack.render.AnimationSet;
import com.bohdloss.fuckunclejack.render.Label;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;

public class CharacterTab extends MenuTab {
	
private Label gamemode;
private GuiButton charname;

private static Shader gui;
private static Model player;
private Animation lastAnim;
private AnimationSystem other;

static {
	gui=Assets.shaders.get("gui");
	player=Assets.models.get("player_model");
}

	public CharacterTab() {
		super("character");
		
		gamemode=new Label(FUJApi.getGamemode().getName(), new Font("Arial", Font.BOLD, 15), new Color(25, 25, 25));
		gamemode.setY(4);
		
		float mulConst=35;
		
		GuiButton butBack = (GuiButton) new GuiButton(this, "Back", -12f, 6.5f, 2f, 1.25f, Assets.sheets.get("metalbuttons"), new Color(0, 0, 0, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				FUJApi.pushAll();
				gamemode.setText(FUJApi.getGamemode().getName());
				ClientState.showMenu(true, true, "main");
				updateName();
				return 0;
			}
		}).setMulConst(mulConst).updateSize();
		GuiButton butPrevMode = (GuiButton) new GuiButton(this, "<", -2, 4, 1.25f, 1.25f, Assets.sheets.get("metalbuttons"), new Color(100, 0, 0, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				previousMode();
				updateName();
				return 0;
			}
		}).setMulConst(mulConst).updateSize();
		GuiButton butNextMode = (GuiButton) new GuiButton(this, ">", 2, 4, 1.25f, 1.25f, Assets.sheets.get("metalbuttons"), new Color(100, 0, 0, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				nextMode();
				updateName();
				return 0;
			}
		}).setMulConst(mulConst).updateSize();
		GuiButton butPrevChar = (GuiButton) new GuiButton(this, "<", -5, -1.75f, 1.5f, 1.5f, Assets.sheets.get("metalbuttons"), new Color(0, 100, 0, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				previousCharacter();
				updateName();
				return 0;
			}
		}).setMulConst(mulConst).updateSize();
		GuiButton butNextChar = (GuiButton) new GuiButton(this, ">", 5, -1.75f, 1.5f, 1.5f, Assets.sheets.get("metalbuttons"), new Color(0, 100, 0, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				nextCharacter();
				updateName();
				return 0;
			}
		}).setMulConst(mulConst).updateSize();
		charname=(GuiButton)new GuiButton(this, "", 0f, -6.5f, 5f, 1.25f, Assets.sheets.get("metalbuttons"), new Color(0, 0, 0, 0)).setAction(new Callable<Integer>() {
			public Integer call() {
				
				//TODO Update animation and api
				
				return 0;
			}
		}).setMulConst(mulConst).updateSize();
		GuiButton butPrevSkin = (GuiButton) new GuiButton(this, "<", -5, -3.75f, 1f, 1f, Assets.sheets.get("metalbuttons"), new Color(0, 0, 100, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				previousSkin();
				updateName();
				return 0;
			}
		}).setMulConst(mulConst).updateSize();
		GuiButton butNextSkin = (GuiButton) new GuiButton(this, ">", 5, -3.75f, 1f, 1f, Assets.sheets.get("metalbuttons"), new Color(0, 0, 100, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				nextSkin();
				updateName();
				return 0;
			}
		}).setMulConst(mulConst).updateSize();
		
		AnimationPacket back = new SmoothAnimationPacket("back", new Vector4f(), butBack.transformInfo(), 500);
		AnimationPacket prev = new SmoothAnimationPacket("prev", new Vector4f(), butPrevMode.transformInfo(), 500);
		AnimationPacket next = new SmoothAnimationPacket("next", new Vector4f(), butNextMode.transformInfo(), 500);
		AnimationPacket nextc = new SmoothAnimationPacket("nextc", new Vector4f(), butNextChar.transformInfo(), 500);
		AnimationPacket prevc = new SmoothAnimationPacket("prevc", new Vector4f(), butPrevChar.transformInfo(), 500);
		AnimationPacket player = new SmoothAnimationPacket("player", new Vector4f(), new Vector4f(0, -2, 8, 4), 500);
		AnimationPacket gmlabel = new SmoothAnimationPacket("gmlabel", new Vector4f(), new Vector4f(0,4.0f,gamemode.getTextWidth(),gamemode.getTextHeight()), 500);
		AnimationPacket bgdeform = new SmoothAnimationPacket("bgdeform", new Vector4f(0,0,1,1), new Vector4f(0,0,1.05f,1.05f), 500);
		AnimationPacket plname = new SmoothAnimationPacket("plname", new Vector4f(), charname.transformInfo(), 500);
		AnimationPacket prevs = new SmoothAnimationPacket("prevs", new Vector4f(), butPrevSkin.transformInfo(), 500);
		AnimationPacket nexts = new SmoothAnimationPacket("nexts", new Vector4f(), butNextSkin.transformInfo(), 500);
		
		AnimationPacket bar = new SmoothAnimationPacket("bar", new Vector4f(0,9,0,0), new Vector4f(0,6.65f,0,0), 500);
		
		fade = new AnimationSystem("character", back, prev, next, player, nextc, prevc, gmlabel, bgdeform, plname, prevs, nexts);
		other = new AnimationSystem("character_other", bar);
	}
	
	@Override
	public void onActivate() {
		FUJApi.fetchAll();
		gamemode.setText(FUJApi.getGamemode().getName());
		super.onActivate();
		other.reset();
		updateName();
	}
	
	@Override
	public void render(Shader s, Matrix4f matrix) {
		s.bind();
		
		((GuiButton)components.get(1)).x=-(1.5f+gamemode.getTexture().getGLWidth()/2f);
		((GuiButton)components.get(2)).x=(1.5f+gamemode.getTexture().getGLWidth()/2f);
		AnimationPacket packet;
		//Background
		
		packet = fade.animations.get("bgdeform").calc();
		res.identity().scale(packet.getXscale(), packet.getYscale(), 1);
		translation.identity().translate(0, 0, 0).scale(26.9f, 15.2f, 1).mul(res);
		setShader(s, translation, matrix);
		renderSprite("characters_background");
		
		//Back button animation
		
		packet = fade.animations.get("back").calc();
		calcMatrix(packet);
		setShader(s, translation, matrix);
		GuiButton back = (GuiButton) components.get(0);
		back.renderNoTransform(s);
		
		//Previous button animation (<)
		
		SmoothAnimationPacket smooth = (SmoothAnimationPacket) fade.animations.get("prev");
		smooth.end.x=-(1.5f+gamemode.getTexture().getGLWidth()/2f);
		
		calcMatrix(smooth.calc());
		setShader(s, translation, matrix);
		GuiButton prev = (GuiButton) components.get(1);
		prev.renderNoTransform(s);
		
		//Next button animation (>)
		
		SmoothAnimationPacket smooth2 = (SmoothAnimationPacket) fade.animations.get("next");
		smooth2.end.x=(1.5f+gamemode.getTexture().getGLWidth()/2f);
		
		calcMatrix(smooth2.calc());
		setShader(s, translation, matrix);
		GuiButton next = (GuiButton) components.get(2);
		next.renderNoTransform(s);
		
		//Render gamemode
		SmoothAnimationPacket gmlabel = (SmoothAnimationPacket) fade.animations.get("gmlabel");
		gmlabel.end.z=gamemode.getTextWidth();
		gmlabel.end.w=gamemode.getTextHeight();
		gmlabel.calc();
		gamemode.setXScale(gmlabel.getXscale());
		gamemode.setYScale(gmlabel.getYscale());
		gamemode.setX(gmlabel.getX());
		gamemode.setY(gmlabel.getY());
		gamemode.render(s, matrix);
		
		//Render idle animation
		
		gui.bind();
		calcMatrix(fade.animations.get("player").calc());
		setShader(gui, translation, matrix);
		AnimationSet nonnull = FUJApi.getSkin().getData();
		if(nonnull!=null) {
			Animation anim = nonnull.longIdle;
			if(anim!=lastAnim) (lastAnim=anim).clear();
			anim.setSpeed(2);
			anim.bind();
			player.render();
		}
		s.bind();
		
		//Render arrows to change selected character
		
		calcMatrix(fade.animations.get("prevc").calc());
		setShader(s, translation, matrix);
		GuiButton prevc = (GuiButton) components.get(3);
		prevc.renderNoTransform(s);
		
		calcMatrix(fade.animations.get("nextc").calc());
		setShader(s, translation, matrix);
		GuiButton nextc = (GuiButton) components.get(4);
		nextc.renderNoTransform(s);
		
		//Render character name (with skin page link)
		
		calcMatrix(fade.animations.get("plname").calc());
		setShader(s, translation, matrix);
		GuiButton plname = (GuiButton) components.get(5);
		plname.renderNoTransform(s);
		
		//render buttons to change skin
		
		calcMatrix(fade.animations.get("prevs").calc());
		setShader(s, translation, matrix);
		GuiButton prevs = (GuiButton) components.get(6);
		prevs.renderNoTransform(s);
		
		calcMatrix(fade.animations.get("nexts").calc());
		setShader(s, translation, matrix);
		GuiButton nexts = (GuiButton) components.get(7);
		nexts.renderNoTransform(s);
		
		//Render stats bar
		
		packet = other.animations.get("bar").calc();
		
		translation.identity().translate(packet.getX(), packet.getY(), 0);
		GuiStatistics.INSTANCE.render(s, matrix.mul(translation, res));
	}

	public void nextMode() {
		GamemodeInfo[] a = GamemodeInfo.AVAILABLE;
		int current = FUJApi.getSelectedGameMode();
		if(current!=a.length-1) {
			FUJApi.setSelectetGamemode(a[current+1].getId());
		} else {
			FUJApi.setSelectetGamemode(a[0].getId());
		}
		gamemode.setText(FUJApi.getGamemode().getName());
	}
	
	public void previousMode() {
		GamemodeInfo[] a = GamemodeInfo.AVAILABLE;
		int current = FUJApi.getSelectedGameMode();
		if(current!=0) {
			FUJApi.setSelectetGamemode(a[current-1].getId());
		} else {
			FUJApi.setSelectetGamemode(a[a.length-1].getId());
		}
		gamemode.setText(FUJApi.getGamemode().getName());
	}
	
	public void nextCharacter() {
		CharacterInfo[] info = CharacterInfo.SUBDIVIDED[FUJApi.getSelectedGameMode()];
		int current = indexOf(FUJApi.getSelectedCharacter(), info);
		if(current!=info.length-1) {
			FUJApi.setSelectetCharacter(info[current+1].getId());
		} else {
			FUJApi.setSelectetCharacter(info[0].getId());
		}
	}
	
	public void previousCharacter() {
		CharacterInfo[] info = CharacterInfo.SUBDIVIDED[FUJApi.getSelectedGameMode()];
		int current = indexOf(FUJApi.getSelectedCharacter(), info);
		if(current!=0) {
			FUJApi.setSelectetCharacter(info[current-1].getId());
		} else {
			FUJApi.setSelectetCharacter(info[info.length-1].getId());
		}
	}
	
	public void updateName() {
		charname.text=FUJApi.getSkin().getDisplayName();
		charname.updateSize();
	}
	
	public void nextSkin() {
		SkinInfo[] info = SkinInfo.SUBDIVIDED[FUJApi.getSelectedCharacter()];
		int current = indexOf(FUJApi.getSelectedSkin(), info);
		if(current!=info.length-1) {
			FUJApi.setSelectedSkin(info[current+1].getId());
		} else {
			FUJApi.setSelectedSkin(info[0].getId());
		}
	}
	
	public void previousSkin() {
		SkinInfo[] info = SkinInfo.SUBDIVIDED[FUJApi.getSelectedCharacter()];
		int current = indexOf(FUJApi.getSelectedSkin(), info);
		if(current!=0) {
			FUJApi.setSelectedSkin(info[current-1].getId());
		} else {
			FUJApi.setSelectedSkin(info[info.length-1].getId());
		}
	}
	
	private int indexOf(int id, CharacterInfo[] sub) {
		for(int i=0;i<sub.length;i++) {
			if(id==sub[i].getId()) return i;
		}
		return -1;
	}
	
	private int indexOf(int id, SkinInfo[] sub) {
		for(int i=0;i<sub.length;i++) {
			if(id==sub[i].getId()) return i;
		}
		return -1;
	}
	
	@Override
	public void onOver() {
		other.start();
	}
}
