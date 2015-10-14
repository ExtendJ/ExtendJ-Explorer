import ownpackage.OwnClass;

public class NpClass1{
	NpClass2 class2;
	OwnClass own;

	public NpClass1(){
		class2 = new NpClass2();
		own = new OwnClass();
	}

	public void exe(){
		class2.exe();
		own.doStuff();
	}
}
