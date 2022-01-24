package tiles;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entities.Player;
import main.gameController;

public class DownwardSpikes extends Tile {
	
	static BufferedImage downSpikeTexture;
	static {
		try { 
			downSpikeTexture = ImageIO.read(new File("src/sprites/Spike2.png")); 
		}
		catch(java.io.IOException e) { 
			e.printStackTrace();
		}
	}
	
	//THERE IS NO NULL CHECK, WE MUST DETERMINE BEFORE HAND WHERE THE IMAGE IS
	public DownwardSpikes(int x, int y, JPanel jpane) {
		super(x, y, downSpikeTexture, jpane, true);
		hitBox = new Rectangle(x,y,gameController.getBlockDimension(),(int)(gameController.getBlockDimension()/2));
		collideable = false;
		hazard = true;
	}
	
	public String toString() {
		return "DownwardSpikes";
	}
	
	public void hazard(Player p) {
		p.respawn();
	}
}
