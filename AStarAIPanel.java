package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import javax.swing.*;

public class AStarAIPanel extends JPanel implements ActionListener  {

	final int SCREEN_WIDTH;
	final int SCREEN_HEIGHT;
	static final int UNIT_SIZE = 25;
	final int GAME_UNITS;
	static final int DELAY = 45;
	final int x[];
	final int y[];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	JFrame frame;
	int xDistance;
	int yDistance;
	int hCost;
	int numDirections = 0;
	char directions[];
	int count = 0;
	int gCost;
	
	public AStarAIPanel(JFrame frame, int w, int h) {
		SCREEN_WIDTH = w;
		SCREEN_HEIGHT = h;
		GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
		this.x = new int[GAME_UNITS];
		this.y = new int[GAME_UNITS];
		this.frame = frame;
		random = new Random();
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setBackground(Color.BLACK);
		setFocusable(true);
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void restart() {
		bodyParts = 6;
		direction = 'R';
		applesEaten = 0;
		for (int i = bodyParts; i >= 0; i--) {
			x[i] = 0;
			y[i] = 0;
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
			for (int i = 0; i < bodyParts; i++) {
				if (applesEaten % 10 == 0 && applesEaten != 0) {
					timer.setDelay(45);
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else if (i == 0) {
					g.setColor(Color.GREEN);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.RED);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}
		
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;	
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;	
		fixApple();

		System.out.println(appleX + " , " + appleY); // 575 it stops working

		List<Node> path = aStar();
		if (path == null) {
			numDirections = -1;
			return;
		}
		numDirections = path.size();
		directions = new char[numDirections];
		for (int i = 0; i < numDirections; i++) {
			directions[i] = path.get(i).getDirection();
		}
	}
	
	private void fixApple() {
		for(int i = bodyParts; i>0; i--) {
			if(appleX == x[i] && appleY == y[i]) {
				newApple();
			}
		}
	}
	
	public void move() {
		if (numDirections != -1) {
			direction = directions[numDirections - 1];
			numDirections--;
		}
		
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		for(int i = bodyParts; i>0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		if(x[0] < 0) {
			running = false;
		}
		if(x[0] >= SCREEN_WIDTH) {
			running = false;
		}
		if (y[0] < 0) {
			running = false;
		}
		if (y[0] >= SCREEN_HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		((MyFrame) frame).gameOverAStar(new GOAStarPanel(applesEaten, SCREEN_WIDTH, SCREEN_HEIGHT, g, frame));
	}
	
	public void actionPerformed(ActionEvent event) {
		if(running) {
			if (numDirections == -1) {
				pathFinder();
			}
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	private boolean isBlocked(char d, int x, int y) { 
		if (d == 'R' ) {
			if (x >= SCREEN_WIDTH) {
				return true;
			}
			for (int i = bodyParts; i > 0; i--) {
				if ((x == this.x[i]) && (y == this.y[i])) {
					return true;
				}
			}
		} else if (d == 'L') {
			if (x < 0) {
				return true;
			}
			for (int i = bodyParts; i > 0; i--) {
				if ((x == this.x[i]) && (y == this.y[i])) {
					return true;
				}
			}
		} else if (d == 'D') {
			if (y >= SCREEN_WIDTH) {
				return true;
			}
			for (int i = bodyParts; i > 0; i--) {
				if ((x == this.x[i]) && (y == this.y[i])) {
					return true;
				}
			}
		} else {
			if (y < 0) {
				return true;
			}
			for (int i = bodyParts; i > 0; i--) {
				if ((x == this.x[i]) && (y == this.y[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	private List<Node> aStar() {

		List<Node> parents = new ArrayList<Node>();
		PriorityQueue<Node> open = new PriorityQueue<Node>();
		List<Node> closed = new ArrayList<Node>();
		
		count = 0;
		gCost = 0;
		Node startNode = new Node(x[0], y[0], gCost, findHCost(x[0], y[0]));
		startNode.setDirection(direction);
		Node goalNode = new Node(appleX, appleY, findHCost(x[0], y[0]), 0);

		open.add(startNode);
		
		while (!open.isEmpty()) { 
			
			count++;
			
			Node current = open.poll();
			current.close();
			closed.add(current);
			
			if (count > (SCREEN_WIDTH / UNIT_SIZE) * (SCREEN_HEIGHT / UNIT_SIZE) * 10) {
				System.out.println("Couldnt find path");
				return null;
			}
			
			if (current.same(goalNode)) {
				//backtrack and create parents list
				boolean finished = false;
				Node n = current;
				while (!finished) {
					parents.add(n);
					n = n.getParent();
					if (n.getParent() == null) {
						finished = true;
					}
				}
				return parents;
			}
			
			// check neighbours
			for (int i = 0; i < 3; i++) {
				
				if (i == 0) {
					gCost = 10; // if current direction
				} else {
					gCost = 14; // if change direction, costs more
				}

				boolean exists = false;
				Node n;
				if (i == 0) {
					if (current.getDirection() == 'R') { // Continue Right
						// CHECK IF BLOCKED
						if (!isBlocked(current.getDirection(), current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'L') { // Continue Left
						if (!isBlocked(current.getDirection(), current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'D') { // Continue Down
						if (!isBlocked(current.getDirection(), current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else { // Continue Up
						if(!isBlocked(current.getDirection(), current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					}
				} else if (i == 1) {
					if (current.getDirection() == 'R') { // Turn Down
						if(!isBlocked('D', current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'L') { // Turn Up
						if(!isBlocked('U', current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'D') { // Turn Left
						if(!isBlocked('L', current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else { // Turn Right
						if(!isBlocked('R', current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					}
				} else {
					if (current.getDirection() == 'R') { // Turn Up
						if(!isBlocked('U', current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'L') { // Turn Down
						if(!isBlocked('D', current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'D') { // Turn Right
						if(!isBlocked('R', current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else { // Turn Left
						if(!isBlocked('L', current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					}
				}
				
				if (exists && n.isClosed()) {
					continue;
				}
				
				if (n.getFCost() <= current.getFCost() || !open.contains(n)) {
					n.setParent(current);
					if (!open.contains(n)) {
						n.setgCost(n.getParent().getgCost() + gCost);
						open.add(n);
					}
				}
			}
		}
		return null;
	}
	
	private int findHCost(int xAxis, int yAxis) {
		hCost = 0;
		xDistance = Math.abs((appleX - xAxis) / UNIT_SIZE);
		yDistance = Math.abs((appleY - yAxis) / UNIT_SIZE);
		if (yDistance != 0) {
			hCost = 4;
		}
		hCost += (xDistance * 10) + (yDistance * 10);
		return hCost;
	}
	
	private void pathFinder() {
		int hCostA = 0;
		int hCostB = 0;
		int hCostC = 0;
		boolean blocked = false;
		int fCostA = 999999999;
		int fCostB = 999999999;
		int fCostC = 999999999;
		
		switch(direction) {
		case 'U':
			hCostA = 0;
			hCostB = 0;
			hCostC = 0;
			
			// If space to go up
			if (y[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for(int i = bodyParts; i>0; i--) {
					if((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going up
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostA = 4;
					}
					hCostA+= (xDistance * 10) + (yDistance * 10);
					fCostA = hCostA + 10;
				}
				blocked = false;
			}
			
			// If space to go left
			if(x[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for(int i = bodyParts; i>0; i--) {
					if((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going left
					xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostB = 4;
					}
					hCostB+= (xDistance * 10) + (yDistance * 10);
					fCostB = hCostB + 14;
				}
				blocked = false;
			}
			
			// If space to go right
			if(x[0] + UNIT_SIZE < SCREEN_WIDTH) {
				// If no body parts blocking
				for(int i = bodyParts; i>0; i--) {
					if((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going right
					xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostC = 4;
					}
					hCostC+= (xDistance * 10) + (yDistance * 10);
					fCostC = hCostC + 14;
				}
				blocked = false;
			}
			
			if(fCostA <= fCostB && fCostA <= fCostC) {
				direction = 'U';
			} else if (fCostB < fCostA && fCostB <= fCostC) {
				direction = 'L';
			} else if(fCostC < fCostB && fCostC < fCostA) {
				direction = 'R';
			}
			fCostA = 999999999;
			fCostB = 999999999;
			fCostC = 999999999;
			
			break;
		
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		case 'D':
			hCostA = 0;
			hCostB = 0;
			hCostC = 0;
			
			// If space to go down
			if (y[0] + UNIT_SIZE < SCREEN_HEIGHT) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going down
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostA = 4;
					}
					hCostA += (xDistance * 10) + (yDistance * 10);
					fCostA = hCostA + 10;
				}
				blocked = false;
			}
			
			// If space to go left
			if (x[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going left
					xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostB = 4;
					}
					hCostB += (xDistance * 10) + (yDistance * 10);
					fCostB = hCostB + 14;
				}
				blocked = false;
			}
			
			// If space to go right
			if (x[0] + UNIT_SIZE < SCREEN_WIDTH) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going right
					xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostC = 4;
					}
					hCostC += (xDistance * 10) + (yDistance * 10);
					fCostC = hCostC + 14;
				}
				blocked = false;
			}
			
			if (fCostA <= fCostB && fCostA <= fCostC) {
				direction = 'D';
			} else if (fCostB < fCostA && fCostB <= fCostC) {
				direction = 'L';
			} else if (fCostC < fCostB && fCostC < fCostA) {
				direction = 'R';
			}
			fCostA = 999999999;
			fCostB = 999999999;
			fCostC = 999999999;
			
			break;
		
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		case 'L':
			hCostA = 0;
			hCostB = 0;
			hCostC = 0;
			
			// If space to go left
			if (x[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going left
					xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostA = 4;
					}
					hCostA += (xDistance * 10) + (yDistance * 10);
					fCostA = hCostA + 10;
				}
				blocked = false;
			}
			
			// If space to go down
			if (y[0] + UNIT_SIZE < SCREEN_HEIGHT) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going down
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostB = 4;
					}
					hCostB += (xDistance * 10) + (yDistance * 10);
					fCostB = hCostB + 14;
				}
				blocked = false;
			}
			
			// If space to go up
			if (y[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going up
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostC = 4;
					}
					hCostC += (xDistance * 10) + (yDistance * 10);
					fCostC = hCostC + 14;
				}
				blocked = false;
			}
			
			if (fCostA <= fCostB && fCostA <= fCostC) {
				direction = 'L';
			} else if (fCostB < fCostA && fCostB <= fCostC) {
				direction = 'D';
			} else if (fCostC < fCostB && fCostC < fCostA) {
				direction = 'U';
			}

			fCostA = 999999999;
			fCostB = 999999999;
			fCostC = 999999999;
			
			break;
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		case 'R':
			hCostA = 0;
			hCostB = 0;
			hCostC = 0;
			
			// If space to go right
			if (x[0] + UNIT_SIZE < SCREEN_WIDTH) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going right
					xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostA = 4;
					}
					hCostA += (xDistance * 10) + (yDistance * 10);
					fCostA = hCostA + 10;
				}
				blocked = false;
			}
			
			// If space to go down
			if (y[0] + UNIT_SIZE < SCREEN_HEIGHT) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going down
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostB = 4;
					}
					hCostB += (xDistance * 10) + (yDistance * 10);
					fCostB = hCostB + 14;
				}
				blocked = false;
			}
			
			// If space to go up
			if (y[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going up
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostC = 4;
					}
					hCostC += (xDistance * 10) + (yDistance * 10);
					fCostC = hCostC + 14;
				}
				blocked = false;
			}
			
			if (fCostA <= fCostB && fCostA <= fCostC) {
				direction = 'R';
			} else if (fCostB < fCostA && fCostB <= fCostC) {
				direction = 'D';
			} else if (fCostC < fCostB && fCostC < fCostA) {
				direction = 'U';
			}

			fCostA = 999999999;
			fCostB = 999999999;
			fCostC = 999999999;
			
			break;
		}
	}
}
