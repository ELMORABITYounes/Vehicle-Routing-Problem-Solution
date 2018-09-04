import com.alee.laf.WebLookAndFeel;

import controller.Controlleur;
import presentation.Fenetre;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebLookAndFeel.install();
		new Fenetre(new Controlleur());
	}

}
