import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridView extends JFrame {
	private static final long serialVersionUID = -6118846850213932379L;

	private JPanel panelBoutons;
	private JButton nouveau;
	private JButton quitter;

	private JPanel panelCases;
	private JButton [][] lesCases;
	
	public GridView() {
		
		this.creePanels();
		this.init();
		
		
		this.setTitle("SMA");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(500,200,520,500);
		this.setResizable(false);
	}
	
	public void init()
	{

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
				this.lesCases[i][j].setText("");
				this.lesCases[i][j].setEnabled(false);
				//this.lesCases[i][j].addActionListener(this);
				this.panelCases.add(this.lesCases[i][j]);
			}
		}
	}
	
	public void creePanelBoutons( )
	{ 
		this.nouveau = new JButton("Nouveau");
		this.nouveau.setBounds(10,10,150,40);
		//this.nouveau.addActionListener(this);
		
		this.quitter = new JButton("Quitter");
		this.quitter.setBounds(200,10,150,40);
		//this.quitter.addActionListener(this);
		
		this.panelBoutons = new JPanel();
		this.panelBoutons.setLayout(null);
		this.panelBoutons.setBackground(Color.darkGray);
		this.panelBoutons.add(nouveau);
		this.panelBoutons.add(quitter);
	}
	
	public static void main(String[] args) {
		GridView i = new GridView();
		i.setVisible(true);
	}
}
