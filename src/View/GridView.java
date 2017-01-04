package View;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Main.Common;
import Main.Grid;
import Main.ImageCutter;
import Main.Position;

public class GridView extends JFrame implements Observer, ActionListener {
	private static final long serialVersionUID = -6118846850213932379L;

	private Grid grid;
	private JLabel [][] blocks;
	
	private JPanel panelResolve;
	private JPanel panelSolution;
	private JPanel panelBlocks;
	private JPanel panelButtons;
	
	private JButton newGame;
	private JButton go;
	private JButton quit;

	public GridView(Grid p_grid) {
		
		this.setGrid(p_grid);
		
		this.createPanels();
		
		this.setTitle("SMA");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, Common.IMAGE_SIZE+160, Common.IMAGE_SIZE+100);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	/***
	 * Create all panels in the frame
	 */
	public void createPanels()
	{  
		this.createPanelButtons(); 
		this.createPanelBlocks();
		
		panelResolve = new JPanel();
		panelResolve.setLayout(null);
		panelResolve.add(panelBlocks);
		
		JLabel labelIcon = new JLabel(ImageCutter.getImageIcon(grid.getImageName()));
		labelIcon.setBounds(10, 10, Common.IMAGE_SIZE, Common.IMAGE_SIZE);
		
		panelSolution = new JPanel();
		panelSolution.setLayout(null);
		panelSolution.add(labelIcon);
		
		JTabbedPane jtp = new JTabbedPane();
		jtp.setBounds(0, 10, Common.IMAGE_SIZE+40, Common.IMAGE_SIZE+65);
	    jtp.addTab("Jeu", panelResolve);
	    jtp.addTab("Solution", panelSolution);
	    
	    Container contenu = this.getContentPane();
	    contenu.setLayout(null);
		contenu.add(jtp);
		contenu.add(panelButtons);
	}
	
	/***
	 * Create cases panel where agents will work
	 */
	public void createPanelBlocks()
	{
		this.blocks = new JLabel[Common.N][Common.N];
		this.panelBlocks = new JPanel();
		this.panelBlocks.setBounds(10, 10, Common.IMAGE_SIZE, Common.IMAGE_SIZE);
		this.panelBlocks.setLayout(new GridLayout(Common.N,Common.N,1,1));
		
		for ( int i = 0 ; i < Common.N ; i ++ ) {
			for ( int j = 0 ; j < Common.N ; j ++ ) {
				this.blocks[i][j] = new JLabel();
				this.blocks[i][j].setFont(new Font("Arial", Font.PLAIN, 50));
				//this.blocks[i][j].setText(this.grid.getSigleToPosition(new Position(i, j)));
				this.blocks[i][j].setIcon(this.grid.getImageToPosition(new Position(i, j)));
				this.panelBlocks.add(this.blocks[i][j]);
			}
		}
	}
	
	/***
	 * Create buttons control panel
	 */
	public void createPanelButtons()
	{ 
		this.newGame = new JButton("Nouveau");
		this.newGame.addActionListener(this);
		
		this.go = new JButton("Lancer");
		this.go.setActionCommand("0");
		this.go.addActionListener(this);
		
		this.quit = new JButton("Quitter");
		this.quit.addActionListener(this);
		
		this.panelButtons = new JPanel();
		this.panelButtons.setBounds(Common.IMAGE_SIZE+50, 20, 100, 200);
		this.panelButtons.setLayout(new GridLayout(3,1,40,20));
		this.panelButtons.add(go);
		this.panelButtons.add(newGame);
		this.panelButtons.add(quit);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.newGame) {
			this.grid.suspendAgents();
			this.grid.reset();
			this.go.setText("Lancer");
			this.go.setActionCommand("0");
		} else if(e.getSource() == this.go) {
			if(this.go.getActionCommand() == "0") {
				this.go.setText("Pause");
				this.go.setActionCommand("1");
				this.grid.startAgents();
			} else if(this.go.getActionCommand() == "1") {
				this.go.setText("Reprendre");
				this.go.setActionCommand("2");
				this.grid.suspendAgents();
			} else if(this.go.getActionCommand() == "2") {
				this.go.setText("Pause");
				this.go.setActionCommand("1");
				this.grid.resumeAgents();
			}
			
		} else if(e.getSource() == this.quit) {
			System.exit(0);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
	
		for ( int i = 0 ; i < Common.N ; i ++ ) {
			for ( int j = 0 ; j < Common.N ; j ++ ) {
				//this.blocks[i][j].setText(grid.getSigleToPosition(new Position(i, j)));
				this.blocks[i][j].setIcon(this.grid.getImageToPosition(new Position(i, j)));
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
