package levelBuilder;

import javax.swing.*;
import main.gameController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import tiles.*;
import tiles.Box;

public class levelBuilder extends JLayeredPane{
	//JPanel wanted this
	private static final long serialVersionUID = 2370201629065856700L;
	
	private JPanel tileSelect; //For holding all the swing objects that select the tile to place
	private JPanel levelPanel; //for displaying level
	private JComboBox tileCB; //For selecting specific tile in your tile group
	private Tile[][] levelArray; //array for keeping track of placed tiles
	private gameController thisFrame; //used for keeping track of parent frame
	private Point levelLoc; //location of level, used for dragging frame around
	private Tile[] tileArray; //used for Combobox selection
	private boolean isDefaultPlaced = false; //flag for default spawn point
	private boolean dragging; //flag for detecting if the pane is getting dragged or not
	
	public levelBuilder(gameController frame) {
		
		this.setLayout(null);
		
		//Setting background image
		JLabel picLabel = new JLabel(); //create jlabel
		ImageIcon background = new ImageIcon("src/main/bob.jpeg"); //init image
		Image img = background.getImage(); //transform to img
		Image newImg = img.getScaledInstance(gameController.getWindowWidth(), gameController.getWindowHeight(), Image.SCALE_SMOOTH); //create scaled version
		ImageIcon finalImage = new ImageIcon(newImg); //transform back to image icon
		picLabel.setIcon(finalImage); //set image to label
		picLabel.setBounds(0, 0, gameController.getWindowWidth(), gameController.getWindowHeight()); //set bounds of label on the layered panel. (has to be done with layered panel.
		this.add(picLabel, 2);
		
		//Creating key variables and panels
		tileSelect = new JPanel();
		tileSelect.setLayout(null);
		levelPanel = new JPanel();
		levelPanel.setLayout(null);
		makeTileArray(); //populating tileArray
		int Sp = frame.getBlockDimension(); //these are used for placing/moving buttons so that
		int Hsp = (int)(Sp/2); 				//screen width doesn't change visuals
		thisFrame = frame;
		
		//Making and buttons and adding listeners
		JButton exitButton = new JButton("Main Menu");
		exitButton.setBackground(new Color((float)0.996,(float)0.992,(float)0.871));
		exitButton.setFocusPainted(false);
		exitButton.setBorder(BorderFactory.createLineBorder(Color.black,2));
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.builderToMain();
			}
		});
		JButton newButton = new JButton("New Level");
		newButton.setBackground(new Color((float)0.996,(float)0.992,(float)0.871));
		newButton.setFocusPainted(false);
		newButton.setBorder(BorderFactory.createLineBorder(Color.black,2));
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					makeNewLevel();
				} catch (NumberFormatException err) {
					
				}
			}		
		});
		JButton saveButton = new JButton("Save Level");
		saveButton.setBackground(new Color((float)0.996,(float)0.992,(float)0.871));
		saveButton.setFocusPainted(false);
		saveButton.setBorder(BorderFactory.createLineBorder(Color.black,2));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				saveLevel();
				}
				catch (IOException err) {
					err.printStackTrace();
				} 
			}
		});
		JButton loadButton = new JButton("Load Level");
		loadButton.setBackground(new Color((float)0.996,(float)0.992,(float)0.871));
		loadButton.setFocusPainted(false);
		loadButton.setBorder(BorderFactory.createLineBorder(Color.black,2));
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					loadLevel();
				} catch (FileNotFoundException err) {
					JOptionPane.showMessageDialog(null, "There is no file of that name");
				} catch (NullPointerException err2) {
					//do nothing
				}
			}
		});
		
		//combobox for selecting the tiles and label for combobox
		//the string arrays need to be manually edited when we
		//add new tiles.
		JLabel cbLabel = new JLabel("Tile Select");
		cbLabel.setBackground(new Color((float)0.996,(float)0.992,(float)0.871));
		tileCB = new JComboBox(tileArray);
		tileCB.setBackground(new Color((float)0.996,(float)0.992,(float)0.871));
		tileCB.setBorder(BorderFactory.createLineBorder(Color.black,2));

		//Label for explaining controls
		JLabel helpLabel = new JLabel("<html>Left Click: Place<br/>Right Click: Delete<br/> Drag: Move Level");
		helpLabel.setBackground(new Color((float)0.996,(float)0.992,(float)0.95));
		
		//formatting tileSelect and then adding it to the Panel
		tileSelect.setBorder(BorderFactory.createTitledBorder("Tile Select"));
		tileSelect.setBackground(new Color((float)0.996,(float)0.992,(float)0.95));
		tileSelect.setVisible(true);
		tileSelect.setBounds(0,0,frame.getWidth(),(int)(frame.getHeight()*.15));
		//Adding combobox and buttons to the tileSelect and making bounds
		tileSelect.add(exitButton); exitButton.setBounds(Hsp,Hsp,Hsp*3,Hsp);
		tileSelect.add(newButton); newButton.setBounds(Hsp*5,Hsp,Hsp*3,Hsp);
		tileSelect.add(saveButton); saveButton.setBounds(Hsp*8,Hsp,Hsp*3,Hsp);
		tileSelect.add(loadButton); loadButton.setBounds(Hsp*11,Hsp,Hsp*3,Hsp);
		tileSelect.add(tileCB); tileCB.setBounds(Hsp*15,Hsp,Sp*2,Hsp);
		tileSelect.add(cbLabel); cbLabel.setBounds((Hsp*19)+10,Hsp,Sp,Hsp);
		tileSelect.add(helpLabel); helpLabel.setBounds(Hsp*21,0,Sp*3,Hsp*3);
		this.add(tileSelect, 0);
		tileCB.setVisible(true);
		
		//^^ but for level object
		this.add(levelPanel, 1);
		levelPanel.setBackground(Color.black);
		levelPanel.setVisible(true);
		//making level panel clickable
		levelPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				levelLoc = e.getPoint();
			}
		});
		
		this.setVisible(true);
	}
	
	//this will populate the levelArray for use in making a level.
	//It is called when the "new level" button is pushed.
	private void makeNewLevel() throws java.lang.NumberFormatException{
		//Creating a panel with two text fields for the popup message
		JTextField widthField = new JTextField(3);
		JTextField heightField = new JTextField(3);
		int width;
		int height;
		
		//creating the panel for placing in the popup
		JPanel panel = new JPanel();
		panel.add(new JLabel("Width: "));
		panel.add(widthField);
		panel.add(new JLabel("Height: "));
		panel.add(heightField);
		
		//recording and using response to the popup
		int response = JOptionPane.showConfirmDialog(null, panel, "Width and height in tiles", 
				JOptionPane.OK_CANCEL_OPTION);
		if(response == JOptionPane.OK_OPTION)
		{
			width = Integer.parseInt(widthField.getText());
			height = Integer.parseInt(heightField.getText());
			levelArray = new Tile[width][height];
			//  where the panel actually gets painted
			paintLevel(levelPanel.getGraphics());
		}
	}
	
	private void saveLevel() throws java.io.IOException{
		//prompting user for level name to save
		String levelName = JOptionPane.showInputDialog("Level Name");
		
		/*
		 * creates a new file object based on the name. Will overwrite if there
		 * is already a file with the given name.
		 */
		File newLevel = new File(levelName + ".txt");
		newLevel.delete();
		newLevel.createNewFile();
			FileWriter writer = new FileWriter(levelName + ".txt");
			writer.write(Integer.toString(levelArray.length)+":"+levelArray[0].length+"\n");
			for(int i = 0; i < levelArray[0].length; i++) {
				for(int j = 0; j < levelArray.length; j++) {
					if(levelArray[j][i]==null) {
						writer.write("null ");
					} else if (levelArray[j][i] instanceof SpawnPoint) {
						writer.write("SpawnPoint:" + ((SpawnPoint) levelArray[j][i]).isCurrent()+" ");
					}
					else {
					writer.write(levelArray[j][i].toString() + " ");
					}
				}
				writer.write("\n");
			}
			writer.close();

	}
	
	//Called when painting a level/loading it in
	private void paintLevel(Graphics g)
	{
		int tileSpacing = 1;
		int tileWidth = thisFrame.getBlockDimension();
		levelPanel.setVisible(false);
		levelPanel.removeAll();
		int width = levelArray.length;
		int height = levelArray[0].length;
		levelPanel.setBounds(0,tileSelect.getHeight(),(tileWidth+tileSpacing)*width+tileSpacing,(tileWidth+tileSpacing)*height+tileSpacing);
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				JLabel tileIcon = new JLabel();
				tileIcon.setEnabled(true);
				//for detecting a mouse click
				tileIcon.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						dragging = false;
						levelLoc = new Point(tileIcon.getX()+e.getX(),tileIcon.getY()+e.getY());
					}
					
					public void mouseReleased(MouseEvent b) {
						if(!dragging) {
							tilePressed(b);
						}
					}
				});
				//making level panel draggable
				tileIcon.addMouseMotionListener(new MouseAdapter() {
					public void mouseDragged(MouseEvent e) {
						dragging = true;
						Point currentScreenLoc = e.getLocationOnScreen();
						levelPanel.setLocation(currentScreenLoc.x - levelLoc.x, currentScreenLoc.y - levelLoc.y);
					}
				});
				
				if(levelArray[j][i]==null) {
					tileIcon.setBackground(Color.white);
					tileIcon.setIcon(null);
				}
				else {
					tileIcon.setBackground(Color.white);
					tileIcon.setIcon(null);
					tileIcon.setIcon(resizeTile(levelArray[j][i].getImage()));
				}
				tileIcon.setBounds(tileSpacing+(j*(tileWidth+tileSpacing)),tileSpacing+(i*(tileWidth+tileSpacing)),tileWidth,tileWidth);
				tileIcon.setOpaque(true);
				tileIcon.repaint();
				tileIcon.revalidate();
				tileIcon.setVisible(true);
				levelPanel.add(tileIcon);
			}
		}
		levelPanel.repaint();
		levelPanel.setVisible(true);
	}
	
	//What happens when one of the labels is pressed.
	private void tilePressed(MouseEvent e) {
		JLabel label = (JLabel) e.getSource();
		int x = label.getX();
		int y = label.getY(); 
		boolean canPlace = true;
		Object item = tileCB.getSelectedItem();
		if(item instanceof Tile) {
			Tile tile = (Tile)item;
			if(SwingUtilities.isLeftMouseButton(e)) {
				if(tile instanceof SpawnPoint) {
					if(!isDefaultPlaced) {
						((SpawnPoint) tile).setCurrent(true);
						isDefaultPlaced=true;
					} else {
						canPlace = false;
					}
				}
				if(canPlace) {
					levelArray[x/thisFrame.getBlockDimension()][y/thisFrame.getBlockDimension()] = tile;
					ImageIcon icon = resizeTile(tile.getImage());
					label.setIcon(icon);
					label.repaint();
				}
			}
			if(SwingUtilities.isRightMouseButton(e)) {
				if(levelArray[x/thisFrame.getBlockDimension()][y/thisFrame.getBlockDimension()] != null) {
					if(levelArray[x/thisFrame.getBlockDimension()][y/thisFrame.getBlockDimension()] instanceof SpawnPoint) {
						SpawnPoint temp = (SpawnPoint) levelArray[x/thisFrame.getBlockDimension()][y/thisFrame.getBlockDimension()];
						if(temp.isCurrent()) {
							isDefaultPlaced = false;
						}
					}
				}
				levelArray[x/thisFrame.getBlockDimension()][y/thisFrame.getBlockDimension()] = null;
				label.setIcon(null);
				label.repaint();
			}
		}
	}
	
	//Tile array for selecting tiles to place,
	//needs to be manually updated for every new tile.
	//we can set coords to 0 for all because we are only
	//using these for reference.
	private void makeTileArray() {
		tileArray = new Tile[8];
		tileArray[0] = new Dirt(0,0,levelPanel);
		tileArray[1] = new SpawnPoint(0,0,levelPanel);
		((SpawnPoint) tileArray[1]).setCurrent(true);
		tileArray[2] = new Floor(0,0,levelPanel);
		tileArray[3] = new Acid(0,0,levelPanel);
		tileArray[4] = new EndPoint(0,0,levelPanel);
		tileArray[5] = new Box(0,0,levelPanel);
		tileArray[6] = new Spikes(0,0,levelPanel);
		tileArray[7] = new DownwardSpikes(0,0,levelPanel);
	}
	
	private ImageIcon resizeTile(BufferedImage img) {
		Image imgResize = img.getScaledInstance(thisFrame.getBlockDimension(), thisFrame.getBlockDimension(), Image.SCALE_SMOOTH);
		ImageIcon icon = new ImageIcon(imgResize);
		return icon;
	}
	
	private void loadLevel() throws FileNotFoundException, NullPointerException{
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
		JComboBox levelComboBox = new JComboBox(levelNames);
		int response = JOptionPane.showConfirmDialog(null, levelComboBox, "Select a level", 
				JOptionPane.OK_CANCEL_OPTION);
		try {
			if(response == JOptionPane.OK_OPTION) {
				levelInfo info;
				info = LevelLoader.load((String)levelComboBox.getSelectedItem(),levelPanel);
				levelArray = new Tile[info.getLevel().length][info.getLevel()[0].length];
				levelArray = info.getLevel();
				isDefaultPlaced = false;
				for(int x = 0; x < levelArray.length; x++) {
					for(int y = 0; y < levelArray[0].length; y++) {
						if(levelArray[x][y] != null) {
							if(levelArray[x][y] instanceof SpawnPoint) {
								if(((SpawnPoint) levelArray[x][y]).isCurrent()) {
									isDefaultPlaced = true;
								}
							}
						}
					}
				}
			}
				paintLevel(levelPanel.getGraphics());
		} finally {
			
		}
	}
	
}
