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

public class GOSingleAIPanel extends JPanel {
	int applesEaten;
	int width;
	int height;
	Graphics g;
	JFrame frame;
	
	public GOSingleAIPanel(int applesEaten, int width, int height, Graphics g, JFrame frame) {
		this.applesEaten = applesEaten;
		this.width = width;
		this.height = height;
		this.g = g;
		this.frame = frame;
		
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.BLACK);
		setLayout(null);
		
		JButton againB = new JButton("Play Again?");
		againB.setLayout(null);
		againB.setBounds((width / 2) - 100, (height - (height / 4)) - 30, 200, 60);
		againB.setBackground(Color.GREEN);
		againB.setBorder(BorderFactory.createBevelBorder(0));
		againB.addActionListener(new PASingleAIListener(frame));
		add(againB);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (width - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		
		g.setColor(Color.RED);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (width - metrics2.stringWidth("Game Over"))/2, height / 2);
	}
	

}
