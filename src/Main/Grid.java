package Main;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import javax.swing.ImageIcon;

import Message.MessageBox;

public class Grid extends Observable {

	private ArrayList<Agent> listAgents;
	private ArrayList<MessageBox> listBoxes;
	private String imageName;

	public Grid() {
		Agent.GRID = this;
		
		setImageName("cross.png");
		
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
		
		Random rand = new Random();
		ArrayList<Position> finals = getFinalPositions();
		ArrayList<ImageIcon> images = ImageCutter.getListImages(imageName);
		
		for(int i=0; i<Common.NB_AGENTS; i++) {
			int x = (int) Math.ceil(i/Common.N);
			int y = i%Common.N;
			int random = rand.nextInt(finals.size()-1);
			Position tempPosition = finals.get(random);
			ImageIcon tempImage = images.get(random);
			finals.remove(tempPosition);
			images.remove(tempImage);
			arrayAgents.add(new Agent(i, Common.SIGLES[i], new Position(x, y), tempPosition, tempImage));
			arrayBoxes.add(new MessageBox(i));
		}
		
		setListAgents(arrayAgents);
		setListBoxes(arrayBoxes);
	}
	
	private ArrayList<Position> getFinalPositions() {
		
		ArrayList<Position> list = new ArrayList<Position>();
		
		for(int i=0;i<Common.N;i++) {
			for(int j=0;j<Common.N;j++) {
				list.add(new Position(i, j));
			}
		}
		
		return list;
	}

	private void shuffleGrid() {
		
		Random rand = new Random();
		
		for(int i=0;i<Common.N;i++) {
			for(int j=0;j<Common.N;j++) {
				switchCases(new Position(i, j), new Position(rand.nextInt(Common.N), rand.nextInt(Common.N)));
			}
		}
		
		this.setChanged();
		this.notifyObservers();
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
	
	public ImageIcon getImageToPosition(Position pos) {
		
		for(Agent agent : listAgents) {
			if(agent.getCurrentPosition().equals(pos)) {
				return agent.getImage();
			}
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
	
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String toString() {
		
		String retur = "";
		
		for(int i=0;i<Common.N;i++) {
			for(int j=0;j<Common.N;j++) {
				retur += "|"+this.getSigleToPosition(new Position(i, j))+"|";
			}
			retur += "\n";
		}
		
		return retur;
	}
	
	public String toStringFinal() {
		
		String retur = "";
		boolean print;
		
		for(int i=0;i<Common.N;i++) {
			for(int j=0;j<Common.N;j++) {
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
