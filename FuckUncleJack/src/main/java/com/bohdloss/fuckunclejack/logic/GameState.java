package com.bohdloss.fuckunclejack.logic;

import java.util.HashMap;

import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.PlayerEntity;

public class GameState {

public static final long tickDelay=25;
public static final long replicationDelay=25;
	
public static LockedBoolean isClient=new LockedBoolean();
	
public static HashMap<Integer, PlayerEntity> players = new HashMap<Integer, PlayerEntity>();
public static HashMap<String, World> dimensions = new HashMap<String, World>();

}
