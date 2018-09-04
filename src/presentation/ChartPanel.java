package presentation;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import vrp.SolomonReader;
import vrp.Solution;

import model.*;

public class ChartPanel extends JPanel {
	SolomonReader s;
	/**
	 * Create the panel.
	 */
	public ChartPanel(SolomonReader s) {
		this.s=s;
	}
	
	public void paintComponent(Graphics gf){
		Graphics2D g = (Graphics2D)gf;
		 	int margin = 30;
	        int marginNode = 1;


	        int XXX = this.getWidth();
	        int YYY = this.getHeight();

	        g.setColor(Color.WHITE);
	        g.fillRect(0, 0, XXX, YYY);
	        //g.setColor(Color.RED);


	        double minX = Double.MAX_VALUE;
	        double maxX = Double.MIN_VALUE;
	        double minY = Double.MAX_VALUE;
	        double maxY = Double.MIN_VALUE;

	            for (int i = 0; i < s.getNbrCustomers()+1; i++)
	            {
	                Node n = s.getCustomers()[i];
	                if (n.getNode_X() > maxX) maxX = n.getNode_X();
	                if (n.getNode_X() < minX) minX = n.getNode_X();
	                if (n.getNode_Y() > maxY) maxY = n.getNode_Y();
	                if (n.getNode_Y() < minY) minY = n.getNode_Y();

	            }

	        int mX = XXX - 2 * margin;
	        int mY = YYY - 2 * margin;

	        int A, B;
	        if ((maxX - minX) > (maxY - minY))
	        {
	            A = mX;
	            B = (int)((double)(A) * (maxY - minY) / (maxX - minX));
	            if (B > mY)
	            {
	                B = mY;
	                A = (int)((double)(B) * (maxX - minX) / (maxY - minY));
	            }
	        }
	        else
	        {
	            B = mY;
	            A = (int)((double)(B) * (maxX - minX) / (maxY - minY));
	            if (A > mX)
	            {
	                A = mX;
	                B = (int)((double)(A) * (maxY - minY) / (maxX - minX));
	            }
	        }
	        
	        for (int i = 0; i < s.getNbrCustomers()+1; i++)
         {
             Node n = s.getCustomers()[i];

	                int ii = (int) ((double) (A) * ((n.getNode_X()  - minX) / (maxX - minX) - 0.5) + (double) mX / 2) + margin;
	                int jj = (int) ((double) (B) * (0.5 - (n.getNode_Y() - minY) / (maxY - minY)) + (double) mY / 2) + margin;
	                if (i == 0) {
	        	        g.setColor(Color.RED);
	                    g.fillOval(ii - 3 * marginNode, jj - 3 * marginNode, 10 * marginNode, 10 * marginNode); //2244
	                    g.drawString("Dépot", ii + 8 * marginNode, jj + 10 * marginNode); //88
	                } else {
	        	        g.setColor(Color.DARK_GRAY);
	                    g.fillRect(ii - 3 * marginNode, jj - 3 * marginNode, 6 * marginNode, 6 * marginNode);  //4488
	                    String id = Integer.toString(n.getNodeId());
	                    g.drawString(id, ii + 4 * marginNode, jj + 10 * marginNode); //88
	                }
         }
		}

}
