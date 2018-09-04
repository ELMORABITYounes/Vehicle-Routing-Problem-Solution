package vrp;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


import model.*;

public class SolomonReader {
	private String typeProbleme;
	private int nbrVehicles;
	private int nbrCustomers;
	private Node[] customers;
	
	public SolomonReader(String PATH) throws FileNotFoundException {
		super();
		// TODO Auto-generated constructor stub
		 Scanner input ;
		 File file;
		 ArrayList<Node> list=new ArrayList<Node>();
	            file = new File(PATH);
	            input = new Scanner(file);
	            int cnt=0;

	            while (input.hasNextLine()) {
	            	cnt++;
	            	String line=input.nextLine();
	            	if(cnt==1) {
	            		typeProbleme = line;
	            	}
	                if(cnt==5) {
	                	
	                	Scanner lineScanner=new Scanner(line);
	                	nbrVehicles=lineScanner.nextInt();
	                	lineScanner.close();
	                }else if(cnt>9) {
	                	//System.out.println(line);
	                	Scanner lineScanner=new Scanner(line);
	                	if(lineScanner.hasNextInt()) {
	                	Node n=new Node();
	                	n.setNodeId(lineScanner.nextInt());
	                	n.setNode_X(lineScanner.nextInt());
	                	n.setNode_Y(lineScanner.nextInt());
	                	lineScanner.nextInt();
	                	n.setReadyTime(lineScanner.nextInt());
	                	n.setCloseTime(lineScanner.nextInt());
	                	n.setServiceTime(lineScanner.nextInt());
	                	list.add(n);
	                	}
	                	lineScanner.close();
	                }
	            }
	            nbrCustomers=list.size()-1;
	            customers=list.toArray(new Node[0]);
	            input.close();
	}

	public int getNbrVehicles() {
		return nbrVehicles;
	}

	public int getNbrCustomers() {
		return nbrCustomers;
	}

	public Node[] getCustomers() {
		return customers;
	}
	public long[][] getDistanceMatrix(){
		long[][] distanceMatrix = new long[nbrCustomers + 1][nbrCustomers + 1];
	    double Delta_x, Delta_y;
	    for (int i = 0; i <= nbrCustomers; i++) {
	        for (int j = i + 1; j <= nbrCustomers; j++) //The table is symmetric to the first diagonal
	        {                                      //Use this to compute distances in O(n/2)

	            Delta_x = (customers[i].getNode_X() - customers[j].getNode_X());
	            Delta_y = (customers[i].getNode_Y() - customers[j].getNode_Y());

	            double distance = Math.sqrt((Delta_x * Delta_x) + (Delta_y * Delta_y));

	            long distanceInt = Math.round(distance);                //Distance is Casted in Integer
	            //distance = Math.round(distance*100.0)/100.0; //Distance in double

	            distanceMatrix[i][j] = distanceInt;
	            distanceMatrix[j][i] = distanceInt;
	        }
	    }
	    
	    return distanceMatrix;
	}

	public String getTypeProbleme() {
		return typeProbleme;
	}
	

}
