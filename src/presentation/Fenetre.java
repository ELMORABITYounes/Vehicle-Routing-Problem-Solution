package presentation;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


import vrp.*;
import model.*;
import controller.Controlleur;


public class Fenetre extends JFrame {

	private JPanel contentPane;
	private JButton buttonFolder;
	private JTextField textFieldFile;
	private JLabel lblNewLabel;
	private JButton readBtn;
	private JButton initSolutionBtn;
	private JButton btnNewButton_2;
	private JButton btnNewButton_4;
	private JButton btnMthodeRcuitSimul;
	private JButton btnMthodeGnitique;
	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel lblNewLabel_1;
	private JTextField tyeTextField;
	private JTextField nbrVehiclesField;
	private JLabel lblNewLabel_2;
	private JTextField nbrClientsField;
	private JLabel lblNewLabel_3;
	private JPanel chart;
	private SolomonReader instance=null;
	private JPanel panel_3;
	private JLabel lblNewLabel_4;
	private Controlleur cont;
	private JButton btnMthodeDscenterearangement;

	/**
	 * Create the frame.
	 */
	public Fenetre(Controlleur c) {
		//WebLookAndFeel.install();
		cont=c;
		cont.setHome(this);
		initialize();
		this.setSize(700, 500);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
buttonFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc=new JFileChooser();
				fc.setCurrentDirectory(new File("doc"));
				int returnValue=fc.showOpenDialog(Fenetre.this);
				if(returnValue== JFileChooser.APPROVE_OPTION) {
					textFieldFile.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
readBtn.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
			if(!textFieldFile.getText().isEmpty()) {
				try {
				instance=new SolomonReader(textFieldFile.getText());
				}catch(InputMismatchException ex) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veulliez Choisir un fichier Solomon valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				}catch(FileNotFoundException ex) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veulliez Choisir un fichier Solomon valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
				if(instance!=null) {
					tyeTextField.setText(instance.getTypeProbleme());
					nbrVehiclesField.setText(String.valueOf(instance.getNbrVehicles()));
					nbrClientsField.setText(String.valueOf(instance.getNbrCustomers()));
					chart=new ChartPanel(instance);
					chart.setPreferredSize(new Dimension(panel_3.getWidth(),panel_3.getHeight()));
					panel_3.removeAll();
					panel_3.add(chart, BorderLayout.CENTER);
					panel_3.revalidate();
					panel_3.repaint();
				}
			}
	}
});

initSolutionBtn.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
		if(instance!=null) {
			try {
				instance=new SolomonReader(textFieldFile.getText());
				}catch(InputMismatchException ex) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veulliez Choisir un fichier Solomon valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				}catch(FileNotFoundException ex) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veulliez Choisir un fichier Solomon valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
		Solution s= new Solution(instance.getNbrVehicles(),instance.getNbrCustomers());
		long time = System.currentTimeMillis();
		s.GreedySolution(instance.getCustomers(), instance.getDistanceMatrix());
		cont.showInitSolution(s,instance,System.currentTimeMillis()-time);
		}
	}
});

btnNewButton_4.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
		if(instance!=null) {
			try {
				instance=new SolomonReader(textFieldFile.getText());
				}catch(InputMismatchException ex) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veulliez Choisir un fichier Solomon valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				}catch(FileNotFoundException ex) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veulliez Choisir un fichier Solomon valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
		Solution s= new Solution(instance.getNbrVehicles(),instance.getNbrCustomers());
		long time = System.currentTimeMillis();
		s.GreedySolution(instance.getCustomers(), instance.getDistanceMatrix());
        s.TabuSearch(40, instance.getDistanceMatrix(),instance.getCustomers());
		cont.showTabuSolution(s,instance,System.currentTimeMillis()-time);
		}
	}
});

btnNewButton_2.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
		if(instance!=null) {
			try {
				instance=new SolomonReader(textFieldFile.getText());
				}catch(InputMismatchException ex) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veulliez Choisir un fichier Solomon valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				}catch(FileNotFoundException ex) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veulliez Choisir un fichier Solomon valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
		Solution s= new Solution(instance.getNbrVehicles(),instance.getNbrCustomers());
		long time = System.currentTimeMillis();
		s.GreedySolution(instance.getCustomers(), instance.getDistanceMatrix());
        s.InterRouteLocalSearch(instance.getCustomers(),instance.getDistanceMatrix());
		cont.showDescenteSolution(s,instance,System.currentTimeMillis()-time);
		}
	}
});

btnMthodeDscenterearangement.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
		if(instance!=null) {
			try {
				instance=new SolomonReader(textFieldFile.getText());
				}catch(InputMismatchException ex) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veulliez Choisir un fichier Solomon valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				}catch(FileNotFoundException ex) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veulliez Choisir un fichier Solomon valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
		Solution s= new Solution(instance.getNbrVehicles(),instance.getNbrCustomers());
		long time = System.currentTimeMillis();
		s.GreedySolution(instance.getCustomers(), instance.getDistanceMatrix());
        s.IntraRouteLocalSearch(instance.getCustomers(),instance.getDistanceMatrix());
		cont.showDescenteSolution(s,instance,System.currentTimeMillis()-time);
		}
	}
});
	}
	
	public void initialize(){
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(15);
		panel.setPreferredSize(new Dimension(200, 0));
		
		lblNewLabel = new JLabel("S\u00E9lectioner Un fichier Solomon :");
		panel.add(lblNewLabel);
		
		
		textFieldFile = new JTextField();
        textFieldFile.setColumns(11);
        panel.add(textFieldFile);
        
        buttonFolder = new JButton("");
        buttonFolder.setPreferredSize(new Dimension(46,30));
        buttonFolder.setIcon(new ImageIcon("icones/icon.png"));
		panel.add(buttonFolder);
		 
		contentPane.add(panel, BorderLayout.EAST);
		
		readBtn = new JButton("Lire Le Ficheir");
		readBtn.setPreferredSize(new Dimension(150, 30));
		panel.add(readBtn);
		
		initSolutionBtn = new JButton("Solution Initiale");
		initSolutionBtn.setPreferredSize(new Dimension(180, 30));
		panel.add(initSolutionBtn);
		
		btnNewButton_2 = new JButton("M\u00E9thode d\u00E9scente(Ejection)");
		btnNewButton_2.setPreferredSize(new Dimension(180, 30));
		panel.add(btnNewButton_2);
		
		btnMthodeDscenterearangement = new JButton("M\u00E9thode d\u00E9scente(Rearangement)");
		
		btnMthodeDscenterearangement.setPreferredSize(new Dimension(180, 30));
		panel.add(btnMthodeDscenterearangement);
		
		btnNewButton_4 = new JButton("M\u00E9thode Tabou");
		
		btnNewButton_4.setPreferredSize(new Dimension(180, 30));
		panel.add(btnNewButton_4);
		
		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Informations du probleme :", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		panel_2.setPreferredSize(new Dimension(0, 60));
		panel_1.add(panel_2, BorderLayout.NORTH);
		
		lblNewLabel_1 = new JLabel("Type de Probleme :");
		panel_2.add(lblNewLabel_1);
		
		tyeTextField = new JTextField();
		tyeTextField.setEditable(false);
		panel_2.add(tyeTextField);
		tyeTextField.setColumns(3);
		
		lblNewLabel_2 = new JLabel("Nbr Vehicules :");
		panel_2.add(lblNewLabel_2);
		
		nbrVehiclesField = new JTextField();
		nbrVehiclesField.setEditable(false);
		panel_2.add(nbrVehiclesField);
		nbrVehiclesField.setColumns(3);
		
		lblNewLabel_3 = new JLabel("Nbr Clients :");
		panel_2.add(lblNewLabel_3);
		
		nbrClientsField = new JTextField();
		nbrClientsField.setEditable(false);
		panel_2.add(nbrClientsField);
		nbrClientsField.setColumns(3);
		
		panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Carte du Probleme :", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel_4 = new JLabel("Vieelliez Selectionner une instance pour voir la carte");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblNewLabel_4, BorderLayout.CENTER);
	}

}
