package CalcASM.src.java.gen;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeSet;
/**
 * @ast class
 * @aspect UserTest
 * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UserTest.jrag:11
 */
 class Counter extends Object {
  
		private int value;

  
		public Counter() { value = 0; }

  
		public void add(int value) { this.value += value; }

  
		public int value() { return value; }


}
