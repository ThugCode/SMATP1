import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridView extends JFrame implements Observer, ActionListener {
	private static final long serialVersionUID = -6118846850213932379L;

	private Grid grid;
	private JButton [][] lesCases;
	
	private JPanel panelCases;
	private JPanel panelBoutons;
	private JButton nouveau;
	private JButton lancer;
	private JButton quitter;

	public GridView(Grid p_grid) {
		
		this.setGrid(p_grid);
		
		this.creePanels();
		
		this.setTitle("SMA");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(500,200,520,500);
		this.setResizable(false);
	}
	
	public void creePanels()
	{  
		this.creePanelBoutons( ); 
		this.creePanelCases( );
		
		this.panelCases.setBounds(10,10,495,380);
		this.panelBoutons.setBounds(10,400,495,60);
		
		Container contenu = this.getContentPane();
		contenu.setLayout(null);
		contenu.add(panelCases);		
		contenu.add(panelBoutons);
	}
	
	public void creePanelCases()
	{
		this.lesCases = new JButton[Grid.N][Grid.N];
		this.panelCases = new JPanel();
		this.panelCases.setLayout(new GridLayout(Grid.N,Grid.N,10,10));
		
		for ( int i = 0 ; i < Grid.N ; i ++ )
		{
			for ( int j = 0 ; j < Grid.N ; j ++ )
			{
				this.lesCases[i][j] = new JButton();
				this.lesCases[i][j].setFont(new Font("Arial", Font.PLAIN, 50));
				this.lesCases[i][j].setText(this.grid.getPositionSigle(new Position(i, j)));
				//this.lesCases[i][j].addActionListener(this);
				this.panelCases.add(this.lesCases[i][j]);
			}
		}
	}
	
	public void creePanelBoutons( )
	{ 
		this.nouveau = new JButton("Nouveau");
		this.nouveau.addActionListener(this);
		
		this.lancer = new JButton("Lancer");
		this.lancer.setActionCommand("0");
		this.lancer.addActionListener(this);
		
		this.quitter = new JButton("Quitter");
		this.quitter.addActionListener(this);
		
		this.panelBoutons = new JPanel();
		this.panelBoutons.setLayout(new GridLayout(1,3,40,20));
		this.panelBoutons.add(lancer);
		this.panelBoutons.add(nouveau);
		this.panelBoutons.add(quitter);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.nouveau) {
			this.grid.suspendAgents();
			this.grid.reset();
			this.lancer.setText("Lancer");
			this.lancer.setActionCommand("0");
		} else if(e.getSource() == this.lancer) {
			if(this.lancer.getActionCommand() == "0") {
				this.lancer.setText("Pause");
				this.lancer.setActionCommand("1");
				this.grid.startAgents();
			} else if(this.lancer.getActionCommand() == "1") {
				this.lancer.setText("Reprendre");
				this.lancer.setActionCommand("2");
				this.grid.suspendAgents();
			} else if(this.lancer.getActionCommand() == "2") {
				this.lancer.setText("Pause");
				this.lancer.setActionCommand("1");
				this.grid.resumeAgents();
			}
			
		} else if(e.getSource() == this.quitter) {
			System.exit(0);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
	
		for ( int i = 0 ; i < Grid.N ; i ++ ) {
			for ( int j = 0 ; j < Grid.N ; j ++ ) {
				this.lesCases[i][j].setText(grid.getPositionSigle(new Position(i, j)));
			}
		}
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}
}
