package tiles;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entities.Player;

public class SpawnPoint extends Tile implements InteractableTile{
	
	private boolean isCurrent;
	private BufferedImage currentImage;
	
	static BufferedImage[] texture = new BufferedImage[2];
	static {
		try { 
			texture[0] = ImageIO.read(new File("src/sprites/greenflag.png")); //image for active
			texture[1] = ImageIO.read(new File("src/sprites/redflag.png")); //image for inactive
		}
		catch(java.io.IOException e) { 
			e.printStackTrace();
		}
	}

	public SpawnPoint(int x, int y,JPanel pane) {
		super(x, y, texture[1], pane, false);
		collideable = false;
		currentImage = texture[1];
		isCurrent = false;
	}
	
	public SpawnPoint(int x, int y,JPanel pane, boolean isDefault) {
		super(x, y, (isDefault) ? texture[0] : texture[1], pane, false); //in-line condition to determine texture based on default value
		collideable = false;
		currentImage = (isDefault) ? texture[0] : texture[1];
		isCurrent = isDefault;
	}
	
	public void toggleIsCurrent() {
		isCurrent = !(isCurrent);
		this.swapImage();
	}
	
	public void setCurrent(boolean b) {
		isCurrent = b;
	}
	
	//Have one activated image and one unactivated image
	public void swapImage() {
		if (!(currentImage.equals(texture[0])))
			currentImage = texture[0];
		else
			currentImage = texture[1];
	}

	@Override
	public void use(Player player) {
		player.getSpawnPoint().toggleIsCurrent();
		this.toggleIsCurrent();
		player.setSpawnPoint(this);
	}
	
	public BufferedImage getImage() {
		return currentImage;
	}
	
	public boolean isCurrent() {
		return isCurrent;
	}
	
	public String toString() {
		return "SpawnPoint";
	}
}
