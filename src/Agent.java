import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Agent extends Thread {

	private int idNumber;
	private Grid grid;
	private Position finalPosition;
	private Position currentPosition;
	private Position virtualPosition;
	private String sigle;
	private ImageIcon image;

	public Agent(Grid p_grid, int p_idNumber, String p_sigle, Position p_pos_i, Position p_pos_f, ImageIcon p_image) {
		setIdNumber(p_idNumber);
		setGrid(p_grid);
		setSigle(p_sigle);
		setCurrentPosition(p_pos_i);
		setFinalPosition(p_pos_f);
		setImage(p_image);
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
		ArrayList<Position> path;

		while(true) {
			
			path = getPath();
			
			
			ArrayList<Message> messages = this.grid.getListBoxes().get(this.getIdNumber()).getListMessages();
			for(Message msg : messages) {
				if(msg.isRead())
					continue;

				Position need = null;
				if(path.size() > 0) need = path.get(0);
				Position newPos = this.grid.getNeightboorFreePosition(this.getCurrentPosition(), msg.getPositionToAvoid(), need);
				if(newPos != null && need != newPos) {
					path.add(0, newPos);
					//System.out.println(this.getSigle()+" se déplace en "+newPos.getX()+":"+newPos.getY());
					msg.setRead(true);
				}
			}
			//this.grid.getListBoxes().get(this.getIdNumber()).removeReadMessages();
			
			Position p2 = null;

			if(path.size() > 0) {
				Position p = path.get(0);
				if(path.size() > 1) 
					p2 = path.get(1);

				Agent neighbor = this.grid.moveAgent(this, p);
				if(neighbor != null) {
					int index = neighbor.getIdNumber();
					Message msg = new Message(this, neighbor, p2);
					this.grid.getListBoxes().get(index).addMessage(msg);
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

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}
}
