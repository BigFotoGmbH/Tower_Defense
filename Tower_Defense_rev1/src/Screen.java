import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

public class Screen extends JPanel implements Runnable {
	public static Screen screen; 
	
	public Thread thread = new Thread(this); 
	
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
	public static Save save;
	public static Store store;
	
	public static Mob mob[] = new Mob[100];
	
	
	public Screen(Frame frame) { 
		frame.addMouseListener(new KeyHandel());
		frame.addMouseMotionListener(new KeyHandel());
	
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
		
		if(started = false) {
			thread.start();
			started = true;	
		}
		
		room = new Room();
		save = new Save();
		store = new Store();
		
		coinage = 10;
		health = 50;
		
		for(int i=0;i<mob.length;i++) {
			mob[i] = new Mob();
			mob[i].spawnMob(0);
		}
		
		for(int i=0; i<tileset_ground.length; i++) {
			tileset_ground[i] = new ImageIcon("res/tileset_ground.png").getImage();         //Bild vom Boden wird geladen
			tileset_ground[i] = createImage(new FilteredImageSource(tileset_ground[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		for(int i=0; i<tileset_air.length; i++) {
			tileset_air[i] = new ImageIcon("res/tileset_air.png").getImage();         //Bild vom Himmel und Objekten wird geladen
			tileset_air[i] = createImage(new FilteredImageSource(tileset_air[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		
		tileset_res[0] = new ImageIcon("res/cell.png").getImage();
		tileset_res[1] = new ImageIcon("res/herz.png").getImage();
		tileset_res[2] = new ImageIcon("res/coin.png").getImage();
		
		tileset_mob[0] = new ImageIcon("res/mob.png").getImage();
		
		save.loadSave(new File("Save/Mission" + level + ".sbm"));  //lädt level
		//Erzwinge neu zeichnen
		repaint();
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
			g.setFont(new Font ("Courier New", Font.BOLD, 14));
			g.drawString("Game Over", 10, 20);
		}
		
		if(isWin) {				// Sieg Display
			g.setColor(new Color (255,255,255));
			g.fillRect( 0, 0, getWidth(), getHeight());
			g.setColor(new Color (0, 0, 0));
			g.setFont(new Font ("Courier New", Font.BOLD, 14));
			if(level == maxLevel ) {
				g.drawString("!!!SIEG!!! Du hast alle Level erfolgreich geschafft ;-) ", 10, 20);
			} else {
			g.drawString("Gewonnen ;-) Warte für das nächste Level...", 10, 20);
			}
		}
	}
	
		public int spawnTime = 2000, spawnFrame = 0;
		public void mobSpawner() {
		    if(spawnFrame >= spawnTime) {
		    	for(int i=0;i<mob.length;i++) {
		    		if(mob[i].inGame) {
		    			mob[i].spawnMob(Values.mobGreeny);
		    			break;
		    		}
		    	}
		    	spawnFrame = 0;
		    }else {
		    	spawnFrame += 1;
		    	
		    }
		}
	

	public void run() {                         // !!!RUN!!!
		while(true) { 
			if(isFirst && health > 0 && !isWin) {
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
