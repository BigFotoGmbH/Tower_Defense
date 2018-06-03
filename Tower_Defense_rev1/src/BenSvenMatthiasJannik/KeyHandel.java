package BenSvenMatthiasJannik;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class KeyHandel implements MouseMotionListener, MouseListener
{
	static int mouseButton = -1;

	@Override
	public void mouseClicked(MouseEvent e)
	{
		Screen.mse = e.getPoint();
		//Screen.mse = new Point((e.getX()) + ((Frame.size.width - Screen.myWidth)/2), (e.getY() + ((Frame.size.height)-(Screen.myHeight)/2)));
		System.out.println("Click auf (" + Screen.mse.getX() + "|" + Screen.mse.getY() + ")"); // DEBUG
		Screen.screen.repaint();
		Screen.store.click(e.getButton());
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		Screen.mse = e.getPoint();	
		Screen.screen.repaint();
		Screen.store.click(e.getButton());
	}
}
