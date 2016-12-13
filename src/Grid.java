import java.util.ArrayList;
import java.util.Random;

public class Grid {

	public static final int N = 5;
	
	private Agent[][] gridAgents;
	private ArrayList<Agent> listAgents;
	private ArrayList<MessageBox> listBoxes;
	private int moveCounter;
	
	public Grid() {
		
		gridAgents = new Agent[N][N];
		
		createAgents();
		System.out.println(toString());
		shuffleGrid();
		System.out.println(toString());
		System.out.println("Final Gridd");
		System.out.println(toStringFinal());
		System.out.println("Current Gridd");
		System.out.println(toString());
		
		for(Agent agent : listAgents) {
			agent.start();
		}
		
	}
	
	/***
	 * Create all agents in the grid
	 */
	private void createAgents() {
		
		ArrayList<Agent> array = new ArrayList<Agent>();
		array.add(new Agent(this, "x", new Position(0, 0), new Position(3, 2)));
		gridAgents[0][0] = array.get(0);
		array.add(new Agent(this, "o", new Position(0, 1), new Position(0, 0)));
		gridAgents[0][1] = array.get(1);
		array.add(new Agent(this, "^", new Position(0, 2), new Position(4, 4)));
		gridAgents[0][2] = array.get(2);
		array.add(new Agent(this, "$", new Position(0, 3), new Position(1, 1)));
		gridAgents[0][3] = array.get(3);
		setListAgents(array);
	}

	private void shuffleGrid() {
		
		Random rand = new Random();
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				switchAgent(new Position(i, j), new Position(rand.nextInt(N), rand.nextInt(N)));
			}
		}
	}
	
	public void switchAgent(Position pos1, Position pos2) {
		
		Agent agent1 = gridAgents[pos1.getX()][pos1.getY()];
		Agent agent2 = gridAgents[pos2.getX()][pos2.getY()];
		
		if(agent1 != null) agent1.setCurrentPosition(pos2);
		if(agent2 != null) agent2.setCurrentPosition(pos1);
		gridAgents[pos1.getX()][pos1.getY()] = agent2;
		gridAgents[pos2.getX()][pos2.getY()] = agent1;
		
		System.out.println(this.toString());
	}
	
	public boolean isEmpty(Position position, int iX, int iY) {
		return this.gridAgents[position.getX()+iX][position.getY()+iY] == null;
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
				if(this.gridAgents[i][j] != null)
					retur += "|"+this.gridAgents[i][j].getSigle()+"|";
				else
					retur += "| |";
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
