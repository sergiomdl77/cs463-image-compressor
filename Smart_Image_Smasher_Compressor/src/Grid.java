
import java.util.ArrayList;

// Class that defines the structure, operations and data of
// a grid, whose data is stored as an array or arrays. This class
// utilizes the type ArrayList as the type of the array. By making
// every element of the array be an ArrayList itself it gets to 
// represent a two dimensional array.
public class Grid<T>
{
	public ArrayList<ArrayList<T>> values;
	
	// Constructor that takes the number of rows (r) and columns (c)
	// and initializes a grid of those dimensions.
	public Grid(int r, int c)
	{
		values = new ArrayList<ArrayList<T>>();
		
		for (int i=0; i<r; i++)
		{
			ArrayList<T> row = new ArrayList<T>();
			for (int j=0; j<c; j++)
			{
				row.add(null);
			}
			values.add(row);
		}
	}
	
	// Constructor that takes a two dimensional array as an 
	// argument and creates a rectangular grid with the exact
	// same dimensions, and elements of the type T
	public Grid(T[][] rectangle)
	{
		int rectRows = rectangle.length;
		int rectCols = rectangle[0].length;
		
		values = new ArrayList<ArrayList<T>>();
		
		for (int i=0; i<rectRows; i++)
		{
			ArrayList<T> gridRow = new ArrayList<T>();

			for (int j=0; j<rectCols; j++)
			{
				gridRow.add(rectangle[i][j]);
			}
			values.add(gridRow);
		}
	}
	
	
	// Returns the height (number or columns) of the grid
	public int height()
	{
		return values.size();
	}
	
	// Returns the width (number of rows) of a grid
	public int width()
	{
		return values.get(0).size();
	}
	
	// Method that return the value (of type T) of one element in a grid that has
	// previously been created with certain dimensions (therefore i and j will 
	// always be within boundaries for this project's implementation).
	public T get(int i, int j)
	{
		return values.get(i).get(j);
	}
	
	// Method that updates the value of one element in a grid that has
	// previously been created with certain dimensions (therefore i
	// and j will always be within boundaries for this project's implementation).
	public void set(int i, int j, T value)
	{
		values.get(i).set(j, value);
	}
	
	// Method that removes one element from the grid given the position
	// in terms of (row=i and column=j)
	public T remove(int i, int j)
	{
		T element = values.get(i).get(j);
		
		if (values.get(i).size() == 1)
			values.remove(i);
		else
			values.get(i).remove(j);

		return element;
	}
	
	// Method that gets the transposes the grid (useful to do horizontal
	// operations on the grid while using the same logic as with the 
	// vertical operations)
	public Grid<T> transpose()
	{
		int rows = height();
		int cols = width();
		
		Grid<T> transGrid = new Grid<T>(cols,rows);
		
		for (int i = 0; i<rows; i++)
		{
			for (int j = 0; j<cols; j++)
			{
				T elem = this.get(i, j);
				transGrid.set(j, i, elem);
			}
		}
		return transGrid;
	}
	
	// overriding the equal method to define what makes two objects of this class equal
	public boolean equals(Object other)
	{	
		// Checking for type
		if (!(other instanceof Grid))
			return false;
		
		@SuppressWarnings("unchecked")
		Grid<T> theOther = (Grid<T>)other;
		
		// Checking for dimensions
		if ( (theOther.height() != this.height() )	// checking for same dimensions
				|| (theOther.width() != this.width() ) )
			return false;
		
		// Checking for equal elements
		for (int i=0; i<this.height(); i++)
		{
			for (int j=0; j<this.width(); j++)
			{
				// if the first element (position 0,0) is null in both grids
				// this method will assume that both grids full of null elements,
				// and therefore, that they are equal.
				if ((theOther.get(i, j) == null) && (this.get(i, i) == null))
					return true;
				
				// if one element of the two grids at (i,j) is not equal, the method
				// returns false immediately
				if ( !(theOther.get(i, j).equals(this.get(i, j))) )
					return false;
			}
		}
		
		return true;
	}

	
	// overriding the string method to customize it to a format for display
	public String toString()
	{
		return values.toString();
	}
	
	
	public static void main(String[] args )
	{
		Grid<RGB> g1 = new Grid<RGB>(new RGB[][]{
			     {new RGB(100, 75,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(200,125,200)},
			     {new RGB(150, 30,180),new RGB(150, 50,180),new RGB(100,120,180),new RGB(100,120,180),new RGB(100,120,180)},
			     {new RGB(100, 75,100),new RGB(100, 80,100),new RGB(100, 85,100),new RGB(100, 95,100),new RGB(100,110,100)},
			     {new RGB(200,100, 10),new RGB(200,100, 10),new RGB(200,100, 10),new RGB(210,200, 10),new RGB(255,  0, 10)}
			   });
		
		Grid<RGB> g2 = new Grid<RGB>(new RGB[][]{
		     {new RGB(100, 75,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(200,125,200)},
		     {new RGB(150, 30,180),new RGB(150, 50,180),new RGB(100,120,180),new RGB(100,120,180),new RGB(100,120,180)},
		     {new RGB(100, 75,100),new RGB(100, 80,100),new RGB(100, 85,100),new RGB(100, 95,100),new RGB(100,110,100)},
		     {new RGB(200,100, 10),new RGB(200,100, 10),new RGB(200,100, 10),new RGB(210,200, 10),new RGB(255,  0, 10)}
		   });
		
		Grid<RGB> g3 = new Grid<RGB>(new RGB[][]{
		      {new RGB(100, 75,200),new RGB(100,100,200), new RGB(0,0,0)}, 
		      {new RGB(100,100,200),new RGB(100,100,200), new RGB(1,1,1)} } );
		
		Grid<RGB> g4 = new Grid<RGB>(new RGB[][]{
		      {new RGB(100, 75,200),new RGB(100,100,200), new RGB(0,0,0)}, 
		      {new RGB(100,100,200),new RGB(100,100,200), new RGB(1,1,1)} } );

		Grid<Integer> g5 = new Grid<Integer>(new Integer[][]{  { 1, 2, 3 }, { 4, 5, 6 } } );
		
		Grid<Integer> g6 = new Grid<Integer>(new Integer[][]{  { 1, 2, 3 }, { 4, 5, 6 } } );
		
		if (g3.equals(g4))
			System.out.print("yes");
		else
			System.out.print("no");
		
		
		
		
//		Grid<Integer> myGrid = new Grid<Integer>(5,6);

//		System.out.print(myGrid);
//		System.out.print(myGrid.transpose());
	}
}





