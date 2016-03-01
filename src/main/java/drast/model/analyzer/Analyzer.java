package drast.model.analyzer;

/**
 *   The DrAST model has support for user developed Analysers. Analysers are plugins that will
 *   perform different tasks on the reflected tree or the filtered tree while the model are
 *   creating these.
 *
 *   An Analyser can be executed at three situations.
 *          - When the Reflected tree is being build. The Analyser will be called once per Node node in the tree.
 *          - After the Reflected tree is build. The Analyser will be called once, with the root Node node.
 *          - After the Filtered tree is build. The Analyser will be called once, with the root GenericTreeNode node.
 *
 * Created by gda10jli on 2/17/16.
 */
public interface Analyzer {
    //Ello meee freeends
}
