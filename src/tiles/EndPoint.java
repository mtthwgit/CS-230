package tiles;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entities.Player;

public class EndPoint extends Tile {
	
	static BufferedImage texture;
	static {
		try { 
			texture = ImageIO.read(new File("src/sprites/DoorOpen.png")); 
		}
		catch(java.io.IOException e) { 
			e.printStackTrace();
		}
	}

	public EndPoint(int x, int y, JPanel pane) {
		super(x, y, texture, pane);
		collideable = false;
		hazard = true;
	}
	
	public String toString() {
		return "EndPoint";
	}

	public void hazard(Player p) {
		p.won();
	}
}
