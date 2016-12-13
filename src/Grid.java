import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class Grid extends Observable {

	public static final int N = 5;
	
	private ArrayList<Agent> listAgents;
	private ArrayList<MessageBox> listBoxes;
	private int moveCounter;
	
	public Grid() {
		
		createAgents();
		reset();
	}
	
	public void reset() {
		
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
		
		arrayAgents.add(new Agent(this, 0, "x", new Position(0, 0), new Position(3, 2)));
		arrayBoxes.add(new MessageBox(0));
		arrayAgents.add(new Agent(this, 1, "o", new Position(0, 1), new Position(0, 0)));
		arrayBoxes.add(new MessageBox(1));
		arrayAgents.add(new Agent(this, 2, "^", new Position(0, 2), new Position(4, 4)));
		arrayBoxes.add(new MessageBox(2));
		arrayAgents.add(new Agent(this, 3, "$", new Position(0, 3), new Position(1, 1)));
		arrayBoxes.add(new MessageBox(3));
		
		setListAgents(arrayAgents);
		setListBoxes(arrayBoxes);
	}

	private void shuffleGrid() {
		
		Random rand = new Random();
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				switchAgent(new Position(i, j), new Position(rand.nextInt(N), rand.nextInt(N)));
			}
		}
	}
	
	public boolean isOccupated(Position pos) {
		
		for(Agent agent : listAgents) {
			if(agent.getCurrentPosition().equals(pos)) 
				return true;
		}
		
		return false;
	}
	
	public boolean neightboursIsOccupated(Position pos, int iX, int iY) {
		
		Position posNeight = new Position(pos.getX()+iX, pos.getY()+iY);
		for(Agent agent : listAgents) {
			if(agent.getCurrentPosition().equals(posNeight)) 
				return true;
		}
		
		return false;
	}
	
	public Agent getPositionAgent(Position pos) {
		
		for(Agent agent : listAgents) {
			if(agent.getCurrentPosition().equals(pos)) 
				return agent;
		}
		
		return null;
	}
	
	public String getPositionSigle(Position pos) {
		
		for(Agent agent : listAgents) {
			if(agent.getCurrentPosition().equals(pos)) {
				return agent.getSigle();
			}
		}
		
		return " ";
	}
	
	public void switchAgent(Position pos1, Position pos2) {
	
		Agent agent1 = getPositionAgent(pos1);
		Agent agent2 = getPositionAgent(pos2);
		
		if(agent1 != null) agent1.setCurrentPosition(pos2);
		if(agent2 != null) agent2.setCurrentPosition(pos1);
		
		this.setChanged();
		this.notifyObservers();
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
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				retur += "|"+this.getPositionSigle(new Position(i, j))+"|";
			}
			retur += "\n";
		}
		
		return retur;
	}
	
	public String toStringFinal() {
		
		String retur = "";
		boolean print;
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
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
