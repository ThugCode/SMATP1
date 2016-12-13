import java.util.ArrayList;

public class MessageBox {

	private int idNumber;
	private ArrayList<Message> listMessages;
	
	public MessageBox(int p_idNumber) {
		setIdNumber(p_idNumber);
		setListMessages(new ArrayList<Message>());
	}

	public boolean addMessage(Message msg) {
		return getListMessages().add(msg);
	}
	
	public boolean removeMessage(Message msg) {
		return getListMessages().remove(msg);
	}
	
	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	public ArrayList<Message> getListMessages() {
		return listMessages;
	}

	public void setListMessages(ArrayList<Message> listMessages) {
		this.listMessages = listMessages;
	}
}
