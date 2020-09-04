

// class that defines the structure of objects of the pixel's color
public class RGB
{
	public int r;
	public int g;
	public int b;
	
	// constructor with no parameters
	public RGB()
	{
		r = g = b = 0;
	}
	
	// constructor with parameters
	public RGB(int r, int g, int b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	// overriding the equal method to define what makes two objects of this class equal
	public boolean equals(Object other)
	{
		if ( !(other instanceof RGB))
			return false;
		
		RGB theOther = (RGB)other;
		
		if (theOther.r == this.r && theOther.g == this.g && theOther.b == this.b)
			return true;
		else
			return false;
	}
	
	public String toString()
	{
		return "(" + this.r + ","+ this.g + "," + this.b + ")"; 
	}
	
	// overriding the string method to customize it to a format for display
	public static void main(String args[])
	{
		RGB new1 = new RGB(1,2,3);
		RGB new2 = new RGB(1,2,3);
		
		if (new1.equals(new2))
			System.out.print("yes");
		else
			System.out.print("no");
	}
	
	
	
	
	
}
