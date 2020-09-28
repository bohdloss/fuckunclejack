package com.bohdloss.fuckunclejack.menutabs;

import java.awt.Color;

import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

import org.joml.Vector4f;
import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.entities.PlayerEntity;
import com.bohdloss.fuckunclejack.generator.generators.OverworldWorld;
import com.bohdloss.fuckunclejack.guicomponents.AnimationPacket;
import com.bohdloss.fuckunclejack.guicomponents.AnimationSystem;
import com.bohdloss.fuckunclejack.guicomponents.GuiButton;
import com.bohdloss.fuckunclejack.guicomponents.MainAnimationPacket;
import com.bohdloss.fuckunclejack.guicomponents.SmashButtonPacket;
import com.bohdloss.fuckunclejack.guicomponents.SmoothAnimationPacket;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.server.Server;

public class MainTab extends MenuTab {

public static boolean first=true;
public static AnimationSystem buttonfade;
public static AnimationSystem secondfade;

	public MainTab() {
		super("main");
		
		AnimationPacket left = new MainAnimationPacket("left");
		AnimationPacket right = new MainAnimationPacket("right");
		AnimationPacket title = new MainAnimationPacket("title");
		AnimationPacket buttonplay = new SmashButtonPacket("play", 0, 0.75f, 6f, 2f);
		AnimationPacket buttonsettings = new SmashButtonPacket("settings", 0, -1.75f, 4.5f, 1.75f);
		AnimationPacket buttonexit = new SmashButtonPacket("exit", 0, -4.0f, 2.5f, 1.5f);
		
		AnimationPacket buttonplays = new SmoothAnimationPacket("play", new Vector4f(0f,0f,0f,0f), new Vector4f(0, 0.75f, 6f, 2f), 300);
		AnimationPacket buttonsettingss = new SmoothAnimationPacket("settings", new Vector4f(0f,0f,0f,0f), new Vector4f(0, -1.75f, 4.5f, 1.75f), 300);
		AnimationPacket buttonexits = new SmoothAnimationPacket("exit", new Vector4f(0f,0f,0f,0f), new Vector4f(0, -4.0f, 2.5f, 1.5f), 300);
		
		fade=new AnimationSystem("main", left, right, title);
		buttonfade=new AnimationSystem("mainbuttons", buttonplay, buttonsettings, buttonexit);
		secondfade=new AnimationSystem("mainbuttonss", buttonplays, buttonsettingss, buttonexits);
		
		new GuiButton(this, "Play", 0f, 0.75f, 6f, 2f, Assets.sheets.get("menubuttons"), new Color(100, 0, 0, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				//ClientState.showMenu(true, true, "character");
				ClientState.fadeToBlack();
				ClientState.connect("localhost", Server.port);
				ClientState.fadeFromBlack();
				return 0;
			}
		});
		new GuiButton(this, "Settings", 0, -1.75f, 4.5f, 1.75f, Assets.sheets.get("menubuttons"), new Color(0, 100, 0, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				ClientState.showMenu(true, true, "settings");
				return 0;
			}
		});
		new GuiButton(this, "Exit", 0, -4.0f, 2.5f, 1.5f, Assets.sheets.get("menubuttons"), new Color(0, 0, 100, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				ClientState.fadeToBlack();
				System.exit(0);
				return 0;
			}
		});
	}

	@Override
	public void onActivate() {
		if(first) {
			try {
				Thread.sleep(0);
			} catch(Exception e) {}
			super.onActivate();
			Game.blocked=false;
		} else {
			buttonfade.reset();
			buttonfade.start();
		}
	}
	
	@Override
	public void render(Shader s, Matrix4f matrix) {
		
		//Background
		
		translation.identity().translate(0, 0, 0).scale(-26.9f, -15.2f, 1);
		setShader(s, translation, matrix);
		renderSprite("mainbackground");
		
		//Left
		AnimationPacket packet = fade.animations.get("left").calc();
		calcMatrix(packet);
		setShader(s, translation, matrix);
		renderSprite("mainleft");
		//renderSprite("green");
		
		//Right
		
		packet = fade.animations.get("right").calc();
		calcMatrix(packet);
		setShader(s, translation, matrix);
		renderSprite("mainright");
		//renderSprite("green");
		
		//Title
		
		packet = fade.animations.get("title").calc();
		calcMatrix(packet);
		setShader(s, translation, matrix);
		renderSprite("maintitle");
		//renderSprite("green");
		
		packet = buttonfade.animations.get("play").calc();
		calcMatrix(packet);
		setShader(s, translation, matrix);
		GuiButton but = (GuiButton) components.get(0);
		but.renderNoTransform(s);
		//renderSprite("green");
		
		packet = buttonfade.animations.get("settings").calc();
		calcMatrix(packet);
		setShader(s, translation, matrix);
		GuiButton set = (GuiButton) components.get(1);
		set.renderNoTransform(s);
		//renderSprite("green");
		
		packet = buttonfade.animations.get("exit").calc();
		calcMatrix(packet);
		setShader(s, translation, matrix);
		GuiButton ex = (GuiButton) components.get(2);
		ex.renderNoTransform(s);
		//renderSprite("green");
		
	}

}
