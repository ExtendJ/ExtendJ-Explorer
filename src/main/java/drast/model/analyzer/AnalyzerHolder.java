package drast.model.analyzer;

import drast.model.Node;
import drast.model.filteredtree.GenericTreeNode;

import java.util.ArrayList;

/**
 * Created by gda10jli on 2/17/16.
 *
 * This class contain and runs all Analysers in the model. Analysers can be added.
 *
 * Copy from drast.model.analyzer.Analyzer:
 *   The DrAST model has support for user developed Analysers. Analysers are plugins that will
 *   perform different tasks on the reflected tree or the filtered tree while the model are
 *   creating these.
 *
 *   An Analyser can be executed at three situations.
 *          - When the Reflected tree is being build. The Analyser will be called once per Node node in the tree.
 *          - After the Reflected tree is build. The Analyser will be called once, with the root Node node.
 *          - After the Filtered tree is build. The Analyser will be called once, with the root GenericTreeNode node.
 *
 */
public class AnalyzerHolder {

    private ArrayList<RTAnalyzer> RTAnalyzers;
    private ArrayList<RTAnalyzer> duringRTAnalyzers;
    private ArrayList<FTAnalyzer> FTAnalyzers;

    public AnalyzerHolder(){
        FTAnalyzers = new ArrayList<>();
        RTAnalyzers = new ArrayList<>();
        duringRTAnalyzers = new ArrayList<>();
    }

    /**
     * Execute all Analysers that should run during the creation of the reflected tree. The Node node is the
     * node that will be analysed next.
     *
     * @param node
     * @return
     */
    public boolean executeDuringRTAnalyzers(Node node){
        boolean check = true;
        for (RTAnalyzer rtAnalyzer : duringRTAnalyzers){
            check = rtAnalyzer.check(node) && check;
        }
        return check;
    }

    /**
     * Execute all Analysers that should run after the creation of the reflected tree. The Node node is the root node.
     *
     * @param node
     * @return
     */
    public boolean executeRTAnalyzers(Node node){
        boolean check = true;
        for (RTAnalyzer rtAnalyzer : RTAnalyzers){
            check = rtAnalyzer.check(node) && check;
        }
        return check;
    }


    /**
     * Execute all Analysers that should run after the creation of the filtered tree. The GenericTreeNode node is the
     * root node.
     *
     */
    public boolean executeFTAnalyzers(GenericTreeNode node){
        boolean check = true;
        for (FTAnalyzer rtAnalyzer : FTAnalyzers){
            check = rtAnalyzer.check(node) && check;
        }
        return check;
    }

    //These could be merge to one but this can be slightly faster, if a lot of analysers
    public void addRTAnalyzers(RTAnalyzer analyzer){ RTAnalyzers.add(analyzer); }
    public void addDuringRTAnalyzers(RTAnalyzer analyzer){ duringRTAnalyzers.add(analyzer); }
    public void addFTAnalyzers(FTAnalyzer analyzer){ FTAnalyzers.add(analyzer); }

    public ArrayList<RTAnalyzer> getRTAnalyzers(){ return RTAnalyzers; }
    public ArrayList<RTAnalyzer> getDuringRTAnalyzers(){ return duringRTAnalyzers; }
    public ArrayList<FTAnalyzer> getFTAnalyzers(){ return FTAnalyzers; }
}
