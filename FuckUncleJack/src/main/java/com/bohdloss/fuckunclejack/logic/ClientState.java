package com.bohdloss.fuckunclejack.logic;

import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.House;
import com.bohdloss.fuckunclejack.components.entities.Player;
import com.bohdloss.fuckunclejack.editor.Editor;

public class ClientState {

public static final int MENU=0;
public static final int LOBBY=1;
public static final int GAME=2;
	
public static final int EDITMODE=3;

//editmode
public static Editor editor;
//

public static World lWorld;
public static Player lPlayer;

public static final double drawDistance=20*20;

public static House nearHouse;
public static boolean nearHouseB=false;
public static House lastHouse=null;

public static int hovx;
public static int hovy;

public static int state=MENU;

public static String IP;
public static int PORT;
public static boolean locked;
public static boolean hardLocked;
public static boolean renderPlayer=true;

public static void hover(int x, int y) {
	hovx=x;
	hovy=y;
}

public static int sel() {
	return lPlayer.getInventory().selected;
}

public static void connect(String ip, int port) {
	IP=ip;
	PORT=port;
	state=GAME;
}

public static void disconnect() {
	IP=null;
	PORT=0;
	state=MENU;
}

}
