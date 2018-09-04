package presentation;
import java.awt.BorderLayout;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import model.*;
import vrp.*;
import controller.Controlleur;
public class DescenteSolutionFenetre extends JFrame {

	private JTextField nbrVoitureField;
	private JTextField dureeField;
	private JTextField distanceField;
	private Solution s;
	JPanel solutionChart;
	Controlleur c;
	JEditorPane editorPane;
	JEditorPane pastSolotions;
	private JLabel lblNewLabel_3;
	SolomonReader instance;
	JPanel chart;
	JTextField timeField; 
	/**
	 * Create the frame.
	 */
	public DescenteSolutionFenetre(Controlleur con,Solution sol,SolomonReader solomon,long time) {
		this.s=sol;
		this.c=con;
		this.instance=solomon;
		//WebLookAndFeel.install();
		initialize();
		this.setSize(750, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		timeField.setText(time+" (ms)");
		timeField.setEditable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		chart=new SolutionChartPanel(solomon,this.s);
		solutionChart.removeAll();
		solutionChart.add(chart, BorderLayout.CENTER);
		solutionChart.revalidate();
		solutionChart.repaint();
		nbrVoitureField.setText(String.valueOf(s.getNbrUsedVehicles()));
		nbrVoitureField.setEditable(false);
		dureeField.setText(String.valueOf(s.getDureeUtitlise()));
		dureeField.setEditable(false);
		distanceField.setText(String.valueOf(s.Cost));
		distanceField.setEditable(false);
		editorPane.setText(this.s.toString());
		editorPane.setEditable(false);
		String past="";
		for(String t:s.getPastSolutions()) {
			past+=t + "\n";
		}
		pastSolotions.setText(past);	
	}
	
	public void initialize() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0,100));
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		editorPane = new JEditorPane();
		
		panel.add(new JScrollPane(editorPane), BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.setPreferredSize(new Dimension(180,0));
		getContentPane().add(panel_1, BorderLayout.EAST);
		
		JPanel panel_3=new JPanel();
		((FlowLayout)panel_3.getLayout()).setVgap(10);
		panel_3.setPreferredSize(new Dimension(180,280));
		panel_1.add(panel_3,BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("nombre de voiture utilis\u00E9 :");
		panel_3.add(lblNewLabel,BorderLayout.NORTH);
		
		nbrVoitureField = new JTextField();
		panel_3.add(nbrVoitureField,BorderLayout.NORTH);
		nbrVoitureField.setColumns(14);
		
		JLabel lblNewLabel_1 = new JLabel("dur\u00E9e totale d'utlilisation:");
		panel_3.add(lblNewLabel_1,BorderLayout.NORTH);
		
		dureeField = new JTextField();
		panel_3.add(dureeField,BorderLayout.NORTH);
		dureeField.setColumns(14);
		
		JLabel lblNewLabel_2 = new JLabel("distance totale parcourue :");
		panel_3.add(lblNewLabel_2,BorderLayout.NORTH);
		
		distanceField = new JTextField();
		panel_3.add(distanceField,BorderLayout.NORTH);
		distanceField.setColumns(14);
		
		JLabel lbl_time = new JLabel("temps d'execution:");
		panel_3.add(lbl_time,BorderLayout.NORTH);
		
		timeField = new JTextField();
		panel_3.add(timeField,BorderLayout.NORTH);
		timeField.setColumns(14);
		
		JLabel lbl_time1 = new JLabel("Solutions Parcouru:");
		panel_3.add(lbl_time1,BorderLayout.NORTH);
		pastSolotions = new JEditorPane();
		pastSolotions.setSize(160, 160);
		panel_1.add(new JScrollPane(pastSolotions),BorderLayout.CENTER);
		
		solutionChart = new JPanel();
		solutionChart.setBorder(new TitledBorder(null, "Carte du Solution :", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(solutionChart, BorderLayout.CENTER);
		solutionChart.setLayout(new BorderLayout(0, 0));
	}


}
