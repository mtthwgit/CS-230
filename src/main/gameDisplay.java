package main;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import entities.Player;
import tiles.SpawnPoint;
import tiles.Tile;
import levelBuilder.levelInfo;
import levelBuilder.LevelLoader;

import java.util.Timer;
import java.util.TimerTask;



public class gameDisplay extends JPanel{

	private static final long serialVersionUID = -3506813690426695567L;
	
	private boolean gameIsReady = false;
	
	private Player player;
	private gameController frame;
	private Timer timer = new Timer();
	private Time timerThing;
	private static final int MOVE_TIME = 17;
	private levelInfo currentLevel;
	private SpawnPoint currentSpawn;
	private boolean exit = false;
	//private int timerInterval = 17; //Unneeded due to change in timer use

	public gameDisplay(gameController frame) {
		this.frame = frame;
		frame.getContentPane().setLayout(null);
		this.setLayout(null);
		this.setBackground(Color.gray);
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				int key = arg0.getKeyCode();
				if(key == KeyEvent.VK_ESCAPE) {
					timer.cancel();
					frame.gameToMain();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		File filePath = new File("..\\game");
		if(!filePath.exists()) {
			filePath = new File("../game");
		}
		File[] fileList = filePath.listFiles();
		String[] fileNames = new String[fileList.length];
		int levelCount = 0;
		for(int i = 0; i < fileList.length; i ++) {
			if(fileList[i].getName().length() > 4) {
				if(fileList[i].getName().substring(fileList[i].getName().length() -4).equals(".txt")) {
					levelCount++;
					fileNames[i] = fileList[i].getName();
				}
			}
		}
		String[] levelNames = new String[levelCount];
		int inc = 0;
		for(int i = 0; i < fileNames.length; i++) {
			if(fileNames[i] != null) {
				levelNames[inc] = fileNames[i];
				inc++;
			}
		}
		JComboBox<String> levelComboBox = new JComboBox<String>(levelNames);
		int response = JOptionPane.showConfirmDialog(null, levelComboBox, "Select a level", 
				JOptionPane.OK_CANCEL_OPTION);
		if(response == JOptionPane.OK_OPTION) {
			try {
				currentLevel = LevelLoader.load((String)levelComboBox.getSelectedItem(),this);
			} catch (FileNotFoundException err) {
				
			}
		} else {
			exit = true;
		}
		if(!exit) {
				this.setBounds(0,0,currentLevel.getLevel().length*gameController.getBlockDimension(),currentLevel.getLevel()[0].length*gameController.getBlockDimension());
				currentLevel.drawLevel();
				setCurrentSpawn();
				try {
					BufferedImage tempSprite = ImageIO.read(new File("src/sprites/Idle (0).png"));
					player = new Player(0,0,tempSprite,tempSprite,this,currentSpawn);
				} catch (IOException e1) {
					
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				timerThing = new Time();
				timer.schedule(timerThing, 0, MOVE_TIME);
				player.respawn();
				player.draw();
				gameIsReady = true;
			} else {
				timerThing = new Time();
				timer.schedule(timerThing, 500);
			}
		}
	
		public void scalePlayer() {
			ImageIcon imageIcon = new ImageIcon("images"); // load the image to a imageIcon
			Image image = imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(gameController.getBlockDimension(), gameController.getBlockDimension()*2, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);  // transform it back
		}
		
		class Time extends TimerTask {
			public void run() {
				if (gameIsReady) {
					if(player.getDY() < 20 && player.canMoveDown()) {
						player.setDY(player.getDY()+1);
					}
					player.move();
					if(player.didWin()) {
						timer.cancel();
						JOptionPane.showMessageDialog(null, "You escaped Bob's Basement!");
						frame.gameToMain();
					}
				} else if(exit){
					timer.cancel();
					frame.gameToMain();
				}
			}
		}
	
		public void setCurrentSpawn() {
			Tile[][] levelLayout = currentLevel.getLevel();
			for(int y = 0; y < levelLayout[0].length; y++) {
				for(int x = 0; x < levelLayout.length; x++) {
					if(levelLayout[x][y] instanceof SpawnPoint) {
						SpawnPoint sp = ((SpawnPoint) levelLayout[x][y]);
						if(sp.isCurrent()) {
							currentSpawn = sp;
						}
					}
				}
			}
		}
	
		public Tile[][] getLevelLayout() {
			Tile[][] temp1 = currentLevel.getLevel();
			Tile[][] temp2 = new Tile[temp1.length][temp1[0].length];
			for (int i = 0; i < temp1.length; i ++) {
				for (int j = 0; j < temp1[i].length; j ++) {
					temp2[i][j] = temp1[i][j];
				}
			}
			return temp2;
		}
}