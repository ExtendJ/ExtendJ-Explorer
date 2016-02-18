package drast.model.analyzer;

import drast.model.Node;

/**Reflected tree analyzer (RTAnalyzer) interface, purpose to check the some structure of the reflected tree if necessary
 * Add implements this and add your class to correct the list in the ASTBrain
 * Created by gda10jli on 2/17/16.
 */
public interface RTAnalyzer extends Analyzer{
    boolean check(Node node);
}
