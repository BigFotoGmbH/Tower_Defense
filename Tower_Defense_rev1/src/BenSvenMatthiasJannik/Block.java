package BenSvenMatthiasJannik;
import java.awt.*;
@SuppressWarnings("serial")
public class Block extends Rectangle {
	public Rectangle towerSquare;
	public int towerSquareSize = 150;
	public int groundID;
	public int airID;
	public int loseTime = 100, loseFrame = 0;
	
	public int shotMob = -1;
	public boolean shoting = false;
	
	public Block (int x, int y, int width, int height, int groundID, int airID) {
		setBounds(x, y, width, height);
		towerSquare = new Rectangle(x - (towerSquareSize/2), y - (towerSquareSize/2), width + (towerSquareSize), height + (towerSquareSize));
		this.groundID = groundID; 
		this.airID = airID; 

	}
	
	public void draw(Graphics g) {
		g.drawImage(Screen.tileset_ground[groundID], x, y, width, height, null);
	
		if(airID != Values.airAir) {
			g.drawImage(Screen.tileset_air[airID], x, y, width, height, null);	
		}
	}
	
	public void physic() {
		if(shotMob != -1 && towerSquare.intersects(Screen.mob[shotMob])) {
			shoting = true;
		} else {
			shoting = false;
		}
		
		if(!shoting) {
			shoting = false;
				if(airID == Values.airTowerLaser) {
					for(int i=0;i<Screen.mob.length;i++) {
						if(Screen.mob[i].inGame) {
							if(towerSquare.intersects(Screen.mob[i])) {
								shoting = true;
								shotMob = i;
							}
						}
					}
				}
		}
		
		if(shoting) {						// Schießen
			if(loseFrame >= loseTime) {
				Screen.mob[shotMob].loseHealth(1);
				
				loseFrame = 0;
			} else {
				loseFrame += 1;
			}
			
			if(Screen.mob[shotMob].isDead()) {		// Mob tot -> Geld und Schuss Stop^^
				shoting = false;
				shotMob = -1;
				
				Screen.killed += 1;
				
				Screen.hasWon();
			}
		}
	}
	
	public void getMoney(int mobID) {  		// Geld bei Tötung
		Screen.coinage += Values.deathReward[mobID];
	}
	
	public void fight(Graphics g) {
			if(Screen.isDebug) {  			// Schussreichweite - aktiviert über isDebug in Screen Class
				if(airID == Values.airTowerLaser) {
					g.drawRect(towerSquare.x, towerSquare.y, towerSquare.width, towerSquare.height);
				}
			}
			if(shoting) {  					// Schuss-Laserstrahl
				g.setColor(new Color(255,255,0));
				g.drawLine(x + (width/2), y + (height/2), Screen.mob[shotMob].x +  (Screen.mob[shotMob].width/2), Screen.mob[shotMob].y +  (Screen.mob[shotMob].height/2));
			}	
	}
}
