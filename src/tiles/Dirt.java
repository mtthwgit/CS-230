package tiles;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entities.Player;

public class Dirt extends Tile implements InteractableTile{
	
	static BufferedImage dirtTexture;
	static {
		try { 
			dirtTexture = ImageIO.read(new File("src/sprites/Dirt.png")); 
		}
		catch(java.io.IOException e) { 
			e.printStackTrace();
		}
	}
	
	//THERE IS NO NULL CHECK, WE MUST DETERMINE BEFORE HAND WHERE THE IMAGE IS
	public Dirt(int x, int y, JPanel jpane) {
		super(x, y, dirtTexture, jpane, true);
	}

	@Override
	public void use(Player player) {
		if (player.getInventory() == null) {
			if (this.breakTile()) {
				player.setInventory(this);
			}
		}
	}
	
	public String toString() {
		return "Dirt";
	}
}
