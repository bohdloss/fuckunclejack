package com.bohdloss.fuckunclejack.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Toolkit;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import com.bohdloss.fuckunclejack.api.CharacterInfo;
import com.bohdloss.fuckunclejack.api.GamemodeInfo;
import com.bohdloss.fuckunclejack.components.entities.PlayerEntity;
import com.bohdloss.fuckunclejack.editor.Editor;
import com.bohdloss.fuckunclejack.generator.generators.OverworldWorld;
import com.bohdloss.fuckunclejack.hud.HUD;
import com.bohdloss.fuckunclejack.input.InputManager;
import com.bohdloss.fuckunclejack.menutabs.MenuTab;
import com.bohdloss.fuckunclejack.render.Camera;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.MainEvents;
import com.bohdloss.fuckunclejack.render.Shader;

import static com.bohdloss.fuckunclejack.logic.ClientState.*;

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

public static Matrix4f scale;
public static Matrix4f target;
public static Matrix4f guiscale2;
public static Matrix4f guitarget;
public static Matrix4f tempres;
public static Shader shader;

//This is just a workaround and may be fixed in the future
public static boolean blocked=true;

public static float fadeVal=0f;

	public void begin() {
		setup();
		
		input.start();
		renderLoop();
	}
	
	public void loadLibrary() {
		System.out.println("Initializing GLFW...");
		try {
			if(!glfwInit()) {
				throw new IllegalStateException("Initialization failed");
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "LWJGL failed to initialize!\nReport this error!");
			System.exit(1);
		}
	}
	
	public void setup() {
		//Essential setup
				loadLibrary();
				
				System.out.println("Initializing window...");
				
				window=new Window();
				
				System.out.println("Initializing camera...");
				
				camera = new Camera(window.getWidth(),window.getHeight());
				
				System.out.println("Creating context...");
				
				GL.createCapabilities();
				
				//After creating context because otherwise it crashes on Linux
				Main.name ="";// JOptionPane.showInputDialog("username");
				
				//Enable transparent textures
				//Disable depth test
				
				System.out.println("Changing OpenGL settings...");
				
				glEnable(GL_TEXTURE_2D);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glDisable(GL_CULL_FACE);
				glDisable(GL_DEPTH_TEST);
				glDisable(GL_LIGHTING);
				glfwSwapInterval(0);
				
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
			loopEvents();
		}
		glfwTerminate();
		System.out.println("shutting down");
		System.exit(0);
	}
	
	public void loopEvents() {
		MainEvents.computeEvents();
		
		scale.identity().scale(scaleAmount);
		guiscale2.identity().scale(guiScale);
		
		if(window.isDestroyed()) return;
		
		glfwPollEvents();
		
		//if(blocked) return;
		
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
		case MENU:
			if(!blocked)renderTabs();
		break;
		}
		guitarget=guiscale2;
		shader.bind();
		shader.setProjection(tempres.identity().scale(2));
		
		shader.setUniform("opacity", fadeVal);
		
		Assets.textures.get("empty").bind(0);
		Assets.models.get("square").render();
		
		shader.setUniform("opacity", -1f);
		
		FontManager.renderString(-FontManager.strWidth(fps)/2, 7, Assets.sheets.get("font"), Assets.shaders.get("gui"), camera.unTransformedProjection().mul(guitarget, tempres), Assets.models.get("item"), fps);
		
		window.swap();
		
		if(System.currentTimeMillis()>=lastTime+1000) {
			fps=frames+" FPS";
			frames=0;
			lastTime=System.currentTimeMillis();
		}
		
		frames++;
	}
	
	public void renderGame() {
		target=scale;
		if(lWorld!=null&lPlayer!=null) {
			shader.bind();
			lWorld.render(shader, camera.projection().mul(target, tempres));
		}
	}
	
	public void renderEditor() {
		guitarget=guiscale2;
		Editor.render(shader, camera.unTransformedProjection().mul(guitarget, tempres));
	}
	
	public void renderHud() {
		guitarget=guiscale2;
		hud.render(shader, camera.unTransformedProjection().mul(guitarget, tempres));
	}
	
	public void renderTabs() {
		guitarget=guiscale2;
		shader.bind();
		if(MenuTab.active!=null) {
			MenuTab.active.render(shader, camera.unTransformedProjection().mul(guitarget, tempres));
		}
	}
	
	public void fullscreen() {
	}
	
}
