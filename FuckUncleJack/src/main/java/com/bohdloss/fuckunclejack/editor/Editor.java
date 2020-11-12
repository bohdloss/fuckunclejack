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

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.entities.PropEntity;
import com.bohdloss.fuckunclejack.hud.Button;
import com.bohdloss.fuckunclejack.hud.HUD;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.FunctionUtils;
import com.bohdloss.fuckunclejack.logic.structs.CustomEntityData;
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
public static List<CustomEntityData> customs = new ArrayList<CustomEntityData>();

public static List<Integer> normalids = new ArrayList<Integer>();
public static List<Float> normx = new ArrayList<Float>();
public static List<Float> normy = new ArrayList<Float>();

public static CustomEntityData custEnt;
public static int curID;
public static Entity curEnt;
public static boolean savedPhysics;

public static float width=1, height=1;
public static float offsetx, offsety;

public static int currentNormIndex=0;
public static int currentIndex=0;

public static float savedx=0;
public static float savedy=0;

public static final int OFFSET=-3;
public static final int BOUNDS=-2;
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
				custEnt = new CustomEntityData();
				customs.add(custEnt);
				custEnt.collision=JOptionPane.showInputDialog("true/false has collision").equals("true");
				custEnt.physics=JOptionPane.showInputDialog("true/false has physics").equals("true");
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
					Texture txt = Texture.generate(img);
					Assets.textures.put(name, txt);
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				
			}
			return 0;
		}
	});
	new Button("bounds",-12,4).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&status==READY) {
				status=BOUNDS;
			}
			return 0;
		}
	});
	new Button("offset",-9,4).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&status==READY) {
				status=OFFSET;
			}
			return 0;
		}
	});
	new Button("finish bounds",-6,4).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&(status==OFFSET||status==BOUNDS)) {
				status=READY;
			}
			return 0;
		}
	});
	new Button("reset",-3,4).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE) {
				
				if(JOptionPane.showConfirmDialog(null, "Are you sure?")!=0) return 0;
				
				customs.clear();
				normalids.clear();
				normx.clear();
				normy.clear();
				vertical=false;
				i=0;
				scroll=0;
				usedtxt=null;
				xscale=1;
				yscale=1;
				custEnt=null;
				curID=0;
				curEnt=null;
				savedPhysics=false;
				width=1;
				height=1;
				offsetx=0;
				offsety=0;
				currentNormIndex=0;
				currentIndex=0;
				savedx=0;
				savedy=0;
				
				ClientState.toggleEditor();
				
				status=READY;
			}
			return 0;
		}
	});
	new Button("finish model",-3,6).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&status==MODEL) {
				
				custEnt.xscale=xscale;
				custEnt.yscale=yscale;
				
				status=COLLISION;
			}
			return 0;
		}
	});
	new Button("finish collision",0,6).setAction(new Callable<Integer>() {
		public Integer call() {
			if(state==EDITMODE&status==COLLISION) {
				
				custEnt.width=xscale;
				custEnt.height=yscale;
				
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
					custEnt.texture=name;
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
				
				custEnt.x=savedx;
				custEnt.y=savedy;
				
				float xscale=custEnt.xscale;
				float yscale=custEnt.yscale;
				
				String txt=custEnt.texture;
				float sw = custEnt.width;
				float sh = custEnt.height;
				
				float sx = custEnt.x;
				float sy = custEnt.y;
				
				PropEntity p = new PropEntity(xscale, yscale, txt, sw, sh, custEnt.collision, custEnt.physics);
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
				
				File mapData = new File(folder.getPath()+"/"+chooser.getSelectedFile().getName()+".json");
				
				JSONObject data = new JSONObject();
				JSONObject info = new JSONObject();
				data.put("info", info);
				info.put("offsetx", offsetx+"f");
				info.put("offsety", offsety+"f");
				info.put("width", width+"f");
				info.put("height", height+"f");
				
				JSONArray customEnts = new JSONArray();
				data.put("custom", customEnts);
				for(int i=0;i<customs.size();i++) {
					JSONObject obj = customs.get(i).getData(offsetx, offsety);
					customEnts.add(obj);
				}
				
				JSONArray normalEnts = new JSONArray();
				data.put("entities", normalEnts);
				
				for(int i=0;i<normalids.size();i++) {
					JSONObject ent = new JSONObject();
					normalEnts.add(ent);
					ent.put("x", (normx.get(i)-offsetx)+"f");
					ent.put("y", (normy.get(i)-offsety)+"f");
					ent.put("id", normalids.get(i)+"");
				}
				
				JSONArray blocks = new JSONArray();
				data.put("blocks", blocks);
				
				
				//Calculate array of blocks inside of dungeon area
				//using collision detection algorithm used for entities
				
				int pseudoW = ((int)width+1);
				int pseudoH = ((int)height+1);
				
				Block[] blocksArray = new Block[pseudoW*pseudoH];
				Block[] blocksArrayBG = new Block[pseudoW*pseudoH];
				int bx = CMath.fastFloor(offsetx);
				int by = CMath.fastFloor(offsety);
				int ii=0;
				
				int subW = (int)((float)pseudoW/2f);
				int subH = (int)((float)pseudoH/2f);
				
				for(int i=-subW;i<pseudoW-subW;i++) {
					for(int j=-subH;j<pseudoH-subH;j++) {
						try {
						blocksArray[ii]=ClientState.lWorld.getBlock(bx+i, by+j, true);
						blocksArrayBG[ii]=ClientState.lWorld.getBlock(bx+i, by+j, false);
						} catch(Exception e) {}
						ii++;
					}
				}
				
				for(int i=0;i<blocksArray.length;i++) {
					
					Block current = blocksArray[i];
					Block currentBG = blocksArrayBG[i];
					
					boolean curVal=(current==null||current.getId()==0);
					boolean curBGVal=(currentBG==null||currentBG.getId()==0);
					
					if(curVal&&curBGVal) continue;
					
					JSONObject bobj = new JSONObject();
					blocks.add(bobj);
					
					float blockx = ((float)current.getWorldx())-offsetx;
					float blocky = ((float)current.getY())-offsety;
					
					if(!curVal) {
						bobj.put("x", ""+blockx);
						bobj.put("y", ""+blocky);
						bobj.put("id", ""+current.getId());
						bobj.put("background", false);
					}
					
					if(!curBGVal) {
						bobj.put("x", ""+blockx);
						bobj.put("y", ""+blocky);
						bobj.put("id", ""+currentBG.getId());
						bobj.put("background", true);
					}
					
				}
				
				//done
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(mapData));
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
				translation.identity().translate(savedx, savedy, 0).scale(custEnt.xscale, custEnt.yscale, 1);
			}
			res=res.mul(translation, res);
			s.setProjection(res);
			square.render();
		}
		
		Game.camera.projection().mul(Game.scale, res);
		
		if(status>=COLLISION) {
			green.bind(0);
			translation.identity().translate(savedx, savedy, 0).scale(xscale, yscale, 1);
			res=res.mul(translation, res);
			s.setProjection(res);
			square.render();
		}
		
		if(status==BOUNDS||status==OFFSET) {
			green.bind(0);
			translation.identity().translate(offsetx, offsety, 0).scale(width, height, 1);
			res=res.mul(translation, res);
			s.setProjection(res);
			square.render();
		}
		
		//GUI
		
		Button.buttons.forEach(v->{
			if(v.status!=Button.DISABLED) {
				if(v.bounds.pIntersects(Game.guiMouse)) {
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
			s.setProjection(res);
			
			v.bind(0);
			square.render();
			
			FontManager.renderString(-11, 4-i+scroll, font, gui, matrix, square, k);
			
			s.bind();
			
			i++;
		});
	}
	
}
