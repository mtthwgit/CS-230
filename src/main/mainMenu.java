package main;


import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainMenu extends JLayeredPane{
	private static final long serialVersionUID = 7962642755482788779L;
	private ImageIcon background;
		
	private JLabel picLabel;
	
	private JButton play;
	private JButton levelBuilder;
	private JButton exit;
	
	public mainMenu(gameController frame) {
		this.setLayout(null);
		picLabel = new JLabel(); //create jlabel
		background = new ImageIcon("src/main/bob.jpeg"); //init image
		Image img = background.getImage(); //transform to img
		Image newImg = img.getScaledInstance(gameController.getWindowWidth(), gameController.getWindowHeight(), Image.SCALE_SMOOTH); //create scaled version
		ImageIcon finalImage = new ImageIcon(newImg); //transform back to image icon
		picLabel.setIcon(finalImage); //set image to label
		picLabel.setBounds(0, 0, gameController.getWindowWidth(), gameController.getWindowHeight()); //set bounds of label on the layered panel. (has to be done with layered panel.
		
		this.add(picLabel, 1); //add to position 1 on the layered panel
		
		play = new JButton("Play"); //init play button
		play.setBackground(new Color((float)0.996,(float)0.992,(float)0.871));
		play.setFocusPainted(false);
		play.setBorder(BorderFactory.createLineBorder(Color.black,2));
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.mainToGame();
			}
		});
		levelBuilder = new JButton("Level Builder"); //init levels button
		levelBuilder.setBackground(new Color((float)0.996,(float)0.992,(float)0.871));
		levelBuilder.setFocusPainted(false);
		levelBuilder.setBorder(BorderFactory.createLineBorder(Color.black,2));
		levelBuilder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.mainToBuilder();
			}
		});
		exit = new JButton("Exit"); //init exit button
		exit.setBackground(new Color((float)0.996,(float)0.992,(float)0.871));
		exit.setFocusPainted(false);
		exit.setBorder(BorderFactory.createLineBorder(Color.black,2));
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		play.setBounds((gameController.getWindowWidth()/2)-((int)(gameController.getWindowWidth()*.2)/2), 400, (int)(gameController.getWindowWidth()*.2),(int)(gameController.getWindowHeight()*.05)); //set bounds/placement
		levelBuilder.setBounds((gameController.getWindowWidth()/2)-((int)(gameController.getWindowWidth()*.2)/2), 500, (int)(gameController.getWindowWidth()*.2),(int)(gameController.getWindowHeight()*.05)); //set bounds/placement
		exit.setBounds((gameController.getWindowWidth()/2)-((int)(gameController.getWindowWidth()*.2)/2), 600, (int)(gameController.getWindowWidth()*.2),(int)(gameController.getWindowHeight()*.05)); //set bounds/placement
		
		this.add(play, 0); //add play button to 0 position (outermost position)
		this.add(levelBuilder, 0); //add levels button
		this.add(exit, 0); //add exit button
		
		this.setVisible(true);
	}

}
