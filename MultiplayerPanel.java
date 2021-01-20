package Game;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.*;

@SuppressWarnings("serial")
public class MultiplayerPanel extends JPanel implements ActionListener {

	final int SCREEN_WIDTH;
	final int SCREEN_HEIGHT;
	static final int UNIT_SIZE = 15;
	final int GAME_UNITS;
	static final int DELAY = 75;
	final int x1[];
	final int y1[];
	final int x2[];
	final int y2[];
	int bodyParts1 = 6;
	int applesEaten1;
	int bodyParts2 = 6;
	int applesEaten2;
	int appleX;
	int appleY;
	char direction1 = 'R';
	char direction2 = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	JFrame frame;
	MultiplayerKeyAdapter keyAdapter = new MultiplayerKeyAdapter();
	String winner;

	public MultiplayerPanel(JFrame frame, int w, int h) {
		SCREEN_WIDTH = w;
		SCREEN_HEIGHT = h;
		GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
		this.x1 = new int[GAME_UNITS];
		this.y1 = new int[GAME_UNITS];
		this.x2 = new int[GAME_UNITS];
		this.y2 = new int[GAME_UNITS];
		this.frame = frame;
		random = new Random();
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(keyAdapter);
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void restart() {
		bodyParts1 = 6;
		bodyParts2 = 6;
		direction1 = 'R';
		direction2 = 'R';
		applesEaten1 = 0;
		applesEaten2 = 0;
		for (int i = bodyParts1; i >= 0; i--) {
			x1[i] = 0;
			y1[i] = 0;
		}
		for (int i = bodyParts2; i >= 0; i--) {
			x2[i] = 0;
			y2[i] = 0;
		}
		startGame();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (running) {
			g.setColor(Color.WHITE);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			timer.setDelay(DELAY);
			for (int i = 0; i < bodyParts1; i++) {
				if (applesEaten1 % 10 == 0 && applesEaten1 != 0) {
					timer.setDelay(45);
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x1[i], y1[i], UNIT_SIZE, UNIT_SIZE);
				} else if (i == 0) {
					g.setColor(Color.GREEN);
					g.fillRect(x1[i], y1[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x1[i], y1[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			for (int i = 0; i < bodyParts2; i++) {
				if (applesEaten2 % 10 == 0 && applesEaten2 != 0) {
					timer.setDelay(45);
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x2[i], y2[i], UNIT_SIZE, UNIT_SIZE);
				} else if (i == 0) {
					g.setColor(Color.GREEN);
					g.fillRect(x2[i], y2[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x2[i], y2[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.RED);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			//FontMetrics metrics = getFontMetrics(g.getFont());
			//g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
			//		g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void move() {
		for (int i = bodyParts1; i > 0; i--) {
			x1[i] = x1[i - 1];
			y1[i] = y1[i - 1];
		}
		for (int i = bodyParts2; i > 0; i--) {
			x2[i] = x2[i - 1];
			y2[i] = y2[i - 1];
		}

		switch (direction1) {
		case 'U':
			y1[0] = y1[0] - UNIT_SIZE;
			break;
		case 'D':
			y1[0] = y1[0] + UNIT_SIZE;
			break;
		case 'L':
			x1[0] = x1[0] - UNIT_SIZE;
			break;
		case 'R':
			x1[0] = x1[0] + UNIT_SIZE;
			break;
		}
		
		switch (direction2) {
		case 'U':
			y2[0] = y2[0] - UNIT_SIZE;
			break;
		case 'D':
			y2[0] = y2[0] + UNIT_SIZE;
			break;
		case 'L':
			x2[0] = x2[0] - UNIT_SIZE;
			break;
		case 'R':
			x2[0] = x2[0] + UNIT_SIZE;
			break;
		}
	}

	public void checkApple() {
		if ((x1[0] == appleX) && (y1[0] == appleY)) {
			bodyParts1++;
			applesEaten1++;
			newApple();
		} 
		if ((x2[0] == appleX) && (y2[0] == appleY)) {
			bodyParts2++;
			applesEaten2++;
			newApple();
		}
	}

	public void checkCollisions() {
		for (int i = bodyParts1; i > 0; i--) {
			if ((x1[0] == x1[i]) && (y1[0] == y1[i])) {
				running = false;
				winner = "P2";
			}
		}
		if (x1[0] < 0) {
			running = false;
			winner = "P2";
		}
		if (x1[0] > SCREEN_WIDTH) {
			running = false;
			winner = "P2";
		}
		if (y1[0] < 0) {
			running = false;
			winner = "P2";
		}
		if (y1[0] > SCREEN_HEIGHT) {
			running = false;
			winner = "P2";
		}

		for (int i = bodyParts2; i > 0; i--) {
			if ((x2[0] == x2[i]) && (y2[0] == y2[i])) {
				running = false;
				winner = "P1";
			}
		}
		if (x2[0] < 0) {
			running = false;
			winner = "P1";
		}
		if (x2[0] > SCREEN_WIDTH) {
			running = false;
			winner = "P1";
		}
		if (y2[0] < 0) {
			running = false;
			winner = "P1";
		}
		if (y2[0] > SCREEN_HEIGHT) {
			running = false;
			winner = "P1";
		}

		for (int i = bodyParts2; i > 0; i--) {
			if ((x1[0] == x2[i]) && (y1[0] == y2[i])) {
				running = false;
				winner = "P2";
			}
		}
		for (int i = bodyParts1; i > 0; i--) {
			if ((x2[0] == x1[i]) && (y2[0] == y1[i])) {
				running = false;
				winner = "P1";
			}
		}

		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		((MyFrame) frame).gameOverMultiplayer(new GOMultiplayerPanel(applesEaten1, applesEaten2, SCREEN_WIDTH, SCREEN_HEIGHT, frame, winner));
	}

	public void actionPerformed(ActionEvent event) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MultiplayerKeyAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction2 != 'R') {
					direction2 = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction2 != 'L') {
					direction2 = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction2 != 'D') {
					direction2 = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction2 != 'U') {
					direction2 = 'D';
				}
				break;
			case KeyEvent.VK_W:
				if (direction1 != 'D') {
					direction1 = 'U';
				}
				break;
			case KeyEvent.VK_D:
				if (direction1 != 'L') {
					direction1 = 'R';
				}
				break;
			case KeyEvent.VK_A:
				if (direction1 != 'R') {
					direction1 = 'L';
				}
				break;
			case KeyEvent.VK_S:
				if (direction1 != 'U') {
					direction1 = 'D';
				}
				break;
			}
			
		}
	}

	
}

