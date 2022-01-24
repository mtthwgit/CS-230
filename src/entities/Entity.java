package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Entity {
	public static final int DIR_LEFT = 0;
	public static final int DIR_RIGHT = 1;
	//integer location in pixels of top left corner
	public int x;
	public int y;
	//buffered images for left and right directional
	private BufferedImage lSprite;
	private BufferedImage rSprite;
	//entity width and height by image width and height
	private int width;
	private int height;
	//boolean is this entity visible
	private boolean  visible = false;
	//current JPanel to paint to
	private JPanel currentPane;
	private int currentDir;
	
	static JFrame testFrame = new JFrame();
	static JPanel testPane = new JPanel();
	protected JLabel label = new JLabel();
	
	//private ImageIcon leftSprite;
	//private ImageIcon rightSprite;
	
	/**
	 * 
	 * @param x upper left corner (pixel)
	 * @param y upper right corner (pixel)
	 * @param left LEFT SPRITE (same size as right)
	 * @param right RIGHT SPRITE (same size as left)
	 * @param pane current pane
	 * @throws IOException IMAGE DIMENSIONS ARE NOT THE SAME
	 */
	public Entity(int x, int y, BufferedImage lSprite, BufferedImage rSprite, JPanel pane) throws IOException {
		
		//set left and right sprite image
		//this.lSprite = left;
		//this.rSprite = right;
		try {
			this.rSprite = ImageIO.read(new File("src/sprites/Run (1).png"));
			this.lSprite = ImageIO.read(new File("src/sprites/Run (1).png"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		if (lSprite.getWidth() != rSprite.getWidth() || lSprite.getHeight() != rSprite.getHeight())
			throw new java.io.IOException("Ensure image dimensions are the same");
		//set start location
		this.x = x;
		this.y = y;
		//this.width = lSprite.getWidth();
		//this.height = lSprite.getHeight();
		currentDir = DIR_LEFT;
		
		currentPane = pane;
		
		label = new JLabel(new ImageIcon(rSprite));
		currentPane.add(label);
		toggleVisible();
		
		System.out.println(label);
	}
	
	/**
	 * 
	 * @param x upper left corner (pixel)
	 * @param y upper right corner (pixel)
	 * @param left LEFT SPRITE (same size as right)
	 * @param right RIGHT SPRITE (same size as left)
	 * @param pane current pane
	 * @param dir integer denoting direction; 0=Left, 1=Right
	 * @throws IOException IMAGE DIMENSIONS ARE NOT THE SAME
	 */
	public Entity(int x, int y, BufferedImage left, BufferedImage right, JPanel pane, int dir) throws IOException {
		this(x,y,left,right,pane);
		currentDir = dir;
	}
	
	//draws based on direction
	public void draw() {
		if (visible) {
			switch (currentDir) {
				case DIR_LEFT:
					label.setIcon(new ImageIcon(rSprite));
				case DIR_RIGHT:
					label.setIcon(new ImageIcon(lSprite));
			}
		}
		/*
		if (currentIconType == "Idle" || currentIconType == "leftIdle") {
			currentIconNumber = 0;
			File file = new File("src/sprites/" + currentIconType + " " + "(" + currentIconNumber + ")" + ".png");
			try {
				currentImage = ImageIO.read(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentIcon = new ImageIcon(currentImage);
			label.setIcon(currentIcon);
		}
		else {
			currentIconNumber = (currentIconNumber+1) %15;
			File file = new File("src/sprites/" + currentIconType + " " + "(" + currentIconNumber + ")" + ".png");
			try {
				currentImage = ImageIO.read(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentIcon = new ImageIcon(currentImage);
			label.setIcon(currentIcon);
		}
		*/
	}
	
	//erases entity
	public void erase() {
		currentPane.getGraphics().clearRect(x, y, width, height);
	}
	
	//toggles whether the entity should be visible
	public void toggleVisible() {
		visible = !visible;
	}
	
	//changes the left and right images of the entity
	public void updateSprites(BufferedImage left, BufferedImage right) throws IOException {
		if (left.getWidth() != right.getWidth() || left.getHeight() != right.getHeight())
			throw new java.io.IOException("Ensure image dimensions are the same");
		this.lSprite = left;
		this.rSprite = right;
		//erase();
		draw();
	}
	
	public int getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(int currentDir) {
		this.currentDir = currentDir;
	}

	//Called when changing directions by control handler
	public void toggleDir() {
		if (currentDir == DIR_LEFT)
			currentDir = DIR_RIGHT;
		else
			currentDir = DIR_LEFT;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public BufferedImage[] getSprites() { BufferedImage[] temp = {lSprite, rSprite}; return temp;  }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
}
