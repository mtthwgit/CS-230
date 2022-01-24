package tiles;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import entities.Player;

public class PuzzleTile extends Tile implements InteractableTile{
	
	//see PuzzleItem for info
	private int typeID;
	private int specialID = -1;
	
	private boolean completePuzzle = false;
		
	public PuzzleTile(int x, int y, BufferedImage sprite, JPanel pane, int type, int special) {
		super(x,y,sprite,pane,false);
		this.typeID = type;
		this.specialID = special;
	}
	
	public boolean checkMatch(int typeID, int special) {
		if (this.specialID != -1 && this.specialID == special)
			return true;
		else if (this.typeID == typeID)
			return true;
		else
			return false;
	}
	
	public boolean isComplete() {
		return completePuzzle;
	}
	
	@Override
	public void use(Player player) {
		if (player.getInventory() instanceof PuzzleItem) {
			PuzzleItem key = (PuzzleItem) player.getInventory();
			completePuzzle = (checkMatch(key.getTypeID(), key.getSpecial()));
		}
	}
}
