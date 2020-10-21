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

public class ItemDropEntity extends Entity {

protected Item item;

//GC
private Matrix4f res=new Matrix4f();
private Object[] data=new Object[2];
private Model itemmodel;
//END

	public ItemDropEntity(Item item) {
		super("item", "", 1f);
		this.item=item;
		itemmodel=Assets.models.get("item");
		width=0.8f;
		height=0.8f;
		invulnerable=true;
		updateBounds();
	}

	@Override
	public void render(Shader s, Matrix4f input) {
		s.setUniform("red", red);
		
		res = input.translate(x, y, 0, res);
		s.setProjection(res);
		BlockTexture t = Assets.blocks.get(item.getTexture());
		if(t==null) return;
		t.render(itemmodel, s, res);
		
		s.setUniform("red", false);
		
		renderHitboxes(s, input);
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
