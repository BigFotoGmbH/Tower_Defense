package BenSvenMatthiasJannik;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	public static String title = "Tower Defence Xtreme";
	public static Dimension size = new Dimension(1280, 720);
	
	public Frame () {
		setTitle(title);
		setSize(size);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
	}
	
	public void init() {
		setLayout(new GridLayout (1, 1, 0, 0));
		Screen screen = new Screen(this);
		//Entferne Windows Leiste: setUndecorated(true);
		add(screen);
	}
	 
	public static void main(String args[]) {
		System.out.println("Working dir: " + Frame.class.getResource(""));
		Frame frame = new Frame();
		frame.setVisible(true);
	}
}
