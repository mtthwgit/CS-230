package tiles;

import entities.Player;

public interface InteractableTile {
	/**
	 * Method of interacting with a tile, whether it be using, breaking, or switching.
	 */
	public void use(Player player);
}
