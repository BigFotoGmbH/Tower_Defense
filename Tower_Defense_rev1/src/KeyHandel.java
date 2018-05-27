import java.awt.Point; 
import java.awt.event.*;

public class KeyHandel implements MouseMotionListener, MouseListener {

	
	public void mouseClicked(MouseEvent e) {
		Screen.mse = new Point((e.getX()) + ((Frame.size.width - Screen.myWidth)/2), (e.getY() + ((Frame.size.height)-(Screen.myHeight)/2)));
	}

	
	public void mouseEntered(MouseEvent e) {
	}

	
	public void mouseExited(MouseEvent e) {		
	}

	
	public void mousePressed(MouseEvent e) {		
	}

	
	public void mouseReleased(MouseEvent e) {		
	}

	
	public void mouseDragged(MouseEvent e) {
		Screen.mse = new Point((e.getX()) + ((Frame.size.width - Screen.myWidth)/2), (e.getY() + ((Frame.size.height)-(Screen.myHeight)/2)));
	}

	
	public void mouseMoved(MouseEvent e) {		
		Screen.mse = new Point((e.getX()) - ((Frame.size.width - Screen.myWidth)/2), (e.getY() - ((Frame.size.height)-(Screen.myHeight)/2)));
	}
}
