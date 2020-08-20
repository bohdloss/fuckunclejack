package com.bohdloss.fuckunclejack.components.entities;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.logic.events.ItemPickupEvent;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.BlockTexture;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public class ItemDrop extends Entity {

protected Item item;

//GC
private Matrix4f res=new Matrix4f();
private Object[] data=new Object[2];
private Model itemmodel;
//END

	public ItemDrop(Item item) {
		super("item", "", 1f);
		this.item=item;
		itemmodel=Assets.models.get("item");
		width=0.8f;
		height=0.8f;
		updateBounds();
	}

	@Override
	public void render(Shader s, Matrix4f input) {
		res = input.translate(x, y, 0, res);
		s.setUniform("projection", res);
		BlockTexture t = Assets.blocks.get(item.getTexture());
		if(t==null) return;
		t.txt[19].bind(0);
		itemmodel.render();
	}
	
	@Override
	protected Block[] getSurroundings() {
		Block[] blocks = new Block[35];
		int bx = CMath.fastFloor(x);
		int by = CMath.fastFloor(y);
		int ii=0;
		
		for(int i=0;i<5;i++) {
			for(int j=0;j<7;j++) {
				try {
				blocks[ii]=world.getBlock(bx+i-2, by+j-3);
				} catch(Exception e) {}
				ii++;
			}
		}
		return blocks;
	}

	public Item getItem() {
		return item;
	}
	
	public void pickUp(Entity entity, boolean send) {
		
		//NOTICE: Call to event handler: ITEM PICKUP
		
		if(!EventHandler.itemPicked(send, new ItemPickupEvent(GameEvent.groundPick, entity, this)).isCancelled()) {
			
				entity.getInventory().addItem(item, false);
				
				destroy();
		}
		
		//END
	}

	@Override
	public int getId() {
		return 1;
	}
	
	public Object[] getData() {
		data[0]=(int)item.getId();
		data[1]=(int)item.getAmount();
		return data;
	}
	
}
