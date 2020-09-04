
// Class that defines the position of a pixel in the grid (image map)
public class Point 
{
	public int x;
	public int y;
	
	// constructor with no parameters
	public Point()
	{
		x = y = 0;
	}
	
	// constructor with parameters
	public Point(int row, int col)
	{
		x = row;
		y = col;
	}
	
	// method to swap the values of x and y
	public Point invert()
	{
		return new Point(y,x);  // It is possible that we need to change 
								// the original values of the object too.
	}	
	
	// overriding the equal method to define what makes two objects of this class equal
	public boolean equals(Object other)
	{
		if (other instanceof Point)
		{
			Point theOther = (Point)other;
			
			if ( (theOther.x != this.x) || (theOther.y != this.y) )
				return false;
			
			return true;
		}
		
		else
			return false;
	}
	
	// overriding the string method to customize it to a format for display
	public String toString()
	{
		return ("("+ x + "," + y + ")");
	}
	
	
}
