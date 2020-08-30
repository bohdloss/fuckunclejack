package com.bohdloss.fuckunclejack.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.PlayerEntity;
import com.bohdloss.fuckunclejack.editor.Editor;
import com.bohdloss.fuckunclejack.generator.generators.OverworldWorld;
import com.bohdloss.fuckunclejack.hud.HUD;
import com.bohdloss.fuckunclejack.input.InputManager;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.render.Camera;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;
import com.bohdloss.fuckunclejack.server.Server;

import static com.bohdloss.fuckunclejack.logic.ClientState.*;
import static com.bohdloss.fuckunclejack.main.Game.camera;

import javax.swing.JOptionPane;

public class Game{
	
public static Window window;
public static InputManager input=new InputManager();
public static Camera camera;
public static HUD hud;

public static float scaleAmount=60*((float)Toolkit.getDefaultToolkit().getScreenResolution()/120f);
public static float guiScale=scaleAmount-10;

private int frames=0;
private String fps="0 FPS";
private long lastTime=0;

public static boolean pendingToggle=false;
public static BufferedImage createTexture=null;
public static String createTextureName=null;
public static String createModel;
public static String createModelName;

public static Matrix4f scale;
public static Matrix4f target;
public static Matrix4f guiscale2;
public static Matrix4f guitarget;
public static Matrix4f tempres;
public static Shader shader;

	public void begin() {
		setup();

		lPlayer=new PlayerEntity(Main.name);
		lWorld=new OverworldWorld("world");
		ClientState.connect(Main.ip, Server.port);
		
		hud=new HUD();
		
		input.start();
		
		renderLoop();
	}
	
	public void setup() {
		//Essential setup
		System.out.println("Hacking into the mainframe...");
		
		//LibraryUtils.prepareLWJGL();
		
		System.out.println("Initializing GLFW...");
				if(!glfwInit()) {
					JOptionPane.showMessageDialog(null, "LWJGL failed to initialize!\nReport this error!");
					System.exit(1);
				}
				//End
				
				System.out.println("Initializing window...");
				
				window=new Window();
				int w = window.getWidth();
				int h = window.getHeight();
				
				System.out.println("Initializing camera...");
				
				camera = new Camera(w,h);
				
				System.out.println("Creating context...");
				
				GL.createCapabilities();
				
				
				//Enable transparent textures
				//Disable depth test
				
				System.out.println("Setting OpenGL settings...");
				
				glEnable(GL_TEXTURE_2D);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glDisable(GL_CULL_FACE);
				glDisable(GL_DEPTH_TEST);
				glDisable(GL_LIGHTING);
				
				System.out.println("Loading game assets...");
				
				Assets.load();
				
				System.out.println("Done!");
	}
	
	public void renderLoop() {
		
		scale = new Matrix4f().scale(scaleAmount);
		target = new Matrix4f();
		
		guiscale2 = new Matrix4f().scale(guiScale);
		guitarget = new Matrix4f();
		
		tempres = new Matrix4f();
		
		shader = Assets.shaders.get("shader");
		
		System.out.println("Rendering started");
		
		while(!window.shouldClose()) {
			scale.identity().scale(scaleAmount);
			guiscale2.identity().scale(guiScale);
			if(pendingToggle) {
				pendingToggle=false;
				window.toggleFullscreen();
			}
			if(createTexture!=null&createTextureName!=null) {
				Assets.textures.put(createTextureName, new Texture(createTexture));
				createTexture=null;
				createTextureName=null;
			}
			if(createModel!=null&createModelName!=null) {
				try {
				Assets.models.put(createModelName, ModelLoader.loadString(createModel));
				} catch(Exception e) {
					e.printStackTrace();
				}
				createModel=null;
				createModelName=null;
			}
			
			if(window.isDestroyed()) continue;
			
			glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			switch(state) {
			case GAME:
				renderGame();
				renderHud();
			break;
			case EDITMODE:
				renderGame();
				renderEditor();
			break;
			}
			
			FontManager.renderString(-FontManager.strWidth(fps)/2, 7, Assets.sheets.get("font"), Assets.shaders.get("gui"), tempres, Assets.models.get("item"), fps);
			
			window.swap();
			
			if(System.currentTimeMillis()>=lastTime+1000) {
				fps=frames+" FPS";
				frames=0;
				lastTime=System.currentTimeMillis();
			}
			
			frames++;
		}
		glfwTerminate();
		System.out.println("shutting down");
		System.exit(0);
	}
	
	public void renderGame() {
		if(lWorld!=null&lPlayer!=null) {
			target=scale;
			shader.bind();
			lWorld.render(shader, camera.projection().mul(target, tempres));
		}
	}
	
	public void renderEditor() {
		Editor.render(shader, camera.unTransformedProjection().mul(guitarget, tempres));
	}
	
	public void renderHud() {
		guitarget=guiscale2;
		hud.render(shader, camera.unTransformedProjection().mul(guitarget, tempres));
	}
	
	public void fullscreen() {
	}
	
}
