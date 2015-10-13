package AST;

import java.util.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.io.File;
import java.util.Set;
import java.util.ArrayList;
import beaver.*;
import org.jastadd.util.*;
import java.util.zip.*;
import java.io.*;
import java.io.FileNotFoundException;
/**
 * @ast class
 * @aspect BranchTarget
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:27
 */
 class LazyFinallyIterator extends java.lang.Object implements Iterator<FinallyHost> {

    private final Stmt branch;


    private FinallyHost enclosing;



    LazyFinallyIterator(Stmt root) {
      branch = root;
      enclosing = branch.enclosingFinally(branch);
    }



    @Override
    public boolean hasNext() {
      return enclosing != null;
    }



    @Override
    public FinallyHost next() {
      FinallyHost b = enclosing;
      enclosing = enclosing.enclosingFinally(branch);
      return b;
    }



    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }


}
