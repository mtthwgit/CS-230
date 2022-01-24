package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame; // for JFrame
import javax.swing.JLabel;
import javax.swing.JOptionPane; // messages are displayed using JOptionPane
import javax.swing.JPanel;

import levelBuilder.levelBuilder;
import levelBuilder.levelInfo;

@SuppressWarnings("unused")
public class gameController extends JFrame{
	private static final long serialVersionUID = 6581184009482946096L;
	
	//private JFrame gameJFrame;
	private Container mainPane;
	
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int width = (int) screenSize.getWidth();
    private static int height = (int) screenSize.getHeight();
    private static int blockDim = width/15;

	public gameController() {
	
		mainMenu menu = new mainMenu(this);
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setTitle("Escape From Casa Bob");
		setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        mainPane = getContentPane();
        mainPane.setBackground(Color.WHITE);
        //IF SCREEN IS CYAN THEN WE DID SOMETHING WRONG
        setBackground(Color.BLACK);
        //BACKGROUND IMAGE
        /*
        picLabel = new JLabel(); //create jlabel
		background = new ImageIcon("src/main/bob2.jpeg"); //init image
		Image img = background.getImage(); //transform to img
		Image newImg = img.getScaledInstance(gameController.getWindowWidth(), gameController.getWindowHeight(), Image.SCALE_SMOOTH); //create scaled version
		ImageIcon finalImage = new ImageIcon(newImg); //transform back to image icon
		picLabel.setIcon(finalImage); //set image to label
		picLabel.setBounds(0, 0, gameController.getWindowWidth(), gameController.getWindowHeight()); //set bounds of label on the layered panel. (has to be done with layered panel.
		*/
        
        
        
        mainPane.add(menu);
        mainPane.setVisible(true);
        
        try {
            // Open an audio input stream.
            //URL url = this.getClass().getClassLoader().getResource("gameover.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("music.wav"));
            AudioFormat af = audioIn.getFormat();
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            DataLine.Info info = new DataLine.Info(Clip.class, af);
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
         } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } catch (LineUnavailableException e) {
            e.printStackTrace();
         }
        
    }

	
	
	public static int getWindowWidth() { return width; }
	public static int getWindowHeight() { return height; }
	public static int getBlockDimension() { return blockDim; }

	public static void main(String[] args) {
        gameController myController = new gameController();
	}
	
	public void mainToBuilder() {
		mainPane.setVisible(false);
		mainPane.remove(0);
		levelBuilder builder = new levelBuilder(this);
		builder.setBounds(0,0,width,height);
		mainPane.add(builder);
		mainPane.setVisible(true);
	}
	
	public void builderToMain() {
		mainPane.setVisible(false);
		mainPane.remove(0);
		mainMenu menu = new mainMenu(this);
		menu.setBounds(0,0,width,height);
		mainPane.add(menu);
		mainPane.setVisible(true);
	}
	
	public void mainToGame() {
		mainPane.setVisible(false);
		mainPane.remove(0);
		gameDisplay game = new gameDisplay(this);
		mainPane.add(game);
		mainPane.setVisible(true);
	}
	
	public void gameToMain() {
		mainPane.setVisible(false);
		mainPane.remove(0);
		mainMenu menu = new mainMenu(this);
		menu.setBounds(0,0,width,height);
		mainPane.add(menu);
		mainPane.setVisible(true);
	}
 
}
