import java.util.ArrayList;

public class Agent extends Thread {

	private int idNumber;
	private Grid grid;
	private Position finalPosition;
	private Position currentPosition;
	private Position virtualPosition;
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

	private int virtualFacteurX() {
		return this.getVirtualPosition().getX() - this.getFinalPosition().getX();
	}

	private int virtualFacteurY() {
		return this.getVirtualPosition().getY() - this.getFinalPosition().getY();
	}

	private ArrayList<Position> getPath() {
		this.setVirtualPosition(new Position(this.currentPosition.getX(), this.currentPosition.getY()));
		ArrayList<Position> positions = new ArrayList<Position>();

		while(virtualFacteurX() != 0) {
			int increment = -virtualFacteurX()/Math.abs(virtualFacteurX());
			this.virtualPosition.setX(this.virtualPosition.getX() + increment);

			positions.add(new Position(this.virtualPosition.getX(), this.virtualPosition.getY()));
		}

		while(virtualFacteurY() != 0) {
			int increment = -virtualFacteurY()/Math.abs(virtualFacteurY());
			this.virtualPosition.setY(this.virtualPosition.getY() + increment);

			positions.add(new Position(this.virtualPosition.getX(), this.virtualPosition.getY()));
		}

		return positions;
	}

	@Override
	public void run() {
		ArrayList<Position> path = getPath();
		int i = 0;

		while(true) {
			if(i < path.size()) {
				Position p = path.get(i);
				
				if(!this.grid.isOccupated(p)) {
					this.grid.switchAgent(this.currentPosition, p);
					i++;
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

	public Position getVirtualPosition() {
		return virtualPosition;
	}

	public void setVirtualPosition(Position virtualPosition) {
		this.virtualPosition = virtualPosition;
	}

	public String getSigle() {
		return sigle;
	}

	public void setSigle(String sigle) {
		this.sigle = sigle;
	}
}
