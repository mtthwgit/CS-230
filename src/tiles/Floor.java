package tiles;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entities.Player;

public class Floor extends Tile implements InteractableTile {

	static BufferedImage floorTexture;
	static {
		try { 
			floorTexture = ImageIO.read(new File("src/sprites/BGTile (3).png")); 
		}
		catch(java.io.IOException e) { 
			e.printStackTrace();
		}
	}
	
	//THERE IS NO NULL CHECK, WE MUST DETERMINE BEFORE HAND WHERE THE IMAGE IS
	public Floor(int x, int y, JPanel jpane) {
		super(x, y, floorTexture, jpane, true);
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
		return "Floor";
	}

}
