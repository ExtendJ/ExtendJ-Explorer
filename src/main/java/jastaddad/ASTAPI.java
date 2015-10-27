package jastaddad;

import jastaddad.filteredtree.FilteredTreeCluster;
import jastaddad.filteredtree.FilteredTreeClusterParent;
import jastaddad.filteredtree.FilteredTreeItem;
import jastaddad.filteredtree.FilteredTreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gda10jth on 10/16/15.
 */
public class ASTAPI {
    private Node tree;
    private FilteredTreeItem filteredTree;
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
        traversTree(this.tree, null, null, true);
        cfgTypeList.writeConfigFile("jastaddadui-typelist.cfg",
                typeHash.entrySet().iterator(),
                "# This file will be read by the Interactive user interface JastAddAdUi for JastAddAd");
    }

    public boolean newTypeFiltered(String type, boolean enabled){
        for(FilteredTreeNode fNode : typeNodeHash.get(type) ){
            fNode.setEnabled(enabled);
            addToConfigs(fNode);
            cfgTypeList.writeConfigFile("jastaddadui-typelist.cfg",
                    typeHash.entrySet().iterator(),
                    "# This file will be read by the Interactive user interface JastAddAdUi for JastAddAd");
            cfgTypeList = new Config("jastaddadui-typelist.cfg");
        }
        traversTree(this.tree, null, null, true);
        return true;
    }

    private void addToTypes(FilteredTreeNode fNode){
        // add the node to the hashmap of types
        if(!typeNodeHash.containsKey(fNode.node.className))
            typeNodeHash.put(fNode.node.className, new ArrayList<>());
        typeNodeHash.get(fNode.node.className).add(fNode);
        if(fNode.node.name != "") {
            if(!typeNodeHash.containsKey(fNode.node.fullName))
                typeNodeHash.put(fNode.node.fullName, new ArrayList<>());
            typeNodeHash.get(fNode.node.fullName).add(fNode);
        }
    }

    private void addToConfigs(FilteredTreeNode fNode){
        // Add the node to the config hash
        if(fNode.node.name != "")
            typeHash.put(fNode.node.fullName, fNode.isEnabled() ? 1:0);
        typeHash.put(fNode.node.className, fNode.isEnabled() ? 1:0);
    }

    private void clusterClusters(FilteredTreeNode fNode){
        // put child clusters together in a parent cluster if they have no children in the filtered tree
        if(fNode.isNode()) {
            //FilteredTreeNode n = (FilteredTreeNode) fNode;
            FilteredTreeClusterParent clusterParent = new FilteredTreeClusterParent();
            // get all children cluster children that have no children
            for (FilteredTreeItem fChild : fNode.getChildren()) {
                if (fChild.isCluster() && fChild.getChildren().size() == 0) {
                    clusterParent.addCluster((FilteredTreeCluster)fChild);
                }
            }

            //System.out.println("BU:" + newCluster.getClusterContainer().size() + " " + newCluster.isClusterParent);
            if(clusterParent.getClusters().size() > 1) {
                for(FilteredTreeItem cChild : clusterParent.getClusters()) {
                    fNode.getChildren().remove(cChild);
                }
                fNode.addChild(clusterParent);
            }
        }
    }
    private void traversTree(Node node, FilteredTreeItem parent, FilteredTreeCluster cluster, boolean firstTime){
        if(node == null)
            return;

        FilteredTreeItem addToParent = null;
        FilteredTreeNode fNode = new FilteredTreeNode(node, cfgTypeList);
        FilteredTreeCluster tmpCluster = cluster;

        if(firstTime) {
            addToTypes(fNode);
            addToConfigs(fNode);
        }

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
                tmpCluster = new FilteredTreeCluster(fNode);
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
            traversTree(child, fNode, tmpCluster, firstTime);
        }

        clusterClusters(fNode);

        if(addToParent != null)
            parent.addChild(addToParent);
    }

    public HashMap<String, Integer> getTypeHash(){ return typeHash; }

    public FilteredTreeItem getFilteredTree(){
        return filteredTree;
    }
}
