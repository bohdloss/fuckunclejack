package com.bohdloss.fuckunclejack.logic;

import java.util.HashMap;

import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.Player;

public class GameState {

public static final long tickDelay=25;
public static final long replicationDelay=25;
	
public static LockedBoolean isClient=new LockedBoolean();
	
public static HashMap<Integer, Player> players = new HashMap<Integer, Player>();
public static HashMap<String, World> dimensions = new HashMap<String, World>();

}
