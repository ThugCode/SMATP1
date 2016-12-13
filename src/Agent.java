
public class Agent extends Thread {

	private int idNumber;
	private Grid grid;
	private Position finalPosition;
	private Position currentPosition;
	private String sigle;

	public Agent(Grid p_grid, int p_idNumber, String p_sigle, Position p_pos_i, Position p_pos_f) {
		setIdNumber(p_idNumber);
		setGrid(p_grid);
		setSigle(p_sigle);
		setCurrentPosition(p_pos_i);
		setFinalPosition(p_pos_f);
	}
	
	private int facteurX() {
		return this.getCurrentPosition().getX() - this.getFinalPosition().getX();
	}
	
	private int facteurY() {
		return this.getCurrentPosition().getY() - this.getFinalPosition().getY();
	}
	
	@Override
	public void run() {
		
		while(true) {
			boolean deplacement = false;
			
			if(facteurX() != 0) {
				int increment = -facteurX()/Math.abs(facteurX());
				if(!this.grid.neightboursIsOccupated(this.currentPosition, increment, 0)) {
					this.grid.switchAgent(this.currentPosition, new Position(this.currentPosition.getX()+increment, this.currentPosition.getY()));
					deplacement = true;
				}
			}
			
			if(!deplacement && facteurY() != 0) {
				int increment = -facteurY()/Math.abs(facteurY());
				if(!this.grid.neightboursIsOccupated(this.currentPosition, 0, increment)) {
					this.grid.switchAgent(this.currentPosition, new Position(this.currentPosition.getX(), this.currentPosition.getY()+increment));
					deplacement = true;
				}
			}
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		}
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}
	
	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public Position getFinalPosition() {
		return finalPosition;
	}

	public void setFinalPosition(Position finalPosition) {
		this.finalPosition = finalPosition;
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public String getSigle() {
		return sigle;
	}

	public void setSigle(String sigle) {
		this.sigle = sigle;
	}
}
