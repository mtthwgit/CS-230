package entities;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import tiles.Tile;
import tiles.SpawnPoint;

import main.gameController;
import main.gameDisplay;

public class Player extends Entity implements KeyListener, MouseListener {

	private boolean iWon = false;
	private int dx; //Direction x (change)
    private int dy; //Direction y (change)
    private boolean isAirbourne = false; //to utilize timer to set limit on jump
    private static BufferedImage[] run;
    private static BufferedImage[] leftRun;
    private static BufferedImage[] idle;
    private static BufferedImage[] leftIdle;
    private static BufferedImage[] jump;
    private static BufferedImage[] leftJump;
    private static BufferedImage[] dead;
    private static BufferedImage[] leftDead;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private boolean canJump = true; //to tell the player if they can jump
    private Tile inventorySlot = null;
    private SpawnPoint sp;
    
    //private int[] xpoints = new int[3];
    //private int[] ypoints = new int[5];
    
    private int playerFactor = (int)(1*gameController.getBlockDimension());
    
    private boolean canMoveDown = true;
    private boolean canMoveRight = true;
    private boolean canMoveLeft = true;
    private boolean canMoveUp = true;
    //put timer and keep track in the controlling display class to set dy to -2 for time equivalent to time moving up (Super Mario Feel)

    //Hitbox for collision detecting
    private Rectangle hitBox;
    
    private int currentIconNumber = 1;
    private String currentIconType = "Run";
    private BufferedImage currentImage;
    private ImageIcon currentIcon;
    private JPanel panel;
   
    /**
     * 
     * @param x starting x
     * @param y starting y
     * @param imageLeft left facing image
     * @param imageRight right facing image
     * @param pane pane to be painted to
     * @param sp spawnpoint of player
     * @throws IOException cannot find images
     **/
    
    public Player(int x, int y, BufferedImage imageLeft, BufferedImage imageRight, JPanel pane, SpawnPoint sp) throws IOException {
    	super(x,y,imageLeft,imageRight,pane);
    	this.sp = sp;
    	panel = pane;
    	pane.setFocusable(true);
    	pane.addKeyListener(this);
    	System.out.println(pane);
    	imageLeft = ImageIO.read(new File("src/sprites/leftIdle (0).png"));
    	imageRight = ImageIO.read(new File("src/sprites/Idle (0).png"));
    	//hitBox slightly bigger than player to make collision detection easier
    	hitBox = new Rectangle(x,y,playerFactor,(playerFactor*2));
    }
    
    public void loadImage(String fileName) {
		try {
			idle = new BufferedImage[1];
			leftIdle = new BufferedImage[1];
			run = new BufferedImage[16];
			leftRun = new BufferedImage[16];
			jump = new BufferedImage[16];
			leftJump = new BufferedImage[16];
			dead = new BufferedImage[16];
			leftDead = new BufferedImage[16];
			
			idle[0] = ImageIO.read(new File("src/sprites/Idle (0).png"));
			leftIdle[0] = ImageIO.read(new File("src/sprites/leftIdle (0).png"));
			for (int i = 0; i<15; i++) {
				run[i] = ImageIO.read(new File("src/sprites/Run ("+ (i+1) +").png"));
			}
			for (int i = 0; i<15; i++) {
				leftRun[i] = ImageIO.read(new File("src/sprites/leftRun ("+ (i+1) +").png"));
			}
			for (int i = 0; i<15; i++) {
				jump[i] = ImageIO.read(new File("src/sprites/Jump ("+ (i+1) +").png"));
			}
			for (int i = 0; i<15; i++) {
				leftJump[i] = ImageIO.read(new File("src/sprites/leftJump ("+ (i+1) +").png"));
			}
			for (int i = 0; i<15; i++) {
				dead[i] = ImageIO.read(new File("src/sprites/Dead ("+ (i+1) +").png"));
			}
			for (int i = 0; i<15; i++) {
				leftDead[i] = ImageIO.read(new File("src/sprites/leftDead ("+ (i+1) +").png"));
			}
		} catch (IOException e) {e.printStackTrace();}
	}
    
    
    /**
     * Moves the Player and Checks Collisions
     */
    public void move() {
    	//updatePoints();
    	//collisionCheckX();
    	//collisionCheckY();
    	collisionCheck();
    	if(!canMoveDown) {canJumpTrue();} else {canJumpFalse();}
    	
    	if(!canMoveDown && dy > 0) {setDY(0);}
    	if(!canMoveUp && dy < 0) {setDY(0);}
    	if(!canMoveRight && dx > 0) {setDX(0);}
    	if(!canMoveLeft && dx < 0) {setDX(0);}
    	
    	
    	
    	
    	this.setX(this.getX()+dx);
    	this.setY(this.getY()+dy);
    	
    	//DEBUG System.out.println("DX " + dx + "; DY " + dy);
    	panel.setLocation((panel.getX()-dx), (panel.getY()-dy));
    	
    	
    	
    	canMoveDown = true;
		canMoveUp = true;
		canMoveRight = true;
		canMoveLeft = true;
		
    	
		//updating animations
		if (currentIconType == "Idle" || currentIconType == "leftIdle") {
			currentIconNumber = 0;
			File file = new File("src/sprites/" + currentIconType + " " + "(" + currentIconNumber + ")" + ".png");
			try {
				currentImage = ImageIO.read(file);
			} catch (IOException e) {}
			currentIcon = new ImageIcon(currentImage); //load the image to a imageIcon
			Image img = currentIcon.getImage(); // transform it 
			Image newimg = img.getScaledInstance(playerFactor,playerFactor*2, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			currentIcon = new ImageIcon(newimg);
			label.setIcon(currentIcon);
		}		
		else {
			currentIconNumber = (currentIconNumber>=15) ? 0 : (currentIconNumber+1) %15;
			File file = new File("src/sprites/" + currentIconType + " " + "(" + currentIconNumber + ")" + ".png");
			
			try {
				currentImage = ImageIO.read(file);
			} catch (IOException e) {}
			currentIcon = new ImageIcon(currentImage); //load the image to a imageIcon
			Image img = currentIcon.getImage(); // transform it 
			Image newimg = img.getScaledInstance(playerFactor,playerFactor*2, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			currentIcon = new ImageIcon(newimg);  // transform it back
			label.setIcon(currentIcon);
		}
		
	}
    
    public void respawn() {
    	int x = sp.getX();
    	int y = sp.getY() - playerFactor;
    	this.setX(x);
    	this.setY(y);
    	label.setBounds(x, y, playerFactor, playerFactor*2);
		hitBox.setBounds(label.getX(),label.getY(),playerFactor, playerFactor*2);
    	
    	panel.setLocation((int)(screenSize.getWidth()/2)-x-(int)(playerFactor/2),(int)(screenSize.getHeight()/2)-y-playerFactor);
    }

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT) {
			System.out.println("LEFT MOVE");
        	setCurrentDir(DIR_LEFT);
        	currentIconType = "leftRun";
        	dx = -10;
		}
		if (key == KeyEvent.VK_RIGHT) {
			System.out.println("RIGHT MOVE");
        	setCurrentDir(DIR_RIGHT);
        	currentIconType = "Run";
        	dx = 10;
		}
		if (key == KeyEvent.VK_UP) {
			if (canJump) {
        		if (getCurrentDir() == DIR_RIGHT) {
    				System.out.println("UP RIGHT MOVE");
        			currentIconType = "Jump";
        			dy = -1 * gameController.getBlockDimension()/6;
    			}
    			else {
    				System.out.println("UP LEFT MOVE");
        			currentIconType = "leftJump";
        			dy = -1 * gameController.getBlockDimension()/6;
    			}
        	}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_LEFT) {
			currentIconType = "leftIdle";
        	currentIconNumber = 0;
        	dx = 0;
		}
		if (key == KeyEvent.VK_RIGHT) {
			currentIconType = "Idle";
        	currentIconNumber = 0;
        	dx = 0;
		}
		if (key == KeyEvent.VK_UP) {
			if (getCurrentDir() == DIR_RIGHT)
        	{
        		currentIconType = "Idle";
            	currentIconNumber = 0;
        	}
        	else {
        		currentIconType = "leftIdle";
            	currentIconNumber = 0;
        	}
		}
	}
	
	public JLabel getPlayerLabel() {
		return label;
	}
	
	/*-------------------------/
	 * GETTERS AND SETTERS
	/*------------------------*/
	public SpawnPoint getSpawnPoint() {return sp;}
	public int getDX() { return dx; }
    public int getDY() { return dy; }
    public int getX() {return label.getX();}
    public int getY() {return label.getY();}
    public boolean isAirbourne() { return isAirbourne; }
    //FOR USE IN THE CONTROLLING DISPLAY CLASS WITH TIMER
    public void setDY(int dY) { this.dy = dY; }
    public void setDX(int dX) {this.dx = dX;}
    public Tile getInventory() {return inventorySlot;}
	public void setSpawnPoint(SpawnPoint sp) {this.sp = sp;}
	public void setInventory(Tile t) {inventorySlot = t;}
	public void clearInventory() {inventorySlot = null;}
	public void invertDY() {dy = -1*dy;}
	public void canJumpFalse() {canJump = false;}
	public void canJumpTrue() {canJump = true;}
	
	
	/*
	 * THIS IS WHERE WE ARE HANDLING COLLISIONS
	 */
	public boolean canMoveDown() {return canMoveDown;}
	public void setCanMoveDown(boolean b) {canMoveDown = b;}
	public void setCanMoveUp(boolean b) {canMoveUp = b;}
	public void setCanMoveLeft(boolean b) {canMoveLeft = b;}
	public void setCanMoveRight(boolean b) {canMoveRight = b;}
	
	public void won() {iWon = true;}
	public boolean didWin() {return iWon;}
 	
	public void setX(int x) {
		this.x = x;
		label.setBounds(x,label.getY(),gameController.getBlockDimension(), gameController.getBlockDimension()*2);
		hitBox.setBounds(x, label.getY(),gameController.getBlockDimension(), gameController.getBlockDimension()*2);
		panel.setLocation((int)(screenSize.getWidth()/2)-x-(int)(gameController.getBlockDimension()/2),(int)(screenSize.getHeight()/2)-y-gameController.getBlockDimension());
	}
	public void setY(int y) {
		this.y = y;
		label.setBounds(label.getX(),y,gameController.getBlockDimension(), gameController.getBlockDimension()*2);
		hitBox.setBounds(label.getX(), y,gameController.getBlockDimension(), gameController.getBlockDimension()*2);
		panel.setLocation((int)(screenSize.getWidth()/2)-x-(int)(gameController.getBlockDimension()/2),(int)(screenSize.getHeight()/2)-y-gameController.getBlockDimension());
	}
	
	/*
	public void collisionCheckX() {
		Tile[][] level = ((gameDisplay) panel).getLevelLayout();
		int dim = gameController.getBlockDimension();
		for (int x : xpoints) {
			Tile temp = level[(int) (x/dim)][this.getY()/dim];
			if (temp != null && temp.getHitBox().intersects(this.getHitBox())) {
				if (temp.isHazard())
					this.respawn();
				else {
					if (x < this.getX()) {
						canMoveLeft = false;
					}
					if (x > this.getX()) {
						canMoveRight = false;
					}
				}
			}
		}
	}
	
	public void collisionCheckY() {
		Tile[][] level = ((gameDisplay) panel).getLevelLayout();
		int dim = gameController.getBlockDimension();
		if(this.getY()/dim >= level[0].length-2) {
			this.setCanMoveDown(false);
		} else {
			for (int y : ypoints) {
				Tile temp = level[this.getX()/dim][(int) (y/dim)];
				if (temp != null ) {
					if (temp.isHazard())
						this.respawn();
					else if (temp.isCollideable()){
						if (y < this.getY()) {
							canMoveUp = false;
							System.out.println("TOP!");
						}
						if (y > this.getY()) {
							canMoveDown = false;
							System.out.println("BOTTOM!");
						}
					}
				}
			}
		}
	}
	
	private void updatePoints() {
		Rectangle hitbox = this.getHitBox();
		int xpointInt = (int)hitbox.getWidth()/2;
		int ypointInt = (int)hitbox.getHeight()/4;
		
		
		for (int i = 0; i < xpoints.length; i++) {
			xpoints[i] = (int) ((this.getX() + xpointInt*i) + dx);
		}
		
		for (int i = 0; i < ypoints.length; i++) {
			ypoints[i] = (int) ((this.getY() + ypointInt*i) + dy);
		}
		
	}
	*/
	
	public void collisionCheck() {
		Tile[][] level = ((gameDisplay) panel).getLevelLayout();
		int x = this.getX();
		int y = this.getY();
		int dx = this.getDX();
		int dy = this.getDY();
		int dim = gameController.getBlockDimension();
		int xIA = x/dim;
		int yIA = y/dim;
		int buffer = (int)(dim/15);
		
		//upward hazard test
		if(level[xIA][yIA] != null) {
			if(level[xIA][yIA].isHazard()) {
				if(this.getHitBox().intersects(level[xIA][yIA].getHitBox())) {
					level[xIA][yIA].hazard(this);
				}
			}
		}
		//feet hazard test
		if(yIA < level[0].length) {
			if(level[xIA][yIA+1] != null) {
				if(level[xIA][yIA+1].isHazard()) {
					if(this.getHitBox().intersects(level[xIA][yIA+1].getHitBox())) {
						level[xIA][yIA+1].hazard(this);
					}
				}
			}
		}
		//this going left
		if(dx < 0) {
			if((xIA == 0 && x <= 0)) {
				this.setCanMoveLeft(false);
			} else if(x > dim){
				if(level[xIA-1][yIA] != null) {
					if(level[xIA-1][yIA].isCollideable() && x < level[xIA-1][yIA].getX()+dim+buffer) {
						this.setCanMoveLeft(false);
						if(this.getHitBox().intersects(level[xIA-1][yIA].getHitBox())) {
							this.setX((xIA*dim)+dim);
						}
					} else {
						if(level[xIA-1][yIA].isHazard()) {
							if(this.getHitBox().intersects(level[xIA-1][yIA].getHitBox())) {
								level[xIA-1][yIA].hazard(this);
							}
						}
					} 
				}
				if(level[xIA-1][yIA+1] != null) {
					if(level[xIA-1][yIA+1].isCollideable() && x < level[xIA-1][yIA+1].getX()+dim+buffer) {
						this.setCanMoveLeft(false);
						if(this.getHitBox().intersects(level[xIA-1][yIA+1].getHitBox())) {
							this.setX((xIA*dim)+dim);
						}
					} else {
						if(level[xIA-1][yIA+1].isHazard()) {
							if(this.getHitBox().intersects(level[xIA-1][yIA+1].getHitBox())) {
								level[xIA-1][yIA+1].hazard(this);
							}
						}
					} 
				}
				if(yIA < level[0].length-2) {
					if(level[xIA-1][yIA+2] != null) {
						if(level[xIA-1][yIA+2].isCollideable() && y > level[xIA-1][yIA+2].getY()-(dim*2) && x < level[xIA-1][yIA+2].getX()+dim+buffer) {
							this.setCanMoveLeft(false);
						} else {
							if(level[xIA-1][yIA+2].isHazard()) {
								if(this.getHitBox().intersects(level[xIA-1][yIA+2].getHitBox())) {
									level[xIA-1][yIA+2].hazard(this);
								}
							}
						}
					}
				}
			}
		}
		//this going right
		if(dx > 0) {
			if(xIA == level.length-1) {
				this.setCanMoveRight(false);
			} else {
				if(level[xIA+1][yIA] != null) {
					if(level[xIA+1][yIA].isCollideable()) {
						this.setCanMoveRight(false);
						if(this.getHitBox().intersects(level[xIA+1][yIA].getHitBox())) {
							this.setX(xIA*dim);
						}
					} else {
						if(level[xIA+1][yIA].isHazard()) {
							if(this.getHitBox().intersects(level[xIA+1][yIA].getHitBox())) {
								level[xIA+1][yIA].hazard(this);
							}
						}
					}
				}
				if(level[xIA+1][yIA+1] != null) {
					if(level[xIA+1][yIA+1].isCollideable()) {
						this.setCanMoveRight(false);
						if(this.getHitBox().intersects(level[xIA+1][yIA+1].getHitBox())) {
							this.setX(xIA*dim);
						}
					}  else {
						if(level[xIA+1][yIA+1].isHazard()) {
							if(this.getHitBox().intersects(level[xIA+1][yIA+1].getHitBox())) {
								level[xIA+1][yIA+1].hazard(this);
							}
						}
					}
				}
				if(yIA < level[0].length-2) {
					if(level[xIA+1][yIA+2] != null) {
						if(level[xIA+1][yIA+2].isCollideable() && y > level[xIA+1][yIA+2].getY()-(dim*2)+buffer) {
							this.setCanMoveRight(false);
							if(this.getHitBox().intersects(level[xIA+1][yIA+2].getHitBox())) {
								this.setX(xIA*dim);
							}
						} else {
							if(level[xIA+1][yIA+2].isHazard()) {
								if(this.getHitBox().intersects(level[xIA+1][yIA+2].getHitBox())) {
									level[xIA+1][yIA+2].hazard(this);
								}
							}
						}
					}
				}
			}
		}
		//this going up
		if(dy < 0) {
			if(yIA <= 0 && y <= 0) {
				this.setCanMoveUp(false);
			} else if(y > dim){
				if(level[xIA][yIA-1] != null && y <= level[xIA][yIA-1].getY()+dim+(buffer/2)) {
					if(level[xIA][yIA-1].isCollideable()) {
						this.setCanMoveUp(false);
						if(this.getHitBox().intersects(level[xIA][yIA-1].getHitBox())) {
							this.setY((yIA+1)*dim);
						}
					} else {
						if(level[xIA][yIA-1].isHazard()) {
							if(this.getHitBox().intersects(level[xIA][yIA-1].getHitBox())) {
								level[xIA][yIA-1].hazard(this);
							}
						}
					}
				}
				if(xIA > 0) {
					if(level[xIA+1][yIA-1] != null) {
						if(level[xIA+1][yIA-1].isCollideable() && x >= level[xIA+1][yIA-1].getX()-dim+buffer && y < level[xIA+1][yIA-1].getY()+dim+(buffer/2)) {
							this.setCanMoveUp(false);
							if(this.getHitBox().intersects(level[xIA+1][yIA-1].getHitBox())) {
								this.setY((yIA+1)*dim);
							}
						}  else {
							if(level[xIA+1][yIA-1].isHazard() && x >= level[xIA+1][yIA-1].getX()-dim+buffer && y < level[xIA+1][yIA-1].getY()+dim+(buffer/2)) {
								if(this.getHitBox().intersects(level[xIA+1][yIA-1].getHitBox())) {
									level[xIA+1][yIA-1].hazard(this);
								}
							}
						}
					}
				}
			}
		}
		//this going down
		if(dy > 0) {
			if(yIA >= level[0].length-2) {
				this.setCanMoveDown(false);
			} else {
				if(level[xIA][yIA+2] != null) {
					if(level[xIA][yIA+2].isCollideable()) {
						this.setCanMoveDown(false);
						if(this.getHitBox().intersects(level[xIA][yIA+2].getHitBox())) {
							this.setY(yIA*dim);
						}
					} else {
						if(level[xIA][yIA+2].isHazard()) {
							if(this.getHitBox().intersects(level[xIA][yIA+2].getHitBox())) {
								level[xIA][yIA+2].hazard(this);
							}
						}
					}
				}
				if(xIA < level.length-1) {
					if(level[xIA+1][yIA+2] != null) {
						if(level[xIA+1][yIA+2].isCollideable() && x >= level[xIA+1][yIA+2].getX()-dim+(buffer*2)) {
							this.setCanMoveDown(false);
							if(this.getHitBox().intersects(level[xIA+1][yIA+2].getHitBox())) {
								this.setY(yIA*dim);
							}
						} else {
							if(level[xIA+1][yIA+2].isHazard() && x >= level[xIA+1][yIA+2].getX()-dim+(buffer*2)) {
								if(this.getHitBox().intersects(level[xIA+1][yIA+2].getHitBox())) {
									level[xIA+1][yIA+2].hazard(this);
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	public Rectangle getHitBox() {
		return hitBox;
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		//Unneeded
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
