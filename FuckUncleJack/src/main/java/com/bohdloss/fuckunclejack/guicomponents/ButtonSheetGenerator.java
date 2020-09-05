package com.bohdloss.fuckunclejack.guicomponents;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ButtonSheetGenerator {

	public static void main(String[] args) throws Exception{
		String input = "C:\\Users\\Antonio\\git\\fuckunclejack\\FuckUncleJack\\src\\main\\java\\data\\textures\\menus\\buttons\\button";//JOptionPane.showInputDialog("Image files prefix:");
		File imgi = new File(input+"i.png");
		File imgh = new File(input+"h.png");
		File imgp = new File(input+"p.png");
		
		BufferedImage[] imgs = new BufferedImage[3];
		imgs[0] = ImageIO.read(imgi);
		imgs[1] = ImageIO.read(imgh);
		imgs[2] = ImageIO.read(imgp);
		
		int width=13;//Integer.parseInt(JOptionPane.showInputDialog("width:"));
		int height=13;//Integer.parseInt(JOptionPane.showInputDialog("height:"));
		
		BufferedImage[] res = new BufferedImage[imgs.length];
		
		for(int i=0;i<imgs.length;i++) {
			
			int awidth=imgs[i].getWidth();
			int aheight=imgs[i].getHeight();
			
			int cwidth=imgs[i].getWidth()-width*2;
			int cheight=imgs[i].getHeight()-height*2;
			
			BufferedImage[] parts = new BufferedImage[9];
			
			parts[0] = getPart(imgs[i], 0, 0, width, height);
			parts[1] = getPart(imgs[i], awidth-width, 0, width, height);
			parts[2] = getPart(imgs[i], 0, aheight-height, width, height);
			parts[3] = getPart(imgs[i], awidth-width, aheight-height, width, height);
			
			parts[4] = getPart(imgs[i], width, 0, cwidth, height);
			parts[5] = getPart(imgs[i], width, aheight-height, cwidth, height);
			parts[6] = getPart(imgs[i], 0, height, width, cheight);
			parts[7] = getPart(imgs[i], awidth-width, height, width, cheight);
			
			parts[8] = getPart(imgs[i], width, height, cwidth, cheight);
			
			res[i] = new BufferedImage(imgs[i].getWidth()-width*2, (imgs[i].getHeight()-height*2)*parts.length, BufferedImage.TYPE_INT_ARGB);
			Graphics g = res[i].getGraphics();
			for(int j=0;j<parts.length;j++) {
				g.drawImage(parts[j], 0, j*cheight, res[i].getWidth(), cheight, null);
			}
			
			
		}
		
		BufferedImage result = new BufferedImage(res[0].getWidth(), res[0].getHeight()*3, BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();
		for(int i=0;i<res.length;i++) {
			g.drawImage(res[i], 0, i*res[i].getHeight(), res[i].getWidth(), res[i].getHeight(), null);
		}
		
		File out = new File(input+"sheet.png");
		ImageIO.write(result, "png", out);
		
		//JOptionPane.showMessageDialog(null, "Done!");
		
	}

	private static BufferedImage getPart(BufferedImage in, int startx, int starty, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(in, -startx, -starty, in.getWidth(), in.getHeight(), null);
		
		return img;
	}
	
}
