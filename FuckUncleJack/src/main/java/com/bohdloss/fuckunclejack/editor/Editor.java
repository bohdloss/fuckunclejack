package com.bohdloss.fuckunclejack.editor;

import static com.bohdloss.fuckunclejack.logic.ClientState.EDITMODE;
import static com.bohdloss.fuckunclejack.logic.ClientState.state;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.joml.Matrix4f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.entities.Prop;
import com.bohdloss.fuckunclejack.hud.Button;
import com.bohdloss.fuckunclejack.hud.HUD;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.FunctionUtils;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;
import com.bohdloss.fuckunclejack.render.TileSheet;

public class Editor {

private static Model square;
private static Shader gui;
private static TileSheet font;
private static Texture green;
private static int i;
private static Matrix4f res=new Matrix4f();
private static Matrix4f translation=new Matrix4f();
public static float scroll=0f;
public static Texture usedtxt;
public static float xscale=1;
public static float yscale=1;
public static boolean vertical=false;
public static List<BufferedImage> txts = new ArrayList<BufferedImage>();
public static List<String> txtname = new ArrayList<String>();
public static List<String> savedModel = new ArrayList<String>();
public static List<String> jsonModel = new ArrayList<String>();
public static List<Float> savedScalex = new ArrayList<Float>();
public static List<Float> savedScaley = new ArrayList<Float>();
public static List<Float> widths = new ArrayList<Float>();
public static List<Float> heights = new ArrayList<Float>();
public static List<Float> xs = new ArrayList<Float>();
public static List<Float> ys = new ArrayList<Float>();
public static List<Boolean> collision = new ArrayList<Boolean>();
public static List<Boolean> physics = new ArrayList<Boolean>();

public static List<Integer> normalids = new ArrayList<Integer>();
public static List<Float> normx = new ArrayList<Float>();
public static List<Float> normy = new ArrayList<Float>();

public static int curID;
public static Entity curEnt;
public static boolean savedPhysics;

public static int currentNormIndex=0;
public static int currentIndex=0;

public static float savedx=0;
public static float savedy=0;

public static final int NORMAL=-1;
public static final int READY=0;
public static final int CHOOSE=1;
public static final int MODEL=2;
public static final int COLLISION=3;
public static final int TRANSLATE=4;



public static int status=READY;

static {
	square = Assets.models.get("square");
	gui = Assets.shaders.get("gui");
	font = Assets.sheets.get("font");
	green= Assets.textures.get("green");
	new Button("new entity",-9,6).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&status==READY) {
				collision.add(JOptionPane.showInputDialog("true/false has collision").equals("true"));
				physics.add(JOptionPane.showInputDialog("true/false has physics").equals("true"));
				Editor.status=Editor.CHOOSE;
			}
			return 0;
		}
	});
	new Button("new texture",-12,6).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE) {
				JFileChooser c = new JFileChooser();
				c.showOpenDialog(null);
				
				try {
					BufferedImage img = ImageIO.read(c.getSelectedFile());
					String name = JOptionPane.showInputDialog("texture name: ");
					Game.createTexture=img;
					Game.createTextureName=name;
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				
			}
			return 0;
		}
	});
	new Button("finish model",-3,6).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&status==MODEL) {
				JSONObject save = new JSONObject();
				
				JSONArray vertices = new JSONArray();
				JSONArray tex_coords = new JSONArray();
				JSONArray indices = new JSONArray();
				
				save.put("vertices", vertices);
				save.put("tex_coords", tex_coords);
				save.put("indices", indices);
				
				vertices.add((-0.5f*xscale)+"f");
				vertices.add((0.5f*yscale)+"f");
				vertices.add("0f");
				
				vertices.add((0.5f*xscale)+"f");
				vertices.add((0.5f*yscale)+"f");
				vertices.add("0f");
				
				vertices.add((0.5f*xscale)+"f");
				vertices.add((-0.5f*yscale)+"f");
				vertices.add("0f");
				
				vertices.add((-0.5f*xscale)+"f");
				vertices.add((-0.5f*yscale)+"f");
				vertices.add("0f");
				
				tex_coords.add("0f");
				tex_coords.add("0f");
				
				tex_coords.add("1f");
				tex_coords.add("0f");
				
				tex_coords.add("1f");
				tex_coords.add("1f");
				
				tex_coords.add("0f");
				tex_coords.add("1f");
				
				indices.add("0");
				indices.add("1");
				indices.add("2");
				
				indices.add("2");
				indices.add("3");
				indices.add("0");
				
				String output = save.toJSONString();
				
				String name = JOptionPane.showInputDialog("model name");
				
				jsonModel.add(output);
				savedModel.add(name);
				savedScalex.add(xscale);
				savedScaley.add(yscale);
				Game.createModel=output;
				Game.createModelName=name;
				
				status=COLLISION;
			}
			return 0;
		}
	});
	new Button("finish collision",0,6).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&status==COLLISION) {
				
				widths.add(xscale);
				heights.add(yscale);
				
				status=TRANSLATE;
			}
			return 0;
		}
	});
	new Button("choose texture",-6,6).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&status==CHOOSE) {
				String name = JOptionPane.showInputDialog("choose texture name");
				Texture t = Assets.textures.get(name);
				if(t!=null) {
					usedtxt=t;
					txts.add(usedtxt.getImg());
					txtname.add(name);
					status=MODEL;
				} else {
					JOptionPane.showMessageDialog(null, "invalid name");
				}
			}
			return 0;
		}
	});
	new Button("finish translation",3,6).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE) {
				
				if(status==TRANSLATE) {
				
				xs.add(savedx);
				ys.add(savedy);
				
				String txt=txtname.get(currentIndex);
				String modelname=savedModel.get(currentIndex);
				float sw = widths.get(currentIndex);
				float sh = heights.get(currentIndex);
				
				float sx = xs.get(currentIndex);
				float sy = ys.get(currentIndex);
				
				Prop p = new Prop(modelname, txt, sw, sh, collision.get(currentIndex), physics.get(currentIndex));
				ClientState.lWorld.join(p, sx, sy);
				
				savedx=0;
				savedy=0;
				xscale=1;
				yscale=1;
				
				currentIndex++;
				status=READY;
				
				} else if(status==NORMAL) {
					normx.add(savedx);
					normy.add(savedy);
					normalids.add(curID);
					
					currentNormIndex++;
					
					savedx=0;
					savedy=0;
					xscale=1;
					yscale=1;
					
					curEnt.physics=savedPhysics;
					
					status=READY;
				}
				
			}
			return 0;
		}
	});
	new Button("normal entity",6,6).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&status==READY) {
				try {
					int id=Integer.parseInt(JOptionPane.showInputDialog("entity id"));
					curID=id;
					Entity ent = FunctionUtils.genEntityById(id, new Object[0]);
					if(ent==null) throw new Exception("Entity can't be null");
					Editor.status=Editor.NORMAL;
					savedPhysics=ent.physics;
					ent.physics=false;
					curEnt=ent;
					ClientState.lWorld.join(ent, 0, 2);
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null, "The entity does not exist for this id, or it required additional arguments");
				}
			}
			return 0;
		}
	});
	new Button("save",9,6).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&status==READY) {
				try {
				JFileChooser chooser = new JFileChooser();
				chooser.showSaveDialog(null);
				File folder = chooser.getSelectedFile().getParentFile();
				
				File textures = new File(folder.getPath()+"/textures");
				textures.mkdir();
				
				String txtjavacode="";
				
				for(int i=0;i<txts.size();i++) {
					ImageIO.write(txts.get(i), "png", new File(textures.getPath()+"/"+txtname.get(i)+".png"));
					txtjavacode+="textures.put(\""+txtname.get(i)+"\", new Texture(\"/data/textures/props/"+txtname.get(i)+".png\");\n";
				}
				File codetxt = new File(textures.getPath()+"/java.txt");
				BufferedWriter bw = new BufferedWriter(new FileWriter(codetxt));
				bw.write(txtjavacode);
				bw.flush();
				bw.close();
				
				//models
				
				File models = new File(folder.getPath()+"/models");
				models.mkdir();
				
				String modeljavacode="";
				
				for(int i=0;i<jsonModel.size();i++) {
					BufferedWriter bww = new BufferedWriter(new FileWriter(new File(models.getPath()+"/"+savedModel.get(i)+".json")));
					bww.write(jsonModel.get(i));
					bww.flush();
					bww.close();
					modeljavacode+="models.put(\""+savedModel.get(i)+"\", ModelLoader.load(\"/data/models/"+savedModel.get(i)+".json\");\n";
				}
				codetxt = new File(models.getPath()+"/java.txt");
				bw = new BufferedWriter(new FileWriter(codetxt));
				bw.write(modeljavacode);
				bw.flush();
				bw.close();
				
				File mapData = new File(folder.getPath()+"/"+chooser.getSelectedFile().getName()+".json");
				
				JSONObject data = new JSONObject();
				JSONArray customEnts = new JSONArray();
				data.put("custom", customEnts);
				for(int i=0;i<collision.size();i++) {
					JSONObject obj = new JSONObject();
					customEnts.add(obj);
					obj.put("model", savedModel.get(i));
					obj.put("texture", txtname.get(i));
					obj.put("x", xs.get(i)+"f");
					obj.put("y", ys.get(i)+"f");
					obj.put("width", widths.get(i)+"f");
					obj.put("height", heights.get(i)+"f");
					obj.put("collision", collision.get(i)+"");
					obj.put("physics", physics.get(i)+"");
				}
				
				JSONArray normalEnts = new JSONArray();
				data.put("entities", normalEnts);
				
				for(int i=0;i<normalids.size();i++) {
					JSONObject ent = new JSONObject();
					normalEnts.add(ent);
					ent.put("x", normx.get(i)+"f");
					ent.put("y", normy.get(i)+"f");
					ent.put("id", normalids.get(i)+"");
				}
				
				bw = new BufferedWriter(new FileWriter(mapData));
				bw.write(data.toJSONString());
				bw.flush();
				bw.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			return 0;
		}
	});
}

	public static void render(Shader s, Matrix4f matrix) {
		
		if(status==NORMAL) {
			curEnt.x=savedx;
			curEnt.y=savedy;
		}
		
		//render debug collisions
		
		Game.camera.projection().mul(Game.scale, res);
		
		if(status>=MODEL) {
			usedtxt.bind(0);
			if(status==MODEL) {
				translation.identity().translate(savedx, savedy, 0).scale(xscale, yscale, 1);
			} else {
				translation.identity().translate(savedx, savedy, 0).scale(savedScalex.get(currentIndex), savedScaley.get(currentIndex), 1);
			}
			res=res.mul(translation, res);
			s.setUniform("projection", res);
			square.render();
		}
		
		Game.camera.projection().mul(Game.scale, res);
		
		if(status>=COLLISION) {
			green.bind(0);
			translation.identity().translate(savedx, savedy, 0).scale(xscale, yscale, 1);
			res=res.mul(translation, res);
			s.setUniform("projection", res);
			square.render();
		}
		
		//GUI
		
		HUD.mpoint=CMath.mGLCoord(Game.guiScale);
		
		Button.buttons.forEach(v->{
			if(v.status!=Button.DISABLED) {
				if(v.bounds.pIntersects(HUD.mpoint)) {
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
		
		i=0;
		Assets.textures.forEach((k,v)->{
			translation.identity().translate(-12, 4-i+scroll, 0);
			res=matrix.mul(translation, res);
			s.setUniform("projection", res);
			
			v.bind(0);
			square.render();
			
			FontManager.renderString(-11, 4-i+scroll, font, gui, matrix, square, k);
			
			s.bind();
			
			i++;
		});
	}
	
}
