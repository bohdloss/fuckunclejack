package com.bohdloss.fuckunclejack.logic;

import com.bohdloss.fuckunclejack.client.Client;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.HouseEntity;
import com.bohdloss.fuckunclejack.components.entities.PlayerEntity;
import com.bohdloss.fuckunclejack.editor.Editor;
import com.bohdloss.fuckunclejack.generator.generators.OverworldWorld;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.menutabs.MenuTab;
import com.bohdloss.fuckunclejack.render.CMath;

public class ClientState {

public static final int MENU=0;
public static final int LOBBY=1;
public static final int GAME=2;
	
public static final int EDITMODE=3;

//editmode
public static Editor editor;
//

public static World lWorld;
public static PlayerEntity lPlayer;

public static final double xdrawDistance=20;
public static final double ydrawDistance=10;

public static HouseEntity nearHouse;
public static boolean nearHouseB=false;
public static HouseEntity lastHouse=null;

public static int hovx;
public static int hovy;

public static int state=MENU;

public static boolean queueDisconnect=false;
public static String IP;
public static int PORT;
public static boolean locked;
public static boolean hardLocked;
public static boolean renderPlayer=true;
public static boolean debug=false;

private static final long fadeAnim=500l;


public static void hover(int x, int y) {
	hovx=x;
	hovy=y;
}

public static void fadeToBlack() {
	long start = System.currentTimeMillis();
	while(System.currentTimeMillis()<start+fadeAnim) {
		long delta = System.currentTimeMillis()-start;
		float percent = (float)delta/(float)fadeAnim;
		Game.fadeVal=(float)CMath.lerp(percent, 0, 1);
	}
}

public static void fadeFromBlack() {
	long start = System.currentTimeMillis();
	while(System.currentTimeMillis()<start+fadeAnim) {
		long delta = System.currentTimeMillis()-start;
		float percent = (float)delta/(float)fadeAnim;
		Game.fadeVal=(float)CMath.lerp(percent, 1, 0);
	}
}

public static int sel() {
	return lPlayer.getInventory().selected;
}

public static void connect(String ip, int port) {
	fadeToBlack();
	lWorld=new OverworldWorld("world");
	lPlayer=new PlayerEntity("bohdloss");
	IP=ip;
	PORT=port;
	state=GAME;
	Client.disconnected=false;
	fadeFromBlack();
}

public static void disconnect() {
	IP=null;
	PORT=0;
	showMenu(true, true, "main");
	lWorld=null;
	lPlayer=null;
	System.gc();
}

public static void showMenu(boolean start, boolean end, String string) {
	MenuTab.bindTab(start, end, string);
}

}
