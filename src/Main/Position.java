package Main;

public class Position {

	private int x;
	private int y;
	
	public Position(int p_x, int p_y) {
		setX(p_x);
		setY(p_y);
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof Position)) return false;
		
		if(((Position)obj).getX() == this.getX() 
		&& ((Position)obj).getY() == this.getY())
			return true;
		
		return false;
	}
	
	@Override
	public String toString() {
		return this.getX() + " : " + this.getY();
	}
}
