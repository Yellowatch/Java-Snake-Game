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

@SuppressWarnings("serial")
public class GOMultiplayerPanel extends JPanel {

	int applesEaten1;
	int applesEaten2;
	int width;
	int height;
	JFrame frame;
	String winner;
	Voice voice = new Voice();
	
	public GOMultiplayerPanel(int applesEaten1, int applesEaten2, int width, int height, JFrame frame, String winner) {
		voice.playTwoPlayer();
		
		this.applesEaten1 = applesEaten1;
		this.applesEaten2 = applesEaten2;
		this.width = width;
		this.height = height;
		this.frame = frame;
		this.winner = winner;
		
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.BLACK);
		setLayout(null);
		
		JButton againB = new JButton("Play Again?");
		againB.setLayout(null);
		againB.setBounds((width / 2) - 100, (height - (height / 4)) - 30, 200, 60);
		againB.setBackground(Color.GREEN);
		againB.setBorder(BorderFactory.createBevelBorder(0));
		againB.addActionListener(new PlayAgainMultiplayerListener(frame));
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
		g.drawString("P1 Score: " + applesEaten1, (width - metrics1.stringWidth("Score: " + applesEaten1))/2, g.getFont().getSize());
		g.drawString("P2 Score: " + applesEaten2, (width - metrics1.stringWidth("Score: " + applesEaten2))/2, g.getFont().getSize() + 40);
		
		g.setColor(Color.GREEN);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Winner: " + winner, (width - metrics2.stringWidth("Winner: " + winner))/2, height / 2);
	}

}

