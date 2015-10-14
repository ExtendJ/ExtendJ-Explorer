package abstractpackage;

public class ApClass2 extends AbstractClass{
	public ApClass2(){
		super("ApClass2");
	}

	public void exe(){
		System.out.print("My name is: ");
		System.out.println(getName());
	}
}
