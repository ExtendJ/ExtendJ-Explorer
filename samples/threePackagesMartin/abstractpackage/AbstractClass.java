package abstractpackage;

public class AbstractClass{
	public AbstractClass(){}
	
	private int a = 1337;
	
	public static int getValue(){ return 1337 + 1; }
	
	public static void main(String[] args) { 
	  System.out.print(AbstractClass.getValue()); 
	}
	
}
