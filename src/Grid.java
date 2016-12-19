import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class Grid extends Observable {

	public static final int N = 5;
	public static final String[] SIGLES = new String[] {"x", "o", "^", "$", "&", 
														"w", "§", "!", "ç", "@", 
														"$", "€", "*", "%", "?",
														"#", "[", "]", "{", "}",
														":", ".", "=", "+"};
	
	private ArrayList<Agent> listAgents;
	private ArrayList<MessageBox> listBoxes;
	private int moveCounter;
	
	public Grid() {
		
		createAgents();
		start();
	}
	
	public void reset() {
		createAgents();
		start();
	}
	
	public void start() {
				
		shuffleGrid();
		
		System.out.println("Current Grid");
		System.out.println(toString());
		System.out.println("Final Grid");
		System.out.println(toStringFinal());
	}
	
	public void startAgents() {
		for(Agent agent : listAgents) {
			agent.start();
		}
	}
	
	public void suspendAgents() {
		for(Agent agent : listAgents) {
			agent.suspend();
		}
	}
	
	public void resumeAgents() {
		for(Agent agent : listAgents) {
			agent.resume();
		}
	}
	
	/***
	 * Create all agents in the grid
	 */
	private void createAgents() {
		
		ArrayList<Agent> arrayAgents = new ArrayList<Agent>();
		ArrayList<MessageBox> arrayBoxes = new ArrayList<MessageBox>();
		
		int nbAgent = 10;
		
		
		Random rand = new Random();
		ArrayList<Position> finals = getFinalPositions();
		Position temp;
		int x;
		int y;
		
		for(int i=0; i<nbAgent; i++) {
			x = (int) Math.ceil(i/Grid.N);
			y = i%Grid.N;
			temp = finals.get(rand.nextInt(finals.size()-1));
			finals.remove(temp);
			arrayAgents.add(new Agent(this, i, Grid.SIGLES[i], new Position(x, y), temp));
			arrayBoxes.add(new MessageBox(i));
		}
		
		setListAgents(arrayAgents);
		setListBoxes(arrayBoxes);
	}
	
	private ArrayList<Position> getFinalPositions() {
		
		ArrayList<Position> list = new ArrayList<Position>();
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				list.add(new Position(i, j));
			}
		}
		
		return list;
	}

	private void shuffleGrid() {
		
		Random rand = new Random();
		
		for(int i=0;i<Grid.N;i++) {
			for(int j=0;j<Grid.N;j++) {
				switchCases(new Position(i, j), new Position(rand.nextInt(Grid.N), rand.nextInt(Grid.N)));
			}
		}
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public synchronized Position getNeightboorFreePosition(Position pos, Position posToAvoid, Position need) {
		
		if(need != null && !need.equals(posToAvoid)) {
			if(!isOccupated(need)) return need;
		}
		
		if(pos.getX() < N-1) {
			Position newPosition = new Position(pos.getX()+1, pos.getY());
			if(!isOccupated(newPosition) && !newPosition.equals(posToAvoid)) return newPosition;
		}
		
		if(pos.getY() < N-1) {
			Position newPosition = new Position(pos.getX(), pos.getY()+1);
			if(!isOccupated(newPosition) && !newPosition.equals(posToAvoid)) return newPosition;
		}

		if(pos.getX() > 0) {
			Position newPosition = new Position(pos.getX()-1, pos.getY());
			if(!isOccupated(newPosition) && !newPosition.equals(posToAvoid)) return newPosition;
		}
		
		if(pos.getY() > 0) {
			Position newPosition = new Position(pos.getX(), pos.getY()-1);
			if(!isOccupated(newPosition) && !newPosition.equals(posToAvoid)) return newPosition;
		}

		return null;
	}
	
	private void switchCases(Position pos1, Position pos2) {
    	Agent agent1 = getAgentToPosition(pos1);
		Agent agent2 = getAgentToPosition(pos2);
		
		if(agent1 != null) agent1.setCurrentPosition(pos2);
		if(agent2 != null) agent2.setCurrentPosition(pos1);
	}
	
	public boolean isOccupated(Position pos) {
		
		for(Agent agent : listAgents) {
			if(agent.getCurrentPosition().equals(pos)) 
				return true;
		}
		
		return false;
	}
	
	public Agent getAgentToPosition(Position pos) {
		
		for(Agent agent : listAgents) {
			if(agent.getCurrentPosition().equals(pos)) 
				return agent;
		}
		
		return null;
	}
	
	public String getSigleToPosition(Position pos) {
		
		for(Agent agent : listAgents) {
			if(agent.getCurrentPosition().equals(pos)) {
				return agent.getSigle();
			}
		}
		
		return " ";
	}
	
	public synchronized Agent moveAgent(Agent agent, Position newPos) {
	
		Agent neighbor = this.getAgentToPosition(newPos);
        if(neighbor == null) {
    		
    		if(agent != null) agent.setCurrentPosition(newPos);
    		
    		this.setChanged();
    		this.notifyObservers();
        }
        
        return neighbor;
	}
	
	public ArrayList<Agent> getListAgents() {
		return listAgents;
	}

	public void setListAgents(ArrayList<Agent> listAgents) {
		this.listAgents = listAgents;
	}

	public ArrayList<MessageBox> getListBoxes() {
		return listBoxes;
	}

	public void setListBoxes(ArrayList<MessageBox> listBoxes) {
		this.listBoxes = listBoxes;
	}

	public int getMoveCounter() {
		return moveCounter;
	}

	public void setMoveCounter(int moveCounter) {
		this.moveCounter = moveCounter;
	}
	
	public String toString() {
		
		String retur = "";
		
		for(int i=0;i<Grid.N;i++) {
			for(int j=0;j<Grid.N;j++) {
				retur += "|"+this.getSigleToPosition(new Position(i, j))+"|";
			}
			retur += "\n";
		}
		
		return retur;
	}
	
	public String toStringFinal() {
		
		String retur = "";
		boolean print;
		
		for(int i=0;i<Grid.N;i++) {
			for(int j=0;j<Grid.N;j++) {
				print = false;
				for(Agent agent : listAgents) {
					if(agent.getFinalPosition().getX() == i
					&& agent.getFinalPosition().getY() == j) {
						retur += "|"+agent.getSigle()+"|";
						print = true;
					}
				}
				if(!print) 
					retur += "| |";
			}
			retur += "\n";
		}
		
		return retur;
	}
}
