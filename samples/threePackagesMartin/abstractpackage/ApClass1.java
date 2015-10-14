package abstractpackage;

public class ApClass1 extends AbstractClass{
	public ApClass1(){
		super("ApClass1");
	}

	@Override
	public void exe(){	
		System.out.println("func1: " + Func1());
	}

	private String Func1(){
		return	func2();
	}

	private String func2(){
		return "func2";
	}
}
