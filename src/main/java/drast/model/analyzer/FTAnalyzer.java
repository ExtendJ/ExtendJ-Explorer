package drast.model.analyzer;

import drast.model.filteredtree.GenericTreeNode;

/**
 * Filtered tree analyzer (FTAnalyzer) interface, purpose to check the some structure of the filtered tree if necessary
 * Add implements this and add your class to correct the list in the ASTBrain
 * Created by gda10jli on 2/17/16.
 */

public interface FTAnalyzer extends Analyzer {
    boolean check(GenericTreeNode node);
}
