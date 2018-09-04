package presentation;
import java.awt.BorderLayout;


import vrp.*;
import model.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import controller.Controlleur;

public class InitSolutionFenetre extends JFrame {
	private JTextField nbrVoitureField;
	private JTextField dureeField;
	private JTextField distanceField;
	Solution s;
	JPanel solutionChart;
	JButton backBtn;
	Controlleur c;
	JEditorPane editorPane;
	JTextField timeField; 

	/**
	 * Create the frame.
	 */
	public InitSolutionFenetre(Controlleur c,Solution s,SolomonReader solomon,long time) {
		this.s=s;
		this.c=c;
		//WebLookAndFeel.install();
		initialize();
		this.setSize(750, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		timeField.setText(time+" (ms)");
		timeField.setEditable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel chart=new SolutionChartPanel(solomon,this.s);
		chart.setPreferredSize(new Dimension(solutionChart.getWidth(),solutionChart.getHeight()));
		solutionChart.removeAll();
		solutionChart.add(chart, BorderLayout.CENTER);
		solutionChart.revalidate();
		solutionChart.repaint();
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitSolutionFenetre.this.setVisible(false);
				InitSolutionFenetre.this.c.backFunction();
			}
		});
		nbrVoitureField.setText(String.valueOf(s.getNbrUsedVehicles()));
		nbrVoitureField.setEditable(false);
		dureeField.setText(String.valueOf(s.getDureeUtitlise()));
		dureeField.setEditable(false);
		distanceField.setText(String.valueOf(s.Cost));
		distanceField.setEditable(false);
		editorPane.setText(this.s.toString());
		editorPane.setEditable(false);
	}
	
	public void initialize() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0,100));
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		editorPane = new JEditorPane();
		
		panel.add(new JScrollPane(editorPane), BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(15);
		panel_1.setPreferredSize(new Dimension(180,0));
		getContentPane().add(panel_1, BorderLayout.EAST);
		
		JLabel lblNewLabel = new JLabel("nombre de voiture utilis\u00E9 :");
		panel_1.add(lblNewLabel);
		
		nbrVoitureField = new JTextField();
		panel_1.add(nbrVoitureField);
		nbrVoitureField.setColumns(14);
		
		JLabel lblNewLabel_1 = new JLabel("dur\u00E9e totale d'utlilisation:");
		panel_1.add(lblNewLabel_1);
		
		dureeField = new JTextField();
		panel_1.add(dureeField);
		dureeField.setColumns(14);
		
		JLabel lblNewLabel_2 = new JLabel("distance totale parcourue :");
		panel_1.add(lblNewLabel_2);
		
		distanceField = new JTextField();
		panel_1.add(distanceField);
		distanceField.setColumns(14);
		JLabel lbl_time = new JLabel("temps d'execution:");
		panel_1.add(lbl_time);
		
		timeField = new JTextField();
		panel_1.add(timeField);
		timeField.setColumns(14);
		
		backBtn = new JButton("Retour");
		panel_1.add(backBtn);
		
		solutionChart = new JPanel();
		solutionChart.setBorder(new TitledBorder(null, "Carte du Solution :", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(solutionChart, BorderLayout.CENTER);
		solutionChart.setLayout(new BorderLayout(0, 0));
	}

}
