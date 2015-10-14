package abstractpackage;

public abstract class AbstractClass{
	public abstract void exe();
	String name;
	
	AbstractClass(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}	
}
