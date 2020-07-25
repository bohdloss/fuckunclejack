package com.bohdloss.fuckunclejack.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import static com.bohdloss.fuckunclejack.render.CMath.*;

public class BlockTexture {

public Texture[] txt = new Texture[20];
	
public BlockTexture(String path) throws Exception{
	BufferedImage img = ImageIO.read(Texture.class.getResourceAsStream(path));
	for(int i=0;i<txt.length;i++) {
		BufferedImage buf = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = buf.getGraphics();
		g.drawImage(img, 0, 0, null);
		Color fill = new Color(0,0,0,(int)lerp((double)i/20f, 255, 0));
		g.setColor(fill);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		txt[i] = new Texture(buf);
	}
}

}
