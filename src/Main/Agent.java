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
	
	private int waitingTime;
	private boolean waitOther;

	public Agent(int p_idNumber, String p_sigle, Position p_pos_i, Position p_pos_f, ImageIcon p_image) {
		setIdNumber(p_idNumber);
		setSigle(p_sigle);
		setCurrentPosition(p_pos_i);
		setFinalPosition(p_pos_f);
		setImage(p_image);
	}
	
	/***
	 * Calculate all positions to go to final position on X axis
	 * @return
	 */
	private ArrayList<Position> getPathX(ArrayList<Position> positions) {
		
		while(virtualGapX() != 0) {
			int increment = -virtualGapX()/Math.abs(virtualGapX());
			this.virtualPosition.setX(this.virtualPosition.getX() + increment);

			positions.add(new Position(this.virtualPosition.getX(), this.virtualPosition.getY()));
		}
		return positions;
	}
	
	/***
	 * Calculate all positions to go to final position on Y axis
	 * @return
	 */
	private ArrayList<Position> getPathY(ArrayList<Position> positions) {
		
		while(virtualGapY() != 0) {
			int increment = -virtualGapY()/Math.abs(virtualGapY());
			this.virtualPosition.setY(this.virtualPosition.getY() + increment);

			positions.add(new Position(this.virtualPosition.getX(), this.virtualPosition.getY()));
		}
		return positions;
	}

	/***
	 * Get path to final position
	 * @return
	 */
	private ArrayList<Position> getPath() {
		
		ArrayList<Position> positions = new ArrayList<Position>();
		
		this.setVirtualPosition(new Position(this.currentPosition.getX(), this.currentPosition.getY()));
		
		if(this.forbiddenPostion != null && this.forbiddenPostion.getX() != this.currentPosition.getX()) {
			positions = this.getPathY(positions);
			positions = this.getPathX(positions);
		} else {
			positions = this.getPathX(positions);
			positions = this.getPathY(positions);
		}
		
		return positions;
	}
	
	/***
	 * Verify neighborhood to find best place to go
	 * @param pos
	 * @param posToAvoid
	 * @param need
	 * @return
	 */
	private Position getPositionToGo(Position pos, ArrayList<Position> posToAvoid, Position need) {
		
		Random rand = new Random();
		ArrayList<Position> posAvailable = new ArrayList<Position>();
		ArrayList<Position> allPosition = new ArrayList<Position>();
		
		//If needed position is not avoid, go for it
		if(need != null && !posToAvoid.contains(need)) {
			if(!GRID.isOccupated(need)) return need;
		}
		
		//Verify right block
		if(pos.getX() < Common.N-1) {
			Position posTemp = new Position(pos.getX()+1, pos.getY());
			allPosition.add(posTemp);
			if(!GRID.isOccupated(posTemp))
				posAvailable.add(posTemp);
		}
		
		//Verify down block
		if(pos.getY() < Common.N-1) {
			Position posTemp = new Position(pos.getX(), pos.getY()+1);
			allPosition.add(posTemp);
			if(!GRID.isOccupated(posTemp))
				posAvailable.add(posTemp);
		}

		//Verify left block
		if(pos.getX() > 0) {
			Position posTemp = new Position(pos.getX()-1, pos.getY());
			allPosition.add(posTemp);
			if(!GRID.isOccupated(posTemp))
				posAvailable.add(posTemp);
		}
		
		//Verify up block
		if(pos.getY() > 0) {
			Position posTemp = new Position(pos.getX(), pos.getY()-1);
			allPosition.add(posTemp);
			if(!GRID.isOccupated(posTemp))
				posAvailable.add(posTemp);
		}
		
		//Remove all position to avoid and then return a block
		posAvailable.removeAll(posToAvoid);
		if(posAvailable.size() > 0) {
			return posAvailable.get(rand.nextInt(posAvailable.size()));
		}
		
		//If no block available go for a random block
		return allPosition.get(rand.nextInt(allPosition.size()));
	}
	
	/***
	 * Read message to move to let other agent get the block
	 * @return
	 */
	private ArrayList<Position> readMessages() {
		
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
	
	/***
	 * Try moving or send message to other agent to get free block
	 * @param path
	 * @param trying
	 * @return
	 */
	private int tryMoving(ArrayList<Position> path, int trying) {

		if(path.size() < 1)
			return 0;
			
		Position p = path.get(0);
		Position p2 = null;
		if(path.size() > 1) 
			p2 = path.get(1);

		//Try to move on the grid
		Agent neighbor = GRID.moveAgent(this, p);
		
		//If not possible, send message
		if(neighbor != null) {
			int index = neighbor.getIdNumber();
			Message msg = new Message(this, neighbor, p2);
			GRID.getListBoxes().get(index).addMessage(msg);
			trying = 0;
		} 
		else //Else increase trying
		{
			//If the agent moved for an other agent
			//Wait other agent to get the block and leave it
			if(this.waitOther) this.waitingTime = GRID.getSpeed()*2;
			
			trying++;
			if(trying > 5) {
				this.forbiddenPostion = null;
				trying = 0;
			}
		}
		
		return trying;
	}
	
	/***
	 * if agent can move to let a block free for an other agent, 
	 * he will add a move on his path
	 * @param path
	 * @param positionsToAvoid
	 * @return
	 */
	private ArrayList<Position> addMoveToAvoidPosition(ArrayList<Position> path, ArrayList<Position> positionsToAvoid) {
		
		if(positionsToAvoid.size() == 0)
			return path;
			
		Position need = null;

		if(path.size() > 0)
			need = path.get(0);

		Position newPos = this.getPositionToGo(this.getCurrentPosition(), positionsToAvoid, need);
		
		//If position to move is free and is not the next move
		//Add move to path
		if(newPos != null && need != newPos) {
			path.add(0, newPos);
			this.forbiddenPostion = this.getCurrentPosition();
			this.waitOther = true;
		}
		
		this.waitingTime = GRID.getSpeed()*2;
		
		return path;
	}

	@Override
	public void run() {
		
		int trying = 0;
		ArrayList<Position> path;
		ArrayList<Position> positionsToAvoid = new ArrayList<Position>();

		while(true) {
			
			this.waitOther = false;
			this.waitingTime = GRID.getSpeed();
			
			path = getPath();
			
			positionsToAvoid = readMessages();
			
			path = addMoveToAvoidPosition(path, positionsToAvoid);
			
			trying = tryMoving(path, trying);
			
			GRID.getListBoxes().get(this.getIdNumber()).removeReadMessages();
			
			try { Thread.sleep(this.waitingTime); } catch (InterruptedException e) { e.printStackTrace(); }
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
	
	private int virtualGapX() {
		return this.getVirtualPosition().getX() - this.getFinalPosition().getX();
	}

	private int virtualGapY() {
		return this.getVirtualPosition().getY() - this.getFinalPosition().getY();
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
