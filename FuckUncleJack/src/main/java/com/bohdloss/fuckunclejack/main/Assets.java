package com.bohdloss.fuckunclejack.main;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.bohdloss.fuckunclejack.render.Animation;
import com.bohdloss.fuckunclejack.render.AnimationSet;
import com.bohdloss.fuckunclejack.render.BlockTexture;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;
import com.bohdloss.fuckunclejack.render.TileSheet;

public class Assets {

	public static HashMap<String, Shader> shaders = new HashMap<String, Shader>();
	public static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	public static HashMap<String, BlockTexture> blocks = new HashMap<String, BlockTexture>();
	public static HashMap<String, Model> models = new HashMap<String, Model>();
	public static HashMap<String, TileSheet> sheets = new HashMap<String, TileSheet>();
	public static HashMap<String, AnimationSet> animationSets = new HashMap<String, AnimationSet>();
	public static HashMap<String, Animation> animations = new HashMap<String, Animation>();
	
	public static void load() {
		
		//Load shaders
		
		try {
			loadShaders();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Crashed(While loading shaders)! :D");
			System.exit(1);
		}
		
		//Load textures
		
		try {
			loadTextures();
			loadTileSheets();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Crashed(While loading textures)! :D");
			System.exit(1);
		}
		
		try {
			loadAnimations();
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Crashed(While loading animations)! :D");
			System.exit(1);
		}
		
		//Load models
		
		try {
			loadModels();
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Crashed(While loading models)! :D");
			System.exit(1);
		}
		
		System.out.println("Assets loaded");
	}
	
	private static void loadModels() throws Exception{
		System.out.println("Loading models...");
		
		models.put("square", ModelLoader.load("/data/models/square.json"));
		models.put("player_model", ModelLoader.load("/data/models/rectangle.json"));
		models.put("hud_bar", ModelLoader.load("/data/models/hud_bar.json"));
		models.put("bar_middle", ModelLoader.load("/data/models/bar_middle.json"));
		models.put("inventory_bg", ModelLoader.load("/data/models/inventory_bg.json"));
		models.put("inventory_slot", ModelLoader.load("/data/models/inventory_slot.json"));
		models.put("item", ModelLoader.load("/data/models/item.json"));
		models.put("smallitem", ModelLoader.load("/data/models/smallitem.json"));
		models.put("hotbar", ModelLoader.load("/data/models/hotbar.json"));
		models.put("smallrect", ModelLoader.load("/data/models/smallrect.json"));
		models.put("rect", ModelLoader.load("/data/models/rect.json"));
		models.put("bigrect", ModelLoader.load("/data/models/bigrect.json"));
		models.put("house", ModelLoader.load("/data/models/house.json"));
		models.put("deserthouse", ModelLoader.load("/data/models/deserthouse.json"));
		models.put("interaction", ModelLoader.load("/data/models/interaction.json"));
		models.put("table", ModelLoader.load("/data/models/table.json"));
	}
	
	private static void loadShaders() throws Exception{
		System.out.println("Loading shaders...");
		
		shaders.put("shader", new Shader("/data/shaders/shader.vs", "/data/shaders/fragment.fs"));
		shaders.put("gui", new Shader("/data/shaders/gui.vs", "/data/shaders/fragment.fs"));
		shaders.put("bar", new Shader("/data/shaders/bar.vs", "/data/shaders/barfrag.fs"));
	}
	
	private static void loadTextures() throws Exception{
		System.out.println("Loading textures...");
		
		textures.put("empty", new Texture("/data/textures/empty.png"));
		
		//Hud textures
		
		textures.put("hud_bg", new Texture("/data/textures/hud/bg.png"));
		textures.put("hud_buttons", new Texture("/data/textures/hud/buttons.png"));
		textures.put("icon_health", new Texture("/data/textures/hud/health.png"));
		textures.put("bar_bg", new Texture("/data/textures/hud/bar_bg.png"));
		textures.put("bar_health", new Texture("/data/textures/hud/bar_health.png"));
		textures.put("bar_armour", new Texture("/data/textures/hud/bar_armour.png"));
		textures.put("green", new Texture("/data/textures/hud/green.png"));
		textures.put("greenarrow", new Texture("/data/textures/hud/greenarrow.png"));
		textures.put("inventory_bg", new Texture("/data/textures/hud/inventory_bg.png"));
		textures.put("slot_bg", new Texture("/data/textures/hud/slot_bg.png"));
		textures.put("slot_bg_hovered", new Texture("/data/textures/hud/slot_bg_hovered.png"));
		textures.put("hotbar_bg", new Texture("/data/textures/hud/hotbar_bg.png"));
		textures.put("smallrect", new Texture("/data/textures/hud/smallrect.png"));
		textures.put("interaction", new Texture("/data/textures/hud/interaction.png"));
		
		//World backgrounds
		
		textures.put("overworld_world_sky", new Texture("/data/textures/worlds/overworld.png"));
		textures.put("deserthouse_world_sky", new Texture("/data/textures/worlds/deserthouseworld.png"));
		
		//Generic environment textures
		
		textures.put("hovered_block", new Texture("/data/textures/blocks/assets/hovered.png"));
		textures.put("background_block", new Texture("/data/textures/blocks/assets/background.png"));
		textures.put("dot", new Texture("/data/textures/hud/dot.png"));
		
		//Props entities textures
		
		textures.put("table0", new Texture("/data/textures/entities/props/table0.png"));
		textures.put("table1", new Texture("/data/textures/entities/props/table1.png"));
		
		//Item textures
		
		textures.put("winnersworditem", new Texture("/data/textures/items/weapons/winnersword.png"));
		textures.put("arrowitem", new Texture("/data/textures/items/ammo/arrow.png"));
		textures.put("bowitem", new Texture("/data/textures/items/weapons/bow.png"));
		
		//Projectiles
		textures.put("arrow", new Texture("/data/textures/entities/projectiles/arrow.png"));
		
		//Menus
		textures.put("mainbackground", new Texture("/data/textures/menus/background.png"));
		textures.put("mainleft", new Texture("/data/textures/menus/left.png"));
		textures.put("mainright", new Texture("/data/textures/menus/right.png"));
		textures.put("maintitle", new Texture("/data/textures/menus/title.png"));
		
		loadBlocks();
	}
	
	private static void loadTileSheets() throws Exception{
		System.out.println("Loading tile sheets...");
		
		sheets.put("buttons", new TileSheet(textures.get("hud_buttons"), 4));
		sheets.put("font", FontManager.load(new Font("Arial", 1, 32), new Color(127,127,127,255)));
	
		sheets.put("menubuttons", new TileSheet(new Texture("/data/textures/menus/buttons/buttonsheet.png"), 27));
	}
	
	private static void loadAnimations() throws Exception{
		System.out.println("Loading animations...");
		
		AnimationSet dad_default = new AnimationSet("dad");
		dad_default.longIdle=new Animation("/data/textures/entities/player/dad/longidle.png", 23, true, null);
		dad_default.idle=new Animation("/data/textures/entities/player/dad/idle.png", 1, false, "dad_default_idle");
		dad_default.walking=new Animation("/data/textures/entities/player/dad/walking.png", 4, true, "dad_default_walking");
		dad_default.jumping=new Animation("/data/textures/entities/player/dad/jump.png", 1, false, "dad_default_jump");
		dad_default.falling=new Animation("/data/textures/entities/player/dad/fall.png", 1, false, "dad_default_fall");
		dad_default.damage=new Animation("/data/textures/entities/player/dad/damage.png", 1, false, "dad_default_damage");
		
		animationSets.put("dad", dad_default);
		
		animations.put("deserthouse", new Animation(new TileSheet(new Texture("/data/textures/entities/structures/deserthouse.png"), 29), true, null));
	
	}
	
	private static void loadBlocks() throws Exception{
		System.out.println("Loading block textures...");
		
		blocks.put("grass", new BlockTexture("/data/textures/blocks/grass_block_side.png"));
		blocks.put("dirt", new BlockTexture("/data/textures/blocks/dirt.png"));
		blocks.put("stone", new BlockTexture("/data/textures/blocks/stone.png"));
		blocks.put("bedrock", new BlockTexture("/data/textures/blocks/bedrock.png"));
		blocks.put("wood", new BlockTexture("/data/textures/blocks/oak_log.png"));
		blocks.put("sand", new BlockTexture("/data/textures/blocks/sand.png"));
		blocks.put("sandstone", new BlockTexture("/data/textures/blocks/sandstone.png"));
		blocks.put("cactus", new BlockTexture("/data/textures/blocks/cactus.png"));
		blocks.put("leaves", new BlockTexture("/data/textures/blocks/leaves.png"));
	}
	
}
