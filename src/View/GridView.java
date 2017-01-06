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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Main.Common;
import Main.Grid;
import Main.ImageCutter;
import Main.Position;

public class GridView extends JFrame implements Observer, ActionListener, ChangeListener {
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
	
	private JSpinner spinnerNumber;
	private Timer timer;
	private JLabel timeLabel;
	private int timeInt;
	
	private JSlider speedSlider;

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
		
		SpinnerModel spinnerModel = new SpinnerNumberModel(grid.getNbAgents(), 3, Common.N*Common.N-1, 1);
		spinnerNumber = new JSpinner(spinnerModel);
		spinnerNumber.setFont(new Font("Arial", Font.PLAIN, 20));
		spinnerNumber.addChangeListener(this);
		
		this.timer = new Timer(1000, this);
		this.timer.setInitialDelay(1);
        
        this.timeLabel = new JLabel("0 : 0");
        this.timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        this.timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.speedSlider = new JSlider(JSlider.HORIZONTAL,5,500,Common.WAIT_TIME_INIT);
        this.speedSlider.addChangeListener(this);
		
		this.panelButtons = new JPanel();
		this.panelButtons.setBounds(Common.IMAGE_SIZE+50, 100, 100, 400);
		this.panelButtons.setLayout(new GridLayout(6,1,40,20));
		this.panelButtons.add(go);
		this.panelButtons.add(newGame);
		this.panelButtons.add(quit);
		this.panelButtons.add(spinnerNumber);
		this.panelButtons.add(timeLabel);
		this.panelButtons.add(speedSlider);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.newGame) {
			this.grid.suspendAgents();
			this.timer.stop();
			
			this.grid.reset();
			this.timeInt = 0;
			this.timeLabel.setText( "0 : 0" );
			
			this.go.setText("Lancer");
			this.go.setActionCommand("0");
			
		} else if(e.getSource() == this.go) {
			if(this.go.getActionCommand() == "0") {
				this.go.setText("Pause");
				this.go.setActionCommand("1");
				this.grid.startAgents();
				timer.start();
			} else if(this.go.getActionCommand() == "1") {
				this.go.setText("Reprendre");
				this.go.setActionCommand("2");
				this.grid.suspendAgents();
				this.timer.stop();
			} else if(this.go.getActionCommand() == "2") {
				this.go.setText("Pause");
				this.go.setActionCommand("1");
				this.grid.resumeAgents();
				timer.start();
			}
			
		} else if(e.getSource() == this.quit) {
			System.exit(0);
			
		} else if(e.getSource() == this.timer) {
			timeInt++;
			String time = (int)Math.ceil(timeInt/60)+" : "+timeInt%60;
			timeLabel.setText( time );
			
			if(grid.allAgentWellPlaced()) {
				this.grid.suspendAgents();
				this.timer.stop();
				JOptionPane.showMessageDialog(null, "Terminé en "+(int)Math.ceil(timeInt/60)+" min et "+timeInt%60+" s");
			}
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == this.spinnerNumber) {
			grid.setNbAgents((int)this.spinnerNumber.getValue());
		} else if(e.getSource() == this.speedSlider) {
			grid.setSpeed((int)this.speedSlider.getValue());
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
