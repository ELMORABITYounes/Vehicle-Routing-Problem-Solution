package presentation;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JPanel;


import vrp.*;
import model.*;

public class SolutionChartPanel extends JPanel {
	Random r=new Random();
	SolomonReader solomon;
	Solution s;
	Color[] c= {Color.BLACK,Color.RED,Color.GREEN,Color.BLUE,
			new Color(194, 155, 0),new Color(194, 73, 253),new Color(123, 121, 64),
			new Color(123, 14, 64),new Color(161, 205, 64),new Color(205, 75, 204),
			new Color(255, 128, 0),new Color(255, 0, 255),
			new Color(210,105,30),new Color(139,0,139),new Color(255,140,0)};
	/**
	 * Create the panel.
	 */
	public SolutionChartPanel(SolomonReader solomon,Solution sol) {
		this.solomon=solomon;
		s=sol;
	}
	
	public void paintComponent(Graphics gf){
		Graphics2D g = (Graphics2D)gf;
			g.setStroke(new BasicStroke(2));
		 	int margin = 30;
	        int marginNode = 1;


	        int XXX = this.getWidth();
	        int YYY = this.getHeight();

	        g.setColor(Color.WHITE);
	        g.fillRect(0, 0, XXX, YYY);
	        g.setColor(Color.BLACK);


	        double minX = Double.MAX_VALUE;
	        double maxX = Double.MIN_VALUE;
	        double minY = Double.MAX_VALUE;
	        double maxY = Double.MIN_VALUE;

	        for (int i = 0; i < solomon.getNbrCustomers()+1; i++)
            {
                Node n = solomon.getCustomers()[i];
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
	        
	        if(s.isSolvable()) {
	        // Draw Route
	        for (int i = 0; i < s.Vehicles.length ; i++)
	        {g.setColor(c[i%15]);
	            for (int j = 1; j < s.Vehicles[i].Route.size() ; j++) {
	            	
	                Node n;
	                n = s.Vehicles[i].Route.get(j-1);

	                int ii1 = (int) ((double) (A) * ((n.getNode_X() - minX) / (maxX - minX) - 0.5) + (double) mX / 2) + margin;
	                int jj1 = (int) ((double) (B) * (0.5 - (n.getNode_Y() - minY) / (maxY - minY)) + (double) mY / 2) + margin;

	                n = s.Vehicles[i].Route.get(j);
	                int ii2 = (int) ((double) (A) * ((n.getNode_X() - minX) / (maxX - minX) - 0.5) + (double) mX / 2) + margin;
	                int jj2 = (int) ((double) (B) * (0.5 - (n.getNode_Y() - minY) / (maxY - minY)) + (double) mY / 2) + margin;


	                g.drawLine(ii1, jj1, ii2, jj2);
	            }
	        }
	        

	        for (int i = 0; i < solomon.getNbrCustomers()+1; i++)
	         {
	             Node n = solomon.getCustomers()[i];

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
	        }else {
	        	g.setColor(Color.RED);
	        	Font font = new Font("Courier", Font.BOLD, 16);
	        	g.setFont(font);
	            g.drawString("The rest customers do not fit in any Vehicle", XXX/13, YYY/2);
	            g.drawString("The problem cannot be solved under these constrains", XXX/14, YYY/2+20);
	        }
	        

		}


}
