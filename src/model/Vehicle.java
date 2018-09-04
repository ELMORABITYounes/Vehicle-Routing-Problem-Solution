package model;
import java.util.ArrayList;

public class Vehicle {
	public int VehId;
	public ArrayList<Node> Route = new ArrayList<Node>();
	public int CurLoc;
	public long passedTime;

	public Vehicle(int id) {
		this.VehId = id;
		this.CurLoc = 0; // In depot Initially
		this.passedTime = 0;
		this.Route.clear();
	}

	public void AddNode(Node Customer, long[][] matrix)// Add Customer to Vehicle Route
	{
		Route.add(Customer);
		if (this.passedTime + matrix[CurLoc][Customer.getNodeId()] > Customer.getReadyTime()) {
			this.passedTime += matrix[CurLoc][Customer.getNodeId()];
		} else {
			this.passedTime = Customer.getReadyTime();
		}
		this.CurLoc = Customer.getNodeId();
	}

	public boolean CheckIfFits(Node Customer, long[][] matrix, int maxTime) // Check if the Window time suits
	{
		if (this.passedTime + matrix[CurLoc][Customer.getNodeId()] > Customer.getReadyTime()) {
			if ((this.passedTime + matrix[CurLoc][Customer.getNodeId()]) <= Customer.getCloseTime() && (this.passedTime
					+ matrix[CurLoc][Customer.getNodeId()] + matrix[Customer.getNodeId()][0]) <= maxTime)
				return true;
			return false;
		} else {
			return (Customer.getReadyTime() + matrix[Customer.getNodeId()][0]) < maxTime;
		}
	}

	private long calculPassedTimeIn(int index, long[][] matrix) {
		long t = 0;
		for (int i = 0; i < index; i++) {
			if (t + matrix[Route.get(i).getNodeId()][Route.get(i + 1).getNodeId()] > Route.get(i + 1).getReadyTime()) {
				t += matrix[Route.get(i).getNodeId()][Route.get(i + 1).getNodeId()];
			} else {
				t = Route.get(i + 1).getReadyTime();
			}
		}
		return t;
	}

	private boolean checkFromIndexToDepot(long[][] matrix, long time, int index, int maxTime) {
		long t = time;
		for (int i = index; i < Route.size() - 1; i++) {
			t += matrix[Route.get(i).getNodeId()][Route.get(i + 1).getNodeId()];
			if (t < Route.get(i + 1).getReadyTime())
				t = Route.get(i + 1).getReadyTime();
			if (t > Route.get(i + 1).getCloseTime())
				return false;
		}
		return true;
	}

	public boolean CheckIfFitsAfter(Node Customer, int index, long[][] matrix, int maxTime) // Check if the Window time
																							// suits
	{
		if (Customer.getReadyTime() > this.Route.get(index + 1).getReadyTime()
				|| Customer.getCloseTime() < this.Route.get(index).getReadyTime())
			return false;
		else {
			if (this.calculPassedTimeIn(index, matrix)
					+ matrix[Route.get(index).getNodeId()][Customer.getNodeId()] > Customer.getReadyTime()) {
				if ((this.calculPassedTimeIn(index, matrix)
						+ matrix[Route.get(index).getNodeId()][Customer.getNodeId()]) <= Customer.getCloseTime()
						&& (this.calculPassedTimeIn(index, matrix)
								+ matrix[Route.get(index).getNodeId()][Customer.getNodeId()]
								+ matrix[Customer.getNodeId()][Route.get(index + 1).getNodeId()]) <= Route
										.get(index + 1).getCloseTime())
					if (checkFromIndexToDepot(matrix,
							this.calculPassedTimeIn(index, matrix)
									+ matrix[Route.get(index).getNodeId()][Customer.getNodeId()]
									+ matrix[Customer.getNodeId()][Route.get(index + 1).getNodeId()],
							index + 1, maxTime))
						return true;
				return false;
			} else {
				return (Customer.getReadyTime()
						+ matrix[Customer.getNodeId()][Route.get(index + 1).getNodeId()]) < Route.get(index + 1)
								.getCloseTime();
			}
		}
	}
	
	
	public void updatePassedTime(long[][] matrix) {
		passedTime=calculPassedTime(matrix);
	}
	public long calculPassedTime(long[][] matrix) {
		long t = 0;
		for (int i = 0; i < Route.size()-1; i++) {
			if (t + matrix[Route.get(i).getNodeId()][Route.get(i + 1).getNodeId()] > Route.get(i + 1).getReadyTime()) {
				t += matrix[Route.get(i).getNodeId()][Route.get(i + 1).getNodeId()];
			} else {
				t = Route.get(i + 1).getReadyTime();
			}
		}
		return t;
	}

}
