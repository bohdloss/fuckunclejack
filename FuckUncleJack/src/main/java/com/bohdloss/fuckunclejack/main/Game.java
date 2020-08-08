package com.bohdloss.fuckunclejack.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Toolkit;
import java.util.Random;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.Player;
import com.bohdloss.fuckunclejack.generator.generators.OverworldWorld;
import com.bohdloss.fuckunclejack.hud.HUD;
import com.bohdloss.fuckunclejack.input.InputManager;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.render.Camera;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.server.Server;

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

public static boolean pendingToggle=false;

	public void begin() {
		setup();

		lPlayer=new Player(Main.name);
		lWorld=new OverworldWorld("world");
		ClientState.connect(Main.ip, Server.port);
		
		hud=new HUD();
		
		input.start();
		
		renderLoop();
	}
	
	public void setup() {
		//Essential setup
				if(!glfwInit()) {
					JOptionPane.showMessageDialog(null, "LWJGL failed to initialize!\nReport this error!");
					System.exit(1);
				}
				//End
				
				window=new Window();
				int w = window.getWidth();
				int h = window.getHeight();
				camera = new Camera(w,h);
				GL.createCapabilities();
				glEnable(GL_TEXTURE_2D);
				
				//Enable transparent textures
				//Disable depth test
				
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glDisable(GL_CULL_FACE);
				glDisable(GL_DEPTH_TEST);
				glDisable(GL_LIGHTING);
				
				Assets.load();
	}
	
	public void renderLoop() {
		
		Matrix4f scale = new Matrix4f().scale(scaleAmount);
		Matrix4f target = new Matrix4f();
		
		Matrix4f guiscale = new Matrix4f().scale(guiScale);
		Matrix4f guitarget = new Matrix4f();
		
		Matrix4f tempres = new Matrix4f();
		
		Shader shader = Assets.shaders.get("shader");
		
		while(!window.shouldClose()) {
			scale.identity().scale(scaleAmount);
			guiscale.identity().scale(guiScale);
			if(pendingToggle) {
				pendingToggle=false;
				window.toggleFullscreen();
			}
			if(window.isDestroyed()) continue;
			
			glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			if(lWorld!=null&lPlayer!=null) {
			
			target=scale;
			properRender(shader, camera.projection().mul(target, tempres));
			guitarget=guiscale;
			hud.render(shader, camera.unTransformedProjection().mul(guitarget, tempres));
			
			}
			
			FontManager.renderString(-FontManager.strWidth(fps)/2, 7, Assets.sheets.get("font"), Assets.shaders.get("gui"), tempres, Assets.models.get("item"), fps);
			
			window.swap();
			
			if(System.currentTimeMillis()>=lastTime+1000) {
				fps=frames+" FPS";
				frames=0;
				lastTime=System.currentTimeMillis();
			}
			
			//fps=""+scaleAmount;
			
			frames++;
		}
		glfwTerminate();
		System.out.println("shutting down");
		System.exit(0);
	}
	
	public void properRender(Shader shader, Matrix4f target) {
		shader.bind();
		ClientState.lWorld.render(shader, target);
	}
	
	public void fullscreen() {
	}
	
}
