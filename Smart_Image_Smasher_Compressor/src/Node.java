

// Class defines the structure of one pixel in the grid (image map)
// where each element contains information about its location in 
// the image's map, the node for the best option of low energy path
// and the energy cost of the path up to itself.
public class Node
{
	public Point p;
	public Node bestHop;
	public int cost;
	
	// Constructor with parameters
	public Node(int r, int c, Node bestHop, int cost)
	{
		p = new Point(r,c);
		this.bestHop = bestHop;
		this.cost = cost;
	}
	
	// overriding the string method to customize it to a format for display
	public String toString()
	{
		String result = "";
		result +=  p.toString() + " "; 
		if (bestHop == null)
			result += "null";
		else
			result += bestHop.p.toString();
		
		result += " " + cost;
		
		return result;
	}
}
