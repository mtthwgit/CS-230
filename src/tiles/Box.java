package tiles;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entities.Player;

public class Box extends Tile {
	
	static BufferedImage boxTexture;
	static {
		try { 
			boxTexture = ImageIO.read(new File("src/sprites/Box.png")); 
		}
		catch(java.io.IOException e) { 
			e.printStackTrace();
		}
	}
	
	//THERE IS NO NULL CHECK, WE MUST DETERMINE BEFORE HAND WHERE THE IMAGE IS
	public Box(int x, int y, JPanel jpane) {
		super(x, y, boxTexture, jpane, true);
	}
	
	public String toString() {
		return "Box";
	}
}
