package Main;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import Message.Message;

public class Agent extends Thread {

	private int idNumber;
	public static Grid GRID;
	private Position finalPosition;
	private Position currentPosition;
	private Position virtualPosition;
	private Position forbiddenPostion;
	private String sigle;
	private ImageIcon image;

	public Agent(int p_idNumber, String p_sigle, Position p_pos_i, Position p_pos_f, ImageIcon p_image) {
		setIdNumber(p_idNumber);
		setSigle(p_sigle);
		setCurrentPosition(p_pos_i);
		setFinalPosition(p_pos_f);
		setImage(p_image);
	}
	
	private int virtualFacteurX() {
		return this.getVirtualPosition().getX() - this.getFinalPosition().getX();
	}

	private int virtualFacteurY() {
		return this.getVirtualPosition().getY() - this.getFinalPosition().getY();
	}
	
	private ArrayList<Position> getPathX() {
		ArrayList<Position> positions = new ArrayList<Position>();
		
		while(virtualFacteurX() != 0) {
			int increment = -virtualFacteurX()/Math.abs(virtualFacteurX());
			this.virtualPosition.setX(this.virtualPosition.getX() + increment);

			positions.add(new Position(this.virtualPosition.getX(), this.virtualPosition.getY()));
		}
		
		return positions;
	}
	private ArrayList<Position> getPathY() {
		ArrayList<Position> positions = new ArrayList<Position>();	
		
		while(virtualFacteurY() != 0) {
			int increment = -virtualFacteurY()/Math.abs(virtualFacteurY());
			this.virtualPosition.setY(this.virtualPosition.getY() + increment);

			positions.add(new Position(this.virtualPosition.getX(), this.virtualPosition.getY()));
		}
		
		return positions;
	}

	private ArrayList<Position> getPath() {
		this.setVirtualPosition(new Position(this.currentPosition.getX(), this.currentPosition.getY()));
		ArrayList<Position> positions = new ArrayList<Position>();
		
		if(this.forbiddenPostion != null && this.forbiddenPostion.getX() != this.currentPosition.getX()) {
			positions.addAll(this.getPathY());
			positions.addAll(this.getPathX());
		} else {
			positions.addAll(this.getPathX());
			positions.addAll(this.getPathY());
		}
		
		return positions;
	}
	
	private Position getNeightboorFreePosition(Position pos, ArrayList<Position> posToAvoid, Position need) {
		
		ArrayList<Position> posAvailable = new ArrayList<Position>(); 
		Random rand = new Random();
		
		if(need != null && !posToAvoid.contains(need)) {
			if(!GRID.isOccupated(need)) return need;
		}
		
		if(pos.getX() < Common.N-1) {
			Position posTemp = new Position(pos.getX()+1, pos.getY());
			if(!GRID.isOccupated(posTemp))
				posAvailable.add(posTemp);
		}
		
		if(pos.getY() < Common.N-1) {
			Position posTemp = new Position(pos.getX(), pos.getY()+1);
			if(!GRID.isOccupated(posTemp))
				posAvailable.add(posTemp);
		}

		if(pos.getX() > 0) {
			Position posTemp = new Position(pos.getX()-1, pos.getY());
			if(!GRID.isOccupated(posTemp))
				posAvailable.add(posTemp);
		}
		
		if(pos.getY() > 0) {
			Position posTemp = new Position(pos.getX(), pos.getY()-1);
			if(!GRID.isOccupated(posTemp))
				posAvailable.add(posTemp);
		}
		
		posAvailable.removeAll(posToAvoid);
		
		if(posAvailable.size() == 0) {
			return posToAvoid.get(rand.nextInt(posToAvoid.size()));
		}
		
		return posAvailable.get(rand.nextInt(posAvailable.size()));
	}
	
	private ArrayList<Position> getMessages() {
		
		ArrayList<Position> positionsToAvoid = new ArrayList<Position>();
		
		ArrayList<Message> messages = GRID.getListBoxes().get(this.getIdNumber()).getListMessages();
		for(int i = 0; i < messages.size(); i++) {
			Message msg = messages.get(i);
			if(msg==null || msg.isRead()) 
				continue;
			positionsToAvoid.add(msg.getPositionToAvoid());	
			msg.setRead(true);
		}
		
		return positionsToAvoid;
	}
	
	private int tryMoving(ArrayList<Position> path, int trying) {

		if(path.size() < 1)
			return 0;
			
		Position p = path.get(0);
		Position p2 = null;
		if(path.size() > 1) 
			p2 = path.get(1);

		Agent neighbor = GRID.moveAgent(this, p);
		if(neighbor != null) {
			int index = neighbor.getIdNumber();
			Message msg = new Message(this, neighbor, p2);
			GRID.getListBoxes().get(index).addMessage(msg);
		} else {
			trying++;
			if(trying > 5) {
				System.out.println("Trying reset");
				this.forbiddenPostion = null;
				trying = 0;
			}
		}
		
		return trying;
	}

	@Override
	public void run() {
		
		int trying = 0;
		ArrayList<Position> path;
		ArrayList<Position> positionsToAvoid = new ArrayList<Position>();

		while(true) {
			path = getPath();
			
			positionsToAvoid = getMessages();
			
			if(positionsToAvoid.size() > 0) {
				Position need = null;

				if(path.size() > 0)
					need = path.get(0);

				Position newPos = this.getNeightboorFreePosition(this.getCurrentPosition(), positionsToAvoid, need);
				if(newPos != null && need != newPos) {
					path.add(0, newPos);
					this.forbiddenPostion = this.getCurrentPosition();
				}
			}
			
			trying = tryMoving(path, trying);
			
			GRID.getListBoxes().get(this.getIdNumber()).removeReadMessages();
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		}
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
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
