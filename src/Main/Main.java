package Main;
import View.GridView;

public class Main {

	public static void main(String[] args) {
		Grid g = new Grid();
		GridView gv = new GridView(g);
		g.addObserver(gv);
		gv.setVisible(true);
	}
}
