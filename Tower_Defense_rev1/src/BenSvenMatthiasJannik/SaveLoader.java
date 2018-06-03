package BenSvenMatthiasJannik;

import java.io.*;
import java.util.*;

public class SaveLoader {
	public void loadSave(InputStream inputStream) {
		try {
			Scanner loadScanner = new Scanner(inputStream);
			
			while(loadScanner.hasNext()) {
				Screen.killsToWin = loadScanner.nextInt();
				
				for(int y=0; y<Screen.room.block.length; y++) {
					for(int x=0; x<Screen.room.block[0].length; x++) {
						Screen.room.block[y][x].groundID = loadScanner.nextInt();
					}
				}
				
				for(int y=0; y<Screen.room.block.length; y++) {
					for(int x=0; x<Screen.room.block[0].length; x++) {
						Screen.room.block[y][x].airID = loadScanner.nextInt();						
					}
				}
			}
			loadScanner.close();
		} catch (Exception e) {}
	}
}
