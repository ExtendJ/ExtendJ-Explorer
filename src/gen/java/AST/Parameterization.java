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
 * @ast interface
 * @aspect LookupParTypeDecl
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:687
 */
 interface Parameterization {

     
    boolean isRawType();

     
    TypeDecl substitute(TypeVariable typeVariable);
}
