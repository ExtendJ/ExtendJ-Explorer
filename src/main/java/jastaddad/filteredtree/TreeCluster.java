package jastaddad.filteredtree;

import configAST.Color;
import configAST.Str;
import jastaddad.Config;

import java.util.ArrayList;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeCluster extends GenericTreeCluster {

    private GenericTreeNode node;
    public TreeCluster(GenericTreeNode node, GenericTreeNode parent){
        super(parent);
        this.node = node;
    }

    public GenericTreeNode getClusterRoot(){
        return node;
    }

    @Override
    public boolean isCluster() {
        return true;
    }

    @Override
    public boolean isClusterParent() {
        return false;
    }

}
