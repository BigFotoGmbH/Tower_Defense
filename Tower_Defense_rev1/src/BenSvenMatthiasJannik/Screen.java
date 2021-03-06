package BenSvenMatthiasJannik;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Screen extends JPanel implements Runnable {
	public static Screen screen; 
	
	public Thread thread = new Thread(this); 
	
	public static final int SPAWNTIME = 3000;
	
	//Spawn timer
	public int spawnTime = SPAWNTIME, spawnFrame = 0;
	
	public static Image[] tileset_ground = new Image [100];
	public static Image[] tileset_air = new Image [3]; //3 verschiedene Bilder
	public static Image[] tileset_res = new Image[5];
	public static Image[] tileset_mob = new Image[1]; //1 Mob
	
	public static boolean started = false;
	public static boolean isFirst = true;
	public static boolean isDebug = true; //Tower Rahmen - Schussreichweite
	public static boolean isWin = false;  //Win
	
	/**Mouse coursor click last pos*/
	public static Point mse = new Point(0,0);
	
	public static int myWidth, myHeight;
	public static int coinage = 100, health = 50;
	public static int killed = 0, killsToWin = 0, level = 1, maxLevel = 3;
	public static int winTime = 4000, winFrame = 0;
	
	public static Room room;
	public static SaveLoader save;
	public static Store store;
	
	public static Mob mob[] = new Mob[100];
	
	
	public Screen(Frame frame) { 
		addMouseListener(new KeyHandel());
		addMouseMotionListener(new KeyHandel());
	
		screen = this;
	}
	
	public static void hasWon() {
		if(killed == killsToWin) {
			isWin = true;
			killed = 0;
			coinage = 0;
		}
	}
	
	public void define() {
		try {
		room = new Room();
		save = new SaveLoader();
		store = new Store();
		
		coinage = 10;
		health = 50;
		
		spawnTime = SPAWNTIME;
		
		for(int i=0;i<mob.length;i++) {
			mob[i] = new Mob();
			//mob[i].spawnMob(0);
		}
		
		for(int i=0; i<tileset_ground.length; i++) {
			tileset_ground[i] = ImageIO.read(Frame.class.getResourceAsStream("Res/tileset_ground.png"));         //Bild vom Boden wird geladen
			tileset_ground[i] = createImage(new FilteredImageSource(tileset_ground[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		for(int i=0; i<tileset_air.length; i++) {
			tileset_air[i] = ImageIO.read(Frame.class.getResourceAsStream("Res/tileset_air.png"));         //Bild vom Himmel und Objekten wird geladen
			tileset_air[i] = createImage(new FilteredImageSource(tileset_air[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		
		tileset_res[0] = ImageIO.read(Frame.class.getResourceAsStream("Res/cell.png"));
		tileset_res[1] = ImageIO.read(Frame.class.getResourceAsStream("Res/herz.png"));
		tileset_res[2] = ImageIO.read(Frame.class.getResourceAsStream("Res/coin.png"));
		
		tileset_mob[0] = ImageIO.read(Frame.class.getResourceAsStream("Res/mob.png"));
		
		save.loadSave(Frame.class.getResourceAsStream("Save/Mission" + level + ".sbm"));  //l�dt level
		
		//Erzwinge neu zeichnen
		repaint();
		
		//Starte Thread wenn nicht bereits laufend
		if(started == false) {
			thread.start();
			started = true;	
		}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g) {
		if(isFirst) {
			
			myWidth = getWidth();
			myHeight = getHeight();
			define();
			isFirst = false;
		}
		g.setColor(new Color(70, 70, 70));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(new Color(0, 0, 0));
		g.drawLine(room.block[0][0].x-1, 0, room.block[0][0].x -1,  room.block[room.worldHeight-1][0].y + room.blockSize); //Outline der Linken Seite
		g.drawLine(room.block[0][room.worldWidth-1].x + room.blockSize, 0, room.block[0][room.worldWidth-1].x + room.blockSize,  room.block[room.worldHeight-1][0].y + room.blockSize); //Outline der rechten Seite
		g.drawLine(room.block[0][0].x, room.block[room.worldHeight-1][0].y + room.blockSize, room.block[0][room.worldWidth-1].x + room.blockSize, room.block[room.worldHeight-1][0].y + room.blockSize); // Outline unten
		
		room.draw(g); 			//zeichnet raum
		
		for(int i=0;i<mob.length;i++) {
			if(mob[i].inGame) {
				mob[i].draw(g);
			}
		}
		
		store.draw(g);			//zeichnet Shop Leiste
		
		if(health < 1) {
			g.setColor(new Color(240, 20,20 ,20));
			g.fillRect(0, 0, myWidth, myHeight);
			g.setColor(new Color (255, 255, 255));
			g.setFont(new Font ("Courier New", Font.BOLD, 70));
			g.drawString("Game Over", 150, 50);
		}
		
		if(isWin) {				// Sieg Display
			g.setColor(new Color (255,255,255));
			g.fillRect( 0, 0, getWidth(), getHeight());
			g.setColor(new Color (0, 0, 0));
			g.setFont(new Font ("Courier New", Font.BOLD, 14));
			if(level == maxLevel ) {
				g.drawString("!!!SIEG!!! Du hast alle Level erfolgreich geschafft ;-) ", 10, 20);
			} else {
			g.drawString("Gewonnen ;-) Warte f�r das n�chste Level...", 10, 20);
			}
		}
	}
	
	public void mobSpawner() 
	{
	    if(spawnFrame >= spawnTime) {
	    	for(int i=0;i<mob.length;i++) {
	    		if(!mob[i].inGame) {							// ! hinzugef�gt TODO
	    			mob[i].spawnMob(Values.mobGreeny);
	    			if(spawnTime > 800) spawnTime -= 100;
	    			break;
	    		}
	    	}
	    	spawnFrame = 0;
	    }else {
	    	spawnFrame += 1;
	    	
	    }
	}
	

	public void run() {                         // !!!RUN!!!
		System.out.println("Started Physics Thread");
		while(true) { 
			if(health > 0 && !isWin) {
				room.physic();
				mobSpawner();
				for(int i=0;i<mob.length;i++) {
					if(mob[i].inGame) {
						mob[i].physic();
					}
				}
			} else {
				if(isWin) {
					if(winFrame >= winTime) {
						if(level == maxLevel) {
							System.exit(0);
						} else {
		
							level += 1;
							define();
							isWin = false;
						}	
							winFrame = 0;
					} else {
						winFrame += 1;
					}
				}
			}
			repaint();
		try {
				Thread.sleep(1);
		} 
		catch(Exception e) { }
		}
	
	}
}
