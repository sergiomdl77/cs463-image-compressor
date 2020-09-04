

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Class that defines the operations of the software that is able
// to manipulate image files in order to reduce its dimensions
// in an efficient way that avoids the loss of important features.
public class P1
{

	// Method that looks at the grid, and at row r and column c, and figure out its energy.
	// energy = h_energy + v_energy
	// h_energy = (R_left-R_right)^2 + (G_left-G_right)^2 + (B_left-B_right)^2
	// v_energy = (R_above-R_below)^2 + (G_above-G_below)^2 + (B_above-B_below)^2	
	// Border pixels wrap around to the far side so that we always have four pixels in our calculations.	
	public static int energyAt(Grid<RGB> grid, int r, int c)
	{
		int row = r, col = c;
		
		RGB right = grid.get(r, ((c + 1) % grid.width()));
		RGB lower = grid.get(((r + 1) % grid.height()), c);
		
		if (c == 0)
			col = grid.width()-1;
		else
			col = c - 1;
		RGB left = grid.get(r, col);
		
		if (r == 0)
			row = grid.height()-1;
		else
			row = r - 1;
		RGB upper = grid.get(row, c);
	
		double h_energy = Math.pow((left.r - right.r), 2) + Math.pow((left.g - right.g), 2) + Math.pow((left.b - right.b), 2);
		
		double v_energy = Math.pow((upper.r - lower.r),2) + Math.pow((upper.g - lower.g), 2) + Math.pow((upper.b - lower.b), 2);

		return (int)(v_energy + h_energy);
	}
	
	// Method that given a grid, calculate the energy at each location and return a 
	// grid of these values at the same locations	
	public static Grid<Integer> energy(Grid<RGB> grid)
	{
		int h = grid.height();
		int w = grid.width();

		Grid<Integer> energygrid = new Grid<Integer>(h, w);
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				energygrid.set(i, j, energyAt(grid, i, j) );
				
		return energygrid;
	}
	

	// Helper method that takes a grid of RGB elements and converts it into a
	// grid of nodes, which will be able to stores the calculations of the 
	// energies of the pixel that each RGB element belongs to in the grid. Each
	// one of these node elements will also be able to be updated with the best
	// node below it in the path across the image (the past with the lowest 
	// energy cost.
	private static Grid<Node> turnIntoNodes(Grid<RGB> grid)
	{
		Grid<Node> newGrid = new Grid<Node>(grid.height(), grid.width() );
		
		Grid<Integer> energyGrid = energy(grid);
		
		for (int i=0; i<grid.height(); i++)
		{
			for (int j=0; j<grid.width(); j++)
			{
				Node newNode = new Node(i, j, null, energyGrid.get(i,j));
				newGrid.set(i,j,newNode);
			}
		}
		
		return newGrid;
	}
	
	
	// Helper method that determines which is the best possible node below it
	// to be part of the path across the picture to the lowest cost path.
	// This method returns a pointer to that node.
	public static Node getbestHop(int i, int j, Grid<Node> grid)
	{
		// It starts by assuming the node just below has the lowest cost
		Node lowestCostNode, below, left, right;
		below = grid.get(i+1, j);

		// if the current node represents a pixel in the left edge of the 
		// image map (in column=0)
		if (j == 0)
		{
			right = grid.get(i+1, j+1);
			lowestCostNode = below;
			
			if (right.cost < below.cost)
			{
				lowestCostNode = right;
			}
		}
		// if the current node represents a pixel in the right edge of the 
		// image map (in column = width - 1)
		else if (j == grid.width()-1)
		{
			left = grid.get(i+1, j-1);
			lowestCostNode = left;
			
			if (below.cost < left.cost)
			{
				lowestCostNode = below;
			}
		}
		// if the current node represents a node that is not on either the
		// right or left edge
		else
		{
			left = grid.get(i+1, j-1);
			right = grid.get(i+1, j+1);
			lowestCostNode = left;
			if (below.cost < left.cost && below.cost < right.cost)
				lowestCostNode = below;
			if (right.cost < below.cost && right.cost < left.cost)
				lowestCostNode = right;
		}

		return lowestCostNode;
	}
	
	// Helper Method that, given a grid, find and return a list of points that 
	// constitute the cheapest path from top to bottom.
	// Restriction: there must be exactly one pixel from each row, and the pixel
	// in the row below it along the returned path must be either directly below 
	// it diagonally down to either side: this means there are only three (adjacent) options. 
	public static List<Point> findPath(Grid<Node> nodeGrid)
	{
		List<Point> path = new ArrayList<Point>();

		// traverses the grid row by row (from the second lowest row up)
		for (int i=nodeGrid.height()-2; i>=0; i--)
		{
			// traverses a row, node by node, from left to right 
			for (int j=0; j<nodeGrid.width(); j++)
			{
				// creates a new node with the current the RGB and coordinates
				// of the current node, but it also adds the cost and the best
				// path up to the current node.
				Node originalNode = nodeGrid.get(i, j);
				Node bestHop = getbestHop(i, j, nodeGrid);
				
				int cost = originalNode.cost + bestHop.cost;
				int row = originalNode.p.x;
				int col = originalNode.p.y;
				
				// replaces the node in that position of the grid with the new
				// node with the added characteristics.
				Node updatedNode = new Node(row, col, bestHop, cost);
				nodeGrid.set(i, j, updatedNode);
			}
		}
		
		// getting the node with lowest cost at top (which is the head of the path with the cheapest cost)
		Node cheapestNode = nodeGrid.get(0,0);		// initializing the cheapest node (as the one to left most)
		for (int j=1; j<nodeGrid.width(); j++)
		{
			if (nodeGrid.get(0, j).cost < cheapestNode.cost)
				cheapestNode = nodeGrid.get(0, j);
		}
		
		// creates the path by following the trail of best hops, from node to
		// node in the grid.
		Node next = cheapestNode;
		path.add(next.p);
		for (int i=0; i<nodeGrid.height() - 1; i++)
		{
			next = next.bestHop;
			path.add(next.p);
		}
		
		return path;
	}
	
	// Method that finds (and returns) the vertical path on the grid with the lowest energy
	// cost (which can be removed without affecting the main features of the image. To 
	// accomplish this, the grid of RGB elements will be first turned in to a grid of nodes.
	// and then send to the findPath method.
	public static List<Point> findVerticalPath(Grid<RGB> grid)
	{
		Grid<Node> nodeGrid = turnIntoNodes(grid);
		
		return findPath(nodeGrid);
	}
	
	// Method that finds (and returns) the horizontal path on the grid with the lowest energy
	// cost (which can be removed without affecting the main features of the image). To simplify
	// the logic of such operation the grid is turned into a grid of nodes and then transposed 
	// so that the path can be found by calling the helper method findPath and be operated on 
	// as if it was finding a vertical path.
	public static List<Point> findHorizontalPath(Grid<RGB> grid)
	{
		Grid<Node> nodeGrid = turnIntoNodes(grid);

		nodeGrid = nodeGrid.transpose();
		
		return findPath(nodeGrid);
	}
	
	// Method that removes the horizontal path with the lowest energy cost of the image.
	// This is accomplished by transposing the grid and then operating on it as if it
	// was removing a vertical path.  The grid must be transposed again to obtain the
	// original position of the points before returning the image with the removed path.
	public static Grid<RGB> removeHorizontalPath(Grid<RGB> grid, List<Point> path)
	{
		grid = grid.transpose();
		
		int row, col;
		for (int i=0; i<path.size(); i++)
		{
			row = path.get(i).y;
			col = path.get(i).x;
			grid.remove(row,col);
		}
		
		
		return grid.transpose();
	}
	
	
	// Method that removes the vertical path with the lowest energy cost of the image.
	public static Grid<RGB> removeVerticalPath(Grid<RGB> grid, List<Point> path)
	{
		int row, col;
		for (int i=0; i<path.size(); i++)
		{
			row = path.get(i).x;
			col = path.get(i).y;
			grid.remove(row,col);
		}
		
		return grid;
	}

	
	// Method that reads a ppm file and turns into a grid of RGB elements which 
	// is returned
	public static Grid<RGB> ppm2grid(String filename) throws FileNotFoundException
	{
		try
		{
			int rows=0, cols=0, range=0;
			int R, G, B;
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
	
			String title = reader.readLine();
			cols = Integer.parseInt(reader.readLine());
			rows = Integer.parseInt(reader.readLine());
			range = Integer.parseInt(reader.readLine());
			
			Grid<RGB> newGrid = new Grid<RGB>(rows,cols);
			
			for (int i=0; i<rows; i++)
			{
				for (int j=0; j<cols; j++)
				{
					R = Integer.parseInt(reader.readLine());
					G = Integer.parseInt(reader.readLine());
					B = Integer.parseInt(reader.readLine());
					
					newGrid.set(i, j, new RGB(R,G,B));
				}
			}
			
			reader.close();
			
			return newGrid;
		}
		
		catch (IOException e)
		{
	        System.out.println("Could not find file " + filename);
	        return null;
	    }
	}
	
	// Method that receives a grid of RGB elements and writes in to a ppm file in
	// the ppm format.
	public static void grid2ppm(Grid<RGB> grid, String filename) throws FileNotFoundException
	{
		try 
		{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename)));
			
			writer.write("P3");
			writer.newLine();
	
			String cols = Integer.toString(grid.width());
			writer.write(cols);
			writer.newLine();
			String rows = Integer.toString(grid.height());
			writer.write(rows);
			writer.newLine();
			String range = "255";
			writer.write(range);
			writer.newLine();
			
			
			for (int i=0; i<grid.height(); i++)
			{
				for( int j=0; j<grid.width(); j++)
				{
					int r = grid.get(i, j).r;
					String R = Integer.toString(r);
					writer.write(R);
					writer.newLine();
					int g = grid.get(i, j).g;
					String G = Integer.toString(g);
					writer.write(G);
					writer.newLine();
					int b = grid.get(i, j).b;
					String B = Integer.toString(b);
					writer.write(B);
					writer.newLine();
				}
			}
			
			writer.close();
		}
		
		catch (IOException e)
		{
	        System.out.println("Could not find file " + filename);
	    }
	}
	
/*	
  public static Grid<RGB> g1(){
	    return new Grid<RGB>(new RGB[][]{
	      {new RGB(100, 75,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(200,125,200)},
	      {new RGB(150, 30,180),new RGB(150, 50,180),new RGB(100,120,180),new RGB(100,120,180),new RGB(100,120,180)},
	      {new RGB(100, 75,100),new RGB(100, 80,100),new RGB(100, 85,100),new RGB(100, 95,100),new RGB(100,110,100)},
	      {new RGB(200,100, 10),new RGB(200,100, 10),new RGB(200,100, 10),new RGB(210,200, 10),new RGB(255,  0, 10)}
	    });
  }	
    
	// This method simply creates a ppm file.
	public static void writeFile(String content, String filename){
	    try {
	      PrintWriter pw = new PrintWriter(filename);
	      pw.write(content);
	      pw.close();
	    }
	    catch (FileNotFoundException e){
	      throw new RuntimeException(String.format("couldn't write that string to that file(\"%s\").",filename));
	    }
	  }	
*/	
	
	// This main method was used for small testing purposes only.
	public static void main(String args[]) throws IOException
	{

		Grid<RGB> p1 = new Grid<RGB>( new RGB[][]{
		      {new RGB(100, 75,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(200,125,200)},
		      {new RGB(150, 30,180),new RGB(150, 50,180),new RGB(100,120,180),new RGB(100,120,180),new RGB(100,120,180)},
		      {new RGB(100, 75,100),new RGB(100, 80,100),new RGB(100, 85,100),new RGB(100, 95,100),new RGB(100,110,100)},
		      {new RGB(200,100, 10),new RGB(200,100, 10),new RGB(200,100, 10),new RGB(210,200, 10),new RGB(255,  0, 10)}});
	
		
//		System.out.print(p1.toString());
//		List<Point> path = findHorizontalPath(p1);

		
//		List<Point> v_path = findVerticalPath(p1);
//		Grid<RGB> modified = removeVerticalPath(p1,  v_path);
		
//		System.out.print(v_path + "\n");
//		System.out.print(modified);

//		List<Point> h_path = findHorizontalPath(p1);
//		modified = removeHorizontalPath(p1,  h_path);
		
//		System.out.print(h_path.toString() + "\n");
//		System.out.print(h_path + "\n");
//		System.out.print(modified);
		
//		String s = "P3\n3\n5\n255\n0\n100\n200\n0\n80\n200\n0\n100\n200\n100\n25\n200\n100\n15\n200\n100\n25\n200\n200\n95\n255\n200\n110\n255\n200\n100\n255\n200\n100\n255\n200\n95\n255\n200\n100\n255\n255\n70\n200\n255\n100\n200\n255\n100\n200\n";
//	    writeFile(s,".temp_file.ppm");
		
//		Grid<RGB> grid = ppm2grid( ".temp_file.ppm");
		
//		System.out.print(grid);
		
//		grid2ppm(grid,"temp_file.ppm");
		
	}
	
		
}
