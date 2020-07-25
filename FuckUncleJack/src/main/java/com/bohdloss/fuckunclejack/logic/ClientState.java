package com.bohdloss.fuckunclejack.logic;

import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.Player;

public class ClientState {

public static final int MENU=0;
public static final int LOBBY=1;
public static final int GAME=2;
	
public static World lWorld;
public static Player lPlayer;

public static final double drawDistance=20*20;

public static Item grabbed;
public static boolean grabbing=false;
public static int grabSlot;

public static int hovx;
public static int hovy;

public static int state=MENU;

public static String IP;
public static int PORT;

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

}
