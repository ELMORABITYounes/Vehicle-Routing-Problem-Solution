package vrp;

import java.io.PrintWriter;
import java.util.ArrayList;

import model.Node;
import model.Vehicle;

public class Solution {
	public int NoOfVehicles;
	public int NoOfCustomers;
	public Vehicle[] Vehicles;
	public Long Cost;
	public long time;
	
	// Tabu Variables
	public Vehicle[] VehiclesForBestSolution;
	long BestSolutionCost;

	public ArrayList<String> PastSolutions;

	public ArrayList<String> getPastSolutions() {
		return PastSolutions;
	}
	
	/*
	 * The initialization of the problem
	 */
	public Solution(int noOfVehicles, int noOfCustomers) {
		super();
		NoOfVehicles = noOfVehicles;
		NoOfCustomers = noOfCustomers;
		this.Cost = 0l;
		Vehicles = new Vehicle[NoOfVehicles];

		VehiclesForBestSolution = new Vehicle[NoOfVehicles];
		PastSolutions = new ArrayList<>();
		for (int i = 0; i < NoOfVehicles; i++) {
			Vehicles[i] = new Vehicle(i + 1);
			VehiclesForBestSolution[i] = new Vehicle(i + 1);
		}
	}

	public void init() {
		this.Cost = 0l;
		Vehicles = new Vehicle[NoOfVehicles];

		VehiclesForBestSolution = new Vehicle[NoOfVehicles];
		PastSolutions = new ArrayList<>();

		for (int i = 0; i < NoOfVehicles; i++) {
			Vehicles[i] = new Vehicle(i + 1);
			VehiclesForBestSolution[i] = new Vehicle(i + 1);
		}
	}
	
	/*
	 * calculate the number of vehicles used in the solution
	 */
	public int getNbrUsedVehicles() {
		int nbrUsedVehicles = 0;
		for (int i = 0; i < Vehicles.length; i++) {
			if (!Vehicles[i].Route.isEmpty() && Vehicles[i].Route.size() > 2) {
				nbrUsedVehicles++;
			}
		}
		return nbrUsedVehicles;
	}

	/*
	 * check if we visited all the nodes
	 */
	public boolean UnassignedCustomerExists(Node[] Nodes) {
		for (int i = 1; i < Nodes.length; i++) {
			if (!Nodes[i].isRouted())
				return true;
		}
		return false;
	}

	public void GreedySolution(Node[] Nodes, long[][] CostMatrix) {
		long CandCost,
				// the cost of the comeback to the depot
				EndCost;
		int VehIndex = 0;

		while (UnassignedCustomerExists(Nodes)) {
			// we are in the depot at the beginning
			int CustIndex = 0;
			/*
			 * we initialize the cost with the infinity value
			 */
			long minCost = Long.MAX_VALUE;

			/*
			 * the candidate node
			 */
			Node Candidate = null;

			if (Vehicles[VehIndex].Route.isEmpty()) {
				Vehicles[VehIndex].AddNode(Nodes[0], CostMatrix);
			}

			for (int i = 1; i <= NoOfCustomers; i++) {
				if (!Nodes[i].isRouted()) {
					if (Vehicles[VehIndex].CheckIfFits(Nodes[i], CostMatrix, Nodes[0].getCloseTime())) {
						CandCost = CostMatrix[Vehicles[VehIndex].CurLoc][i];
						if (minCost > CandCost) {
							minCost = CandCost;
							CustIndex = i;
							Candidate = Nodes[i];
						}
					}
				}
			}

			if (Candidate == null) {
				// Not a single Customer Fits
				if (VehIndex + 1 < Vehicles.length) // We have more vehicles to assign
				{
					if (Vehicles[VehIndex].CurLoc != 0) {// End this route
						EndCost = CostMatrix[Vehicles[VehIndex].CurLoc][0];
						Vehicles[VehIndex].AddNode(Nodes[0], CostMatrix);
						this.Cost += EndCost;
					}
					VehIndex = VehIndex + 1; // Go to next Vehicle
				} else // We DO NOT have any more vehicle to assign. The problem is unsolved under
						// these parameters
				{
					System.out.println("\nThe rest customers do not fit in any Vehicle\n"
							+ "The problem cannot be resolved under these constrains");
					return;
				}
			} else {
				Vehicles[VehIndex].AddNode(Candidate, CostMatrix);// If a fitting Customer is Found
				Nodes[CustIndex].setRouted(true);
				this.Cost += minCost;
			}
		}

		EndCost = CostMatrix[Vehicles[VehIndex].CurLoc][0];
		Vehicles[VehIndex].AddNode(Nodes[0], CostMatrix);
		this.Cost += EndCost;

	}

	public void TabuSearch(int TABU_Horizon, long[][] CostMatrix, Node[] Nodes) {
		// ejection or inter-route
		// We use 1-0 exchange move
		ArrayList<Node> RouteFrom;
		ArrayList<Node> RouteTo;

		// the vehicle represent a route
		// the vehicle from where we want to move the node and the one we want to move to
		int VehIndexFrom, VehIndexTo;
		long BestNCost, NeigthboorCost;

		int SwapIndexA = -1, SwapIndexB = -1, SwapRouteFrom = -1, SwapRouteTo = -1;

		int MAX_ITERATIONS = 200;
		int iteration_number = 0,nbrUsedVehicles=getNbrUsedVehicles();
		int bestNbrUsedVehicles=nbrUsedVehicles;
		long time = getDureeUtitlise();
		long minusTime, addedTime, bestTiming = time;
		int DimensionCustomer = CostMatrix[1].length;
		int TABU_Matrix[][] = new int[DimensionCustomer + 1][DimensionCustomer + 1];

		BestSolutionCost = this.Cost; // Initial Solution Cost

		boolean Termination = false;

		while (!Termination) {
			iteration_number++;
			BestNCost = Long.MAX_VALUE;
			int l = this.Vehicles.length;
			for (VehIndexFrom = 0; VehIndexFrom < l; VehIndexFrom++) {
				RouteFrom = this.Vehicles[VehIndexFrom].Route;
				int RoutFromLength = RouteFrom.size();
				for (int i = 1; i < RoutFromLength - 1; i++) { // Not possible to move depot!

					for (VehIndexTo = 0; VehIndexTo < this.Vehicles.length; VehIndexTo++) {
						RouteTo = this.Vehicles[VehIndexTo].Route;
						int RouteTolength = RouteTo.size();
						for (int j = 0; (j < RouteTolength - 1); j++) {// Not possible to move after last Depot!

							if (this.Vehicles[VehIndexTo].CheckIfFitsAfter(RouteFrom.get(i), j, CostMatrix,
									Nodes[0].getCloseTime())) {

								if (!((VehIndexFrom == VehIndexTo) && ((j == i) || (j == i - 1)))) // Not a move that
																									// Changes solution
																									// cost
								{
									long MinusCost1 = CostMatrix[RouteFrom.get(i - 1).getNodeId()][RouteFrom.get(i)
											.getNodeId()];
									long MinusCost2 = CostMatrix[RouteFrom.get(i).getNodeId()][RouteFrom.get(i + 1)
											.getNodeId()];
									long MinusCost3 = CostMatrix[RouteTo.get(j).getNodeId()][RouteTo.get(j + 1)
											.getNodeId()];

									long AddedCost1 = CostMatrix[RouteFrom.get(i - 1).getNodeId()][RouteFrom.get(i + 1)
											.getNodeId()];
									long AddedCost2 = CostMatrix[RouteTo.get(j).getNodeId()][RouteFrom.get(i)
											.getNodeId()];
									long AddedCost3 = CostMatrix[RouteFrom.get(i).getNodeId()][RouteTo.get(j + 1)
											.getNodeId()];

									// Check if the move is a Tabu! - If it is Tabu break
									if ((TABU_Matrix[RouteFrom.get(i - 1).getNodeId()][RouteFrom.get(i + 1)
											.getNodeId()] != 0)
											|| (TABU_Matrix[RouteTo.get(j).getNodeId()][RouteFrom.get(i)
													.getNodeId()] != 0)
											|| (TABU_Matrix[RouteFrom.get(i).getNodeId()][RouteTo.get(j + 1)
													.getNodeId()] != 0)) {
										break;
									}

									NeigthboorCost = AddedCost1 + AddedCost2 + AddedCost3 - MinusCost1 - MinusCost2
											- MinusCost3;
									
									/**
									 * calculate the cost of the new Neigthboor (not the whole cost just the 
									 * difference between the cost of the initial solution and the actual solution)
									 */
									if(RouteFrom.size()==3) {
										BestNCost = NeigthboorCost;
										SwapIndexA = i;
										SwapIndexB = j;
										SwapRouteFrom = VehIndexFrom;
										SwapRouteTo = VehIndexTo;
									}else
									if(time<bestTiming)
									{
										BestNCost = NeigthboorCost;
										SwapIndexA = i;
										SwapIndexB = j;
										SwapRouteFrom = VehIndexFrom;
										SwapRouteTo = VehIndexTo;
										// memories the informations about the move that we made
									}else if(time==bestTiming)
	                                  if (NeigthboorCost < BestNCost) {
										BestNCost = NeigthboorCost;
										SwapIndexA = i;
										SwapIndexB = j;
										SwapRouteFrom = VehIndexFrom;
										SwapRouteTo = VehIndexTo;
										// memories the informations about the move that we made
									}
								}
							}
						}
					}
				}
			}

			// for each iteration we reduce the value of the tabu matrix
			for (int o = 0; o < TABU_Matrix[0].length; o++) {
				for (int p = 0; p < TABU_Matrix[0].length; p++) {
					if (TABU_Matrix[o][p] > 0) {
						TABU_Matrix[o][p]--;
					}
				}
			}

			if (SwapIndexA != -1 && SwapIndexB != -1 && SwapRouteFrom != -1 && SwapRouteTo != -1) {
				// we memorize the routes that are concerned by the move that we will made so
				// that we can rebuild them
				RouteFrom = this.Vehicles[SwapRouteFrom].Route;
				RouteTo = this.Vehicles[SwapRouteTo].Route;
				minusTime = this.Vehicles[SwapRouteFrom].calculPassedTime(CostMatrix)
						+ this.Vehicles[SwapRouteTo].calculPassedTime(CostMatrix);
				this.Vehicles[SwapRouteFrom].Route = null;
				this.Vehicles[SwapRouteTo].Route = null;

				// we get the ids of the nodes concerned by the move so that we can memorize the
				// tabu move
				Node SwapNode = RouteFrom.get(SwapIndexA);
				int NodeIDBefore = RouteFrom.get(SwapIndexA - 1).getNodeId();
				int NodeIDAfter = RouteFrom.get(SwapIndexA + 1).getNodeId();
				int NodeID_F = RouteTo.get(SwapIndexB).getNodeId();
				int NodeID_G = RouteTo.get(SwapIndexB + 1).getNodeId();

				TABU_Matrix[NodeIDBefore][SwapNode.getNodeId()] = TABU_Horizon;
				TABU_Matrix[SwapNode.getNodeId()][NodeIDAfter] = TABU_Horizon;
				TABU_Matrix[NodeID_F][NodeID_G] = TABU_Horizon;

				// we apply the move
				RouteFrom.remove(SwapIndexA);

				if (SwapRouteFrom == SwapRouteTo) {
					if (SwapIndexA < SwapIndexB) {
						RouteTo.add(SwapIndexB, SwapNode);
					} else {
						RouteTo.add(SwapIndexB + 1, SwapNode);
					}
				} else {
					RouteTo.add(SwapIndexB + 1, SwapNode);
				}

				if (RouteFrom.size() <= 2)
					RouteFrom.clear();
				// we recalculate the passed time of the vehicles
				this.Vehicles[SwapRouteFrom].Route = RouteFrom;
				this.Vehicles[SwapRouteFrom].updatePassedTime(CostMatrix);

				this.Vehicles[SwapRouteTo].Route = RouteTo;
				this.Vehicles[SwapRouteTo].updatePassedTime(CostMatrix);

				// we memorize the cost
				PastSolutions.add("V=" + this.getNbrUsedVehicles() + " :T=" + time + " :D=" + this.Cost);

				addedTime = this.Vehicles[SwapRouteFrom].calculPassedTime(CostMatrix)
						+ this.Vehicles[SwapRouteTo].calculPassedTime(CostMatrix);
				time = time - minusTime + addedTime;

				// add the difference to the cost it might be positive or negative

				this.Cost += BestNCost;
				nbrUsedVehicles=getNbrUsedVehicles();
				if(nbrUsedVehicles<bestNbrUsedVehicles) {
					bestNbrUsedVehicles=nbrUsedVehicles;
					SaveBestSolution();
				}else if(nbrUsedVehicles==bestNbrUsedVehicles) {
					if (time < bestTiming) {
						bestTiming = time;
						SaveBestSolution();
					}
					else if (time == bestTiming) {
						if (this.Cost < BestSolutionCost) {
							SaveBestSolution();
						}
					}
				}
				
				SwapIndexA = -1;
				SwapIndexB = -1;
				SwapRouteFrom = -1;
				SwapRouteTo = -1;
			}

			if (iteration_number == MAX_ITERATIONS) {
				Termination = true;
			}
		}

		PastSolutions.add("V=" + this.getNbrUsedVehicles() + " T=" + time + " D=" + this.Cost);

		try {
			PrintWriter writer = new PrintWriter("PastSolutionsTabu.txt", "UTF-8");
			writer.println("Solutions" + "\t");
			for (int i = 0; i < PastSolutions.size(); i++) {
				writer.println(PastSolutions.get(i) + "\t");
			}
			writer.close();
		} catch (Exception e) {
		}
	}

	public void InterRouteLocalSearch(Node[] Nodes, long[][] CostMatrix) {
		// We use 1-0 exchange move
		ArrayList<Node> RouteFrom;
		ArrayList<Node> RouteTo;

		int VehIndexFrom, VehIndexTo;
		long BestNCost, NeigthboorCost;

		int SwapIndexA = -1, SwapIndexB = -1, SwapRouteFrom = -1, SwapRouteTo = -1;

		int MAX_ITERATIONS = 1000000;
		int iteration_number = 0;

		time = getDureeUtitlise();
		long minusTime, addedTime,bestTiming = time;

		boolean Termination = false;

		while (!Termination) {
			iteration_number++;
			BestNCost = Long.MAX_VALUE;

			for (VehIndexFrom = 0; VehIndexFrom < this.Vehicles.length; VehIndexFrom++) {
				RouteFrom = this.Vehicles[VehIndexFrom].Route;
				int RoutFromLength = RouteFrom.size();
				for (int i = 1; i < RoutFromLength - 1; i++) { // Not possible to move depot!

					for (VehIndexTo = 0; VehIndexTo < this.Vehicles.length; VehIndexTo++) {
						RouteTo = this.Vehicles[VehIndexTo].Route;
						int RouteTolength = RouteTo.size();
						for (int j = 0; (j < RouteTolength - 1); j++) {// Not possible to move after last Depot!

							if (this.Vehicles[VehIndexTo].CheckIfFitsAfter(RouteFrom.get(i), j, CostMatrix,
									Nodes[0].getCloseTime())) {
								if (((VehIndexFrom == VehIndexTo) && ((j == i) || (j == i - 1))) == false) // Not a move
																											// that
																											// Changes
																											// solution
																											// cost
								{
									long MinusCost1 = CostMatrix[RouteFrom.get(i - 1).getNodeId()][RouteFrom.get(i)
											.getNodeId()];
									long MinusCost2 = CostMatrix[RouteFrom.get(i).getNodeId()][RouteFrom.get(i + 1)
											.getNodeId()];
									long MinusCost3 = CostMatrix[RouteTo.get(j).getNodeId()][RouteTo.get(j + 1)
											.getNodeId()];

									long AddedCost1 = CostMatrix[RouteFrom.get(i - 1).getNodeId()][RouteFrom.get(i + 1)
											.getNodeId()];
									long AddedCost2 = CostMatrix[RouteTo.get(j).getNodeId()][RouteFrom.get(i)
											.getNodeId()];
									long AddedCost3 = CostMatrix[RouteFrom.get(i).getNodeId()][RouteTo.get(j + 1)
											.getNodeId()];

									NeigthboorCost = AddedCost1 + AddedCost2 + AddedCost3 - MinusCost1 - MinusCost2
											- MinusCost3;

									if (NeigthboorCost < BestNCost) {
										BestNCost = NeigthboorCost;
										SwapIndexA = i;
										SwapIndexB = j;
										SwapRouteFrom = VehIndexFrom;
										SwapRouteTo = VehIndexTo;

									}
								}
							}
						}
					}
				}
			}

			if (SwapIndexA != -1 && SwapIndexB != -1 && SwapRouteFrom != -1 && SwapRouteTo != -1) {

				
				

					RouteFrom = this.Vehicles[SwapRouteFrom].Route;
					RouteTo = this.Vehicles[SwapRouteTo].Route;
					minusTime = this.Vehicles[SwapRouteFrom].calculPassedTime(CostMatrix)
							+ this.Vehicles[SwapRouteTo].calculPassedTime(CostMatrix);
					this.Vehicles[SwapRouteFrom].Route = null;
					this.Vehicles[SwapRouteTo].Route = null;

					Node SwapNode = RouteFrom.get(SwapIndexA);

					RouteFrom.remove(SwapIndexA);

					if (SwapRouteFrom == SwapRouteTo) {
						if (SwapIndexA < SwapIndexB) {
							RouteTo.add(SwapIndexB, SwapNode);
						} else {
							RouteTo.add(SwapIndexB + 1, SwapNode);
						}
					} else {
						RouteTo.add(SwapIndexB + 1, SwapNode);
					}

					if (RouteFrom.size() <= 2)
						RouteFrom.clear();
					this.Vehicles[SwapRouteFrom].Route = RouteFrom;
					this.Vehicles[SwapRouteFrom].updatePassedTime(CostMatrix);

					this.Vehicles[SwapRouteTo].Route = RouteTo;
					this.Vehicles[SwapRouteTo].updatePassedTime(CostMatrix);
					this.Cost += BestNCost;
					addedTime = this.Vehicles[SwapRouteFrom].calculPassedTime(CostMatrix)
							+ this.Vehicles[SwapRouteTo].calculPassedTime(CostMatrix);
					time = time - minusTime + addedTime;
					// we memorize the cost
					if(time<bestTiming) {
						bestTiming=time;
						PastSolutions.add("V=" + this.getNbrUsedVehicles() + " :T=" + time + " :D=" + this.Cost);
						
					}else if (BestNCost == 0) {
						if (BestNCost < 0) {
							PastSolutions.add("V=" + this.getNbrUsedVehicles() + " :T=" + time + " :D=" + this.Cost);
						} else {
							Termination = true;
						}
					} else {
						Termination = true;
					}

				if (iteration_number == MAX_ITERATIONS) {
					Termination = true;
				}
				SwapIndexA = -1;
				SwapIndexB = -1;
				SwapRouteFrom = -1;
				SwapRouteTo = -1;
			}
		}
		PastSolutions.add("V=" + this.getNbrUsedVehicles() + " :T=" + time + " :D=" + this.Cost);
		try {
			PrintWriter writer = new PrintWriter("PastSolutionsInter.txt", "UTF-8");
			for (int i = 0; i < PastSolutions.size(); i++) {
				writer.println(PastSolutions.get(i) + "\t");
			}
			writer.close();
		} catch (Exception e) {
		}
	}

	public void IntraRouteLocalSearch(Node[] Nodes, long[][] CostMatrix) {
		// We use 1-0 exchange move
		ArrayList<Node> rt;
		long BestNCost, NeigthboorCost;

		int SwapIndexA = -1, SwapIndexB = -1, SwapRoute = -1;

		int MAX_ITERATIONS = 1000000;
		int iteration_number = 0;

		time = getDureeUtitlise();
		long minusTime, addedTime,bestTiming=time;

		boolean Termination = false;

		while (!Termination) {
			iteration_number++;
			BestNCost = Long.MAX_VALUE;

			for (int VehIndex = 0; VehIndex < this.Vehicles.length; VehIndex++) {
				rt = this.Vehicles[VehIndex].Route;
				int RoutLength = rt.size();

				for (int i = 1; i < RoutLength - 1; i++) { // Not possible to move depot!

					for (int j = 0; (j < RoutLength - 1); j++) {// Not possible to move after last Depot!

						if ((j != i) && (j != i - 1)) { // Not a move that cHanges solution cost

							long MinusCost1 = CostMatrix[rt.get(i - 1).getNodeId()][rt.get(i).getNodeId()];
							long MinusCost2 = CostMatrix[rt.get(i).getNodeId()][rt.get(i + 1).getNodeId()];
							long MinusCost3 = CostMatrix[rt.get(j).getNodeId()][rt.get(j + 1).getNodeId()];

							long AddedCost1 = CostMatrix[rt.get(i - 1).getNodeId()][rt.get(i + 1).getNodeId()];
							long AddedCost2 = CostMatrix[rt.get(j).getNodeId()][rt.get(i).getNodeId()];
							long AddedCost3 = CostMatrix[rt.get(i).getNodeId()][rt.get(j + 1).getNodeId()];

							NeigthboorCost = AddedCost1 + AddedCost2 + AddedCost3 - MinusCost1 - MinusCost2
									- MinusCost3;

							if (NeigthboorCost < BestNCost) {
								BestNCost = NeigthboorCost;
								SwapIndexA = i;
								SwapIndexB = j;
								SwapRoute = VehIndex;

							}
						}
					}
				}
			}
			
			minusTime = this.Vehicles[SwapRoute].calculPassedTime(CostMatrix);
			rt = this.Vehicles[SwapRoute].Route;

			Node SwapNode = rt.get(SwapIndexA);

			rt.remove(SwapIndexA);

			if (SwapIndexA < SwapIndexB) {
				rt.add(SwapIndexB, SwapNode);
			} else {
				rt.add(SwapIndexB + 1, SwapNode);
			}
			
			addedTime=this.Vehicles[SwapRoute].calculPassedTime(CostMatrix);
			time=time-minusTime+addedTime;
			if(time<bestTiming) {
				bestTiming=time;
				PastSolutions.add("V=" + this.getNbrUsedVehicles() + " :T=" + time + " :D=" + this.Cost);
				this.Cost += BestNCost;
				this.Vehicles[SwapRoute].updatePassedTime(CostMatrix);
				addedTime = this.Vehicles[SwapRoute].calculPassedTime(CostMatrix);
				time = time - minusTime + addedTime;
			}else if (BestNCost == 0) {
				if (BestNCost < 0) {
					PastSolutions.add("V=" + this.getNbrUsedVehicles() + " :T=" + time + " :D=" + this.Cost);
					this.Cost += BestNCost;
					this.Vehicles[SwapRoute].updatePassedTime(CostMatrix);
					addedTime = this.Vehicles[SwapRoute].calculPassedTime(CostMatrix);
					time = time - minusTime + addedTime;
				} else {
					Termination = true;
				}
			} else {
				Termination = true;
			}

			if (iteration_number == MAX_ITERATIONS) {
				Termination = true;
			}
		}
		try {
			PrintWriter writer = new PrintWriter("PastSolutionsIntra.txt", "UTF-8");
			for (int i = 0; i < PastSolutions.size(); i++) {
				writer.println(PastSolutions.get(i) + "\t");
			}
			writer.close();
		} catch (Exception e) {
		}
	}

	public void SaveBestSolution() {
		BestSolutionCost = Cost;
		for (int j = 0; j < NoOfVehicles; j++) {
			VehiclesForBestSolution[j].Route.clear();
			if (!Vehicles[j].Route.isEmpty()) {
				int RoutSize = Vehicles[j].Route.size();
				for (int k = 0; k < RoutSize; k++) {
					Node n = Vehicles[j].Route.get(k);
					VehiclesForBestSolution[j].Route.add(n);
				}
			}
		}
	}

	public void SolutionPrint(String Solution_Label)// Print Solution In console
	{
		System.out.println("=========================================================");
		System.out.println(Solution_Label + "\n");

		for (int j = 0; j < NoOfVehicles; j++) {
			if (!Vehicles[j].Route.isEmpty()) {
				System.out.print("Vehicle " + (j + 1) + ":");
				int RoutSize = Vehicles[j].Route.size();
				for (int k = 0; k < RoutSize; k++) {
					if (k == RoutSize - 1) {
						System.out.print(
								Vehicles[j].Route.get(k).getNodeId() + "[" + Vehicles[j].Route.get(k).getReadyTime()
										+ "-" + Vehicles[j].Route.get(k).getCloseTime() + "]");
					} else if (k == 0) {
						System.out.print(Vehicles[j].Route.get(k).getNodeId() + "->");
					} else {
						System.out.print(
								Vehicles[j].Route.get(k).getNodeId() + "[" + Vehicles[j].Route.get(k).getReadyTime()
										+ "-" + Vehicles[j].Route.get(k).getCloseTime() + "]->");
					}
				}
				System.out.println();
			}
		}
		System.out.println("\nSolution Cost " + this.Cost + "\n");
		System.out.println("\nUsed Vehicles " + this.getNbrUsedVehicles() + "\n");
		System.out.println("\nduree écoulé " + this.getDureeUtitlise() + "\n");
	}

	@Override
	public String toString() {
		String sol = "";
		for (int j = 0; j < NoOfVehicles; j++) {
			if (!Vehicles[j].Route.isEmpty()) {
				sol += "Vehicle " + (j + 1) + ": ";
				int RoutSize = Vehicles[j].Route.size();
				for (int k = 0; k < RoutSize; k++) {
					if (k == RoutSize - 1) {
						sol += " " + Vehicles[j].Route.get(k).getNodeId() + "["
								+ Vehicles[j].Route.get(k).getReadyTime() + "-"
								+ Vehicles[j].Route.get(k).getCloseTime() + "]";
					} else if (k == 0) {
						sol += " " + Vehicles[j].Route.get(k).getNodeId() + "->";
					} else {
						sol += " " + Vehicles[j].Route.get(k).getNodeId() + "["
								+ Vehicles[j].Route.get(k).getReadyTime() + "-"
								+ Vehicles[j].Route.get(k).getCloseTime() + "] ->";
					}
				}
				sol += "\n";
			}
		}
		if (!isSolvable()) {
			sol += "\nThe rest customers do not fit in any Vehicle\n"
					+ "The problem cannot be resolved under these constrains";
		}
		return sol;
	}

	public boolean isSolvable() {
		int count = 0;
		for (int j = 0; j < NoOfVehicles; j++) {
			if (!Vehicles[j].Route.isEmpty() && Vehicles[j].Route.size() > 2) {
				count += Vehicles[j].Route.size() - 2;
			}
		}
		return count == NoOfCustomers;
	}

	public long getDureeUtitlise() {
		long t = 0;
		for (int j = 0; j < NoOfVehicles; j++) {
			if (!Vehicles[j].Route.isEmpty()) {
				t += Vehicles[j].passedTime;
			}
		}
		return t;
	}

}
