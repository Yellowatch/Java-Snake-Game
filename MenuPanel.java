package Game;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {
	
	int width;
	int height;
	JFrame frame;
	
	public MenuPanel(int width, int height, JFrame frame) {
		this.width = width;
		this.height = height;
		this.frame = frame;
		
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.BLACK);
		setLayout(null);
		
		JButton sp = new JButton("SinglePlayer");
		sp.setLayout(null);
		sp.setBounds((width / 2) - 100, (height / 2) - 30, 200, 60);
		sp.setBackground(Color.GREEN);
		sp.setBorder(BorderFactory.createBevelBorder(0));
		sp.addActionListener(new SinglePlayerListener(frame));
		add(sp);
		
		JButton mp = new JButton("MultiPlayer");
		mp.setLayout(null);
		mp.setBounds((width / 2) - 100, (height - (height / 4)) - 30, 200, 60);
		mp.setBackground(Color.GREEN);
		mp.setBorder(BorderFactory.createBevelBorder(0));
		mp.addActionListener(new MultiplayerListener(frame));
		add(mp);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("SNAKE", (width - metrics.stringWidth("SNAKE"))/2, height / 4);
	}
}

