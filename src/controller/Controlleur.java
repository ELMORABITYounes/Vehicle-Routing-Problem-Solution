package controller;

import vrp.*;
import model.*;
import presentation.*;

public class Controlleur {
	Fenetre home;
	InitSolutionFenetre initSolutionFenetre;
	TabuSolutionFenetre tabuSolutionFenetre;
	DescenteSolutionFenetre descenteSolutionFenetre;
	public Controlleur() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Fenetre getHome() {
		return home;
	}
	public void setHome(Fenetre home) {
		this.home = home;
	}
	public InitSolutionFenetre getInitSolutionFenetre() {
		return initSolutionFenetre;
	}
	public void setInitSolutionFenetre(InitSolutionFenetre initSolutionFenetre) {
		this.initSolutionFenetre = initSolutionFenetre;
	}
	
	public void showInitSolution(Solution s,SolomonReader solomon,long time) {
		initSolutionFenetre=new InitSolutionFenetre(this,s,solomon,time);
	}
	public void backFunction() {
		home.setVisible(true);
	}
	
	public void showTabuSolution(Solution s,SolomonReader solomon,long time) {
		tabuSolutionFenetre=new TabuSolutionFenetre(this,s,solomon,time);
	}
	
	public void showDescenteSolution(Solution s,SolomonReader solomon,long time) {
		descenteSolutionFenetre=new DescenteSolutionFenetre(this,s,solomon,time);
	}
}
