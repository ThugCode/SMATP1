
public class Message {

	private static int id = 1;
	
	private int idNumber;
	private boolean read;
	private Agent agentFrom;
	private Agent agentTo;
	private Position positionToLeave;
	private boolean response;
	
	public Message(Agent p_agentFrom, Agent p_agentTo, Position p_positionToLeave) {
		
		setIdNumber(id);
		setRead(false);
		setAgentFrom(p_agentFrom);
		setAgentTo(p_agentTo);
		setPositionToLeave(p_positionToLeave);
		setResponse(false);
		id++;
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
	
	public Agent getAgentFrom() {
		return agentFrom;
	}

	public void setAgentFrom(Agent agentFrom) {
		this.agentFrom = agentFrom;
	}

	public Agent getAgentTo() {
		return agentTo;
	}

	public void setAgentTo(Agent agentTo) {
		this.agentTo = agentTo;
	}

	public Position getPositionToLeave() {
		return positionToLeave;
	}

	public void setPositionToLeave(Position positionToLeave) {
		this.positionToLeave = positionToLeave;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}
}
