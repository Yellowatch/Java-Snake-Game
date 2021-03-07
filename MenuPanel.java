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
		sp.setBounds((width / 2) - 100, (height / 4) + 100 - 30, 200, 60);
		sp.setBackground(Color.GREEN);
		sp.setBorder(BorderFactory.createBevelBorder(0));
		sp.addActionListener(new SinglePlayerListener(frame));
		add(sp);
		
		JButton mp = new JButton("MultiPlayer");
		mp.setLayout(null);
		mp.setBounds((width / 2) - 100, (height / 4) + 175 - 30, 200, 60);
		mp.setBackground(Color.GREEN);
		mp.setBorder(BorderFactory.createBevelBorder(0));
		mp.addActionListener(new MultiplayerListener(frame));
		add(mp);
		
		JButton sAI = new JButton("Single AI");
		sAI.setLayout(null);
		sAI.setBounds((width / 2) - 100, (height / 4) + 250 - 30, 200, 60);
		sAI.setBackground(Color.GREEN);
		sAI.setBorder(BorderFactory.createBevelBorder(0));
		sAI.addActionListener(new SingleAIListener(frame));
		add(sAI);
		
		JButton Astar = new JButton("A* AI");
		Astar.setLayout(null);
		Astar.setBounds((width / 2) - 100, (height / 4) + 325 - 30, 200, 60);
		Astar.setBackground(Color.GREEN);
		Astar.setBorder(BorderFactory.createBevelBorder(0));
		Astar.addActionListener(new AStarListener(frame));
		add(Astar);
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

