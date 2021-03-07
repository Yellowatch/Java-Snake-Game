package Game;

public class Node implements Comparable<Node> {
	
	int xAxis;
	int yAxis;
	//int width;
	int gCost = 0;
	int hCost;
	int fCost;
	Node parent;
	boolean walkable;
	char direction = 'x';
	boolean Closed = false;

	
	public Node(int xAxis, int yAxis, int gCost, int hCost) {
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.gCost = gCost;
		this.hCost = hCost;
		calcFCost();
	}
	
	public char getDirection() {
		return direction;
	}

	public void setDirection(char direction) {
		this.direction = direction;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		if (direction == 'x') {
			if (parent.getxAxis() - this.xAxis > 0) {
				direction = 'L';
			} else if (parent.getxAxis() - this.xAxis < 0) {
				direction = 'R';
			} else {
				if (parent.getyAxis() - this.yAxis > 0) {
					direction = 'U';
				} else {
					direction = 'D';
				}
			}
		}
		this.parent = parent;
	}
	
	public void setgCost(int gCost) {
		this.gCost = gCost;
		calcFCost();
	}
	
	private void calcFCost() {
		fCost = gCost + hCost;
	}
	
	public int getFCost() {
		return fCost;
	}

	@Override
	public int compareTo(Node o) {
		if (this.fCost > o.fCost)
			return 1;
		else if (this.fCost < o.fCost)
			return -1;
		
		return 0;
	}

	public int getxAxis() {
		return xAxis;
	}

	public int getyAxis() {
		return yAxis;
	}

	public boolean isClosed() {
		return Closed;
	}
	
	public void close() {
		Closed = true;
	}
	
	public boolean same(Node o) {
		if (this.xAxis == o.xAxis && this.yAxis == o.yAxis) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
	    // self check
	    if (this == o)
	        return true;
	    // null check
	    if (o == null)
	        return false;
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;
	    Node node = (Node) o;
	    // field comparison
	    return this.xAxis == node.xAxis && this.yAxis == node.yAxis;
	}

	public int getgCost() {
		return gCost;
	}

}
