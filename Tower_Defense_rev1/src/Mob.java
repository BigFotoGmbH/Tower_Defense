import java.awt.*;

@SuppressWarnings("serial")
public class Mob extends Rectangle {
	public int xC, yC;
	public int health;
	public int healthSpace = 3;
	public int healthHeight = 6;
	public int mobSize = 52;
	public int mobWalk = 0;
	public int upward = 0, downward =1, right = 2, left = 3;
	public int direction = right;
	public int mobID = Values.mobAir;
	public boolean inGame = false;
	public boolean hasUpward = false;
	public boolean hasDownward = false;
	public boolean hasLeft = false;
	public boolean hasRight = false;
	
	public Mob() {
		super(26, 26);
	}
	
	public void spawnMob(int mobID)
	{
		for(int y=0;y<Screen.room.block.length;y++) {
			if(Screen.room.block[y][0].groundID == Values.groundRoad) {
				setBounds(Screen.room.block[y][0].x, Screen.room.block[y][0].y, mobSize, mobSize);
				xC = 0;
				yC = y;
			}
		}
		this.mobID = mobID;
		this.health = mobSize;
		
		inGame = true;
	}
	
	public void deleteMob() { 					//löscht Mob & gibt Geld
		inGame = false;
		direction = right;
		mobWalk = 0;
		
		Screen.room.block[0][0].getMoney(mobID);
	}
	
	public void looseHealth() {
		Screen.health -= 10;
	}
	
	public int walkFrame = 0, walkSpeed = 30;
	public void physic() {
			if(walkFrame >= walkSpeed) 
			{
				//Damage to the player
				if(Screen.room.block[yC][xC].airID == Values.airCave)
				{
					looseHealth();
					inGame = false;
					direction = right;
					mobWalk = 0;
				}
				
				if(direction == right) {
				x += 1;
			 }else if (direction == upward){
				y -= 1;
			 }else if (direction == downward){
				 y += 1;
			 }
			 else if (direction == left){
				 x -= 1;
			 }
				mobWalk += 1;
				
				if(mobWalk == Screen.room.blockSize) {
					if(direction == right) {
						xC +=1;
						hasRight = true;
					 }else if (direction == upward){
						 yC -= 1;
						 hasUpward = true;
					 }else if (direction == downward){
						 yC += 1;
					 }else if (direction == left){
						xC -= 1;
						hasLeft = true;
					  }
					if(!hasUpward) {
						try {
							if(Screen.room.block[yC+1][xC].groundID == Values.groundRoad) {
								direction = downward;
								
							}
						}catch(Exception e) {}
						
					}
						
						if(!hasDownward) {
							try {
								if(Screen.room.block[yC-1][xC].groundID == Values.groundRoad && !(direction == downward)) {
									direction = upward;
									
								}
							}catch(Exception e) {}
						}	
							if(!hasLeft) {
								try {
									if(Screen.room.block[yC][xC+1].groundID == Values.groundRoad) {
										direction = right;
										
									}
								}catch(Exception e) {}
							}		
								if(!hasRight) {
									try {
										if(Screen.room.block[yC][xC-1].groundID == Values.groundRoad) {
											direction = left;
											
										}
									}catch(Exception e) {}
								}	
					
				hasUpward = false;
				hasDownward = false;
				hasLeft = false;
				hasRight = false;
				mobWalk = 0;
	}
				walkFrame = 0;
			}else {
				walkFrame += 1;
			}
	}
	
	public void loseHealth(int amo) {
		health -= amo;
			
		checkDeath();
	}
	
	public void checkDeath() {
		if(health == 0) {
			deleteMob();
		}
	}
	
	public boolean isDead() {
		return !inGame;
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(Screen.tileset_mob[Values.mobGreeny], x, y, width, height, null);

		g.setColor(new Color(180, 50, 50));
		g.fillRect(x, y - (healthSpace + healthHeight), width, healthHeight);

		g.setColor(new Color(50, 180, 50));
		g.fillRect(x, y - (healthSpace + healthHeight), health, healthHeight);

		g.setColor(new Color(0, 0, 0));
		g.drawRect(x, y - (healthSpace + healthHeight), health - 1, healthHeight - 1);
	}
}