package model;

public class Node
{
    private int NodeId;
    private int Node_X ,Node_Y; //Node Coordinates
    private boolean routed;
    private int readyTime;
    private int closeTime;
    private int serviceTime;

    public Node(int nodeId, int node_X, int node_Y, int readyTime, int closeTime, int serviceTime) {
		super();
		this.NodeId = nodeId;
		this.Node_X = node_X;
		this.Node_Y = node_Y;
		this.readyTime = readyTime;
		this.closeTime = closeTime;
		this.serviceTime = serviceTime;
		this.routed=false;
	}

	public int getNodeId() {
		return NodeId;
	}

	public void setNodeId(int nodeId) {
		NodeId = nodeId;
	}

	public int getNode_X() {
		return Node_X;
	}

	public void setNode_X(int node_X) {
		Node_X = node_X;
	}

	public int getNode_Y() {
		return Node_Y;
	}

	public void setNode_Y(int node_Y) {
		Node_Y = node_Y;
	}

	public boolean isRouted() {
		return routed;
	}

	public void setRouted(boolean routed) {
		this.routed = routed;
	}

	public int getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(int readyTime) {
		this.readyTime = readyTime;
	}

	public int getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(int closeTime) {
		this.closeTime = closeTime;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
	}

	public Node() {
		super();
		// TODO Auto-generated constructor stub
		this.routed=false;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
       
}