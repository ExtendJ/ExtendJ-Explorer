package jastaddad;
import edu.uci.ics.jung.graph.util.Graphs;

import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

public class JastAddAd{

    public JastAddAd(Object root){
        Node tree = new Node(root, false); //Unknown if root is a list, TODO check this shit out
        Graphs g = new Graphs();
    }
}
