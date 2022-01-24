package tiles;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Metal extends Tile{

	static BufferedImage texture;
	static {
		try { //NEED TO SPLIT "metal_tiles.svg" into mulitple images
			texture = ImageIO.read(new File("sprites/metalplate1.png")); 
		}
		catch(java.io.IOException e) { 
			e.printStackTrace();
		}
	}
	
	public Metal(int x, int y, JPanel pane) {
		super(x, y, texture, pane, false);
	}
	
	@Override
	public String toString() {
		return "Metal";
	}
}
