package tiles;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import entities.Player;

public class PuzzleItem extends Tile implements InteractableTile{
	
	//match type of "key" to type of PuzzleTile
	private int typeID;
	
	//if you need a specific item for specific Tile then use this in addition to the typeID
	private int specialID = -1;
	
	private String name;
	
	public PuzzleItem(int x, int y, BufferedImage sprite, JPanel pane) {
		super(x, y, sprite, pane, true);
	}
	
	public PuzzleItem(int x, int y, BufferedImage sprite, String name, JPanel pane) {
		super(x, y, sprite, pane, true);
		this.setName(name);
	}
	
	public int getTypeID() {
		return typeID;
	}
	
	public int getSpecial() {
		return specialID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void use(Player player) {
		if (player.getInventory() == null) {
			if (this.breakTile()) {
				player.setInventory(this);
			}
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
