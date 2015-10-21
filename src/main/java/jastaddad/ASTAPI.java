package jastaddad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gda10jth on 10/16/15.
 */
public class ASTAPI {
    private Node tree;
    private FilteredTreeNode filteredTree;
    private Config cfgTypeList;
    private HashMap<String, Integer> typeHash;
    private HashMap<String, List<FilteredTreeNode>> typeNodeHash;

    public ASTAPI(Node tree){
        this.tree = tree;
        this.filteredTree = null;
        cfgTypeList = new Config("jastaddadui-typelist.cfg");
        typeHash = new HashMap<>();
        typeNodeHash = new HashMap<>();

        //System.out.println("configCount: " + cfgTypeList.configCount());
        traversTree(this.tree, null, null);
        /*
            Iterator it = typeHash.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                //System.out.println(pair.getKey() + "");
            }
        */

        //if(cfgTypeList.configCount() == 0){
            cfgTypeList.writeConfigFile("jastaddadui-typelist.cfg",
                    typeHash.entrySet().iterator(),
                    "# This file will be read by the Interactive user interface JastAddAdUi for JastAddAd");
        //}
    }

    public void newTypeFiltered(String type){
        System.out.println("Type: " + type);
    }

    private void traversTree(Node node, FilteredTreeNode parent, FilteredTreeNode cluster){
        if(node == null)
            return;

        FilteredTreeNode addToParent = null;
        FilteredTreeNode fNode = new FilteredTreeNode(node, cfgTypeList);
        FilteredTreeNode tmpCluster = cluster;

        if(!typeNodeHash.containsKey(fNode.node.className))
            typeNodeHash.put(fNode.node.className, new ArrayList<>());
        typeNodeHash.get(fNode.node.className).add(fNode);
        if(fNode.node.name != "") {
            if(!typeNodeHash.containsKey(fNode.node.fullName))
                typeNodeHash.put(fNode.node.fullName, new ArrayList<>());
            typeNodeHash.get(fNode.node.fullName).add(fNode);
        }

        // Add the node to the config hash
        if(fNode.node.name != "")
            typeHash.put(fNode.node.fullName, fNode.isEnabled() ? 1:0);
        typeHash.put(fNode.node.className, fNode.isEnabled() ? 1:0);

            //System.out.println("class: " + fNode.node.className + " name: " + fNode.node.name);
        // if the class is not filtered
        if(fNode.isEnabled()){
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

        // put child clusters together in a parent cluster if they have no children in the filtered tree
        if(fNode.isEnabled()) {
            FilteredTreeNode newCluster = new FilteredTreeNode();
            // get all children cluster children that have no children
            for (FilteredTreeNode fChild : fNode.getChildren()) {
                if (fChild.isCluster() && fChild.getChildren().size() == 0) {
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
}
