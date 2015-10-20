package jastaddad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gda10jth on 10/16/15.
 */
public class ASTAPI {
    private Node tree;
    private FilteredTreeNode filteredTree;
    private Config cfgTypeList;
    private HashMap<String, Integer> typeHash;

    public ASTAPI(Node tree){
        this.tree = tree;
        this.filteredTree = null;
        cfgTypeList = new Config("jastaddadui-typelist.cfg");
        typeHash = new HashMap<>();
        //System.out.println("configCount: " + cfgTypeList.configCount());
        traversTree(this.tree, null, null);
        Iterator it = typeHash.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + "");
        }

        if(cfgTypeList.configCount() == 0){
            cfgTypeList.writeConfigFile("jastaddadui-typelist.cfg",
                    typeHash.entrySet().iterator(),
                    "# This file will be read by the Interactive user interface JastAddAdUi for JastAddAd");
        }
    }

    private void traversTree(Node node, FilteredTreeNode parent, FilteredTreeNode cluster){
        if(node == null)
            return;

        FilteredTreeNode addToParent = null;

        FilteredTreeNode fNode = new FilteredTreeNode(node);
        FilteredTreeNode tmpCluster = cluster;

        typeHash.put(fNode.node.className, 1);
        typeHash.put(fNode.node.name, 1);
        System.out.println("class: " + fNode.node.className + " name: " + fNode.node.name);
        boolean normalFuckingNode = cfgTypeList.configCount() == 0 || (cfgTypeList.isEnabled(node.name) && cfgTypeList.isEnabled(node.className));
        // if the class is not filtered
        if(normalFuckingNode){
            // is this the root?
            if(parent == null){
                filteredTree = fNode;
            }else{
                // if the above node a cluster?
                if(tmpCluster == null){
                    addToParent = fNode;
                }else{
                    tmpCluster.addChild(fNode);
                }
            }
            tmpCluster = null;
        }else{
            // first node in this cluster?
            if(tmpCluster == null){
                tmpCluster = new FilteredTreeNode(fNode);
                // is this cluster the root of the tree?
                if(parent != null)
                    addToParent = tmpCluster;
            }else{
                // add node to cluster but not to the filtered tree
                addToParent = fNode;
            }
            if(parent == null) // first node in tree
                filteredTree = tmpCluster;
        }

        // travers down the tree
        for(Node child : node.children){
            traversTree(child, fNode, tmpCluster);
        }

        if(normalFuckingNode) {
            FilteredTreeNode newCluster = new FilteredTreeNode();
            for (FilteredTreeNode fChild : fNode.getChildren()) {
                if (fChild.isCluster && fChild.getChildren().size() == 0) {
                    newCluster.addCluster(fChild);
                }
            }
            //System.out.println("BU:" + newCluster.getClusterContainer().size() + " " + newCluster.isClusterParent);
            if(newCluster.getClusterContainer().size() > 1) {
                for(FilteredTreeNode cChild : newCluster.getClusterContainer()){
                    fNode.getChildren().remove(cChild);
                }

                fNode.addChild(newCluster);
            }
        }

        if(addToParent != null)
            parent.addChild(addToParent);
    }

    public HashMap<String, Integer> getTypeHash(){ return typeHash; }

    public FilteredTreeNode getFilteredTree(){
        return filteredTree;
    }

    private void addToFilteredTree(FilteredTreeNode parent, Node node) {
        FilteredTreeNode childNode = new FilteredTreeNode(node);
        parent.addChild(childNode);
    }

    /*
    private void addToFilteredTree(FilteredTreeNode parent) {
        for (Node child : parent.node.children) {
            FilteredTreeNode childNode = new FilteredTreeNode(child);
            if(!child.name.equals("Stmt")) {
                parent.addChild(childNode);
            }else{
                System.out.print("Skip");
            }
            addToFilteredTree(childNode);
        }
    }
    */
}
