package drast.model.analyzer;

import drast.model.Node;
import drast.model.filteredtree.GenericTreeNode;

import java.util.ArrayList;

/**
 * Created by gda10jli on 2/17/16.
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

    public boolean executeDuringRTAnalyzers(Node node){
        boolean check = true;
        for (RTAnalyzer rtAnalyzer : duringRTAnalyzers){
            check = rtAnalyzer.check(node) && check ? false : true;
        }
        return true;
    }

    public boolean executeRTAnalyzers(Node node){
        boolean check = true;
        for (RTAnalyzer rtAnalyzer : RTAnalyzers){
            check = rtAnalyzer.check(node) && check ? false : true;
        }
        return true;
    }

    public boolean executeFTAnalyzers(GenericTreeNode node){
        boolean check = true;
        for (FTAnalyzer rtAnalyzer : FTAnalyzers){
            check = rtAnalyzer.check(node) && check ? false : true;
        }
        return true;
    }

    //These could be merge to one but this can be slightly faster, if a lot of analysers
    public void addRTAnalyzers(RTAnalyzer analyzer){ RTAnalyzers.add(analyzer); }
    public void addDuringRTAnalyzers(RTAnalyzer analyzer){ duringRTAnalyzers.add(analyzer); }
    public void addFTAnalyzers(FTAnalyzer analyzer){ FTAnalyzers.add(analyzer); }

    public ArrayList<RTAnalyzer> getRTAnalyzers(){ return RTAnalyzers; }
    public ArrayList<RTAnalyzer> getDuringRTAnalyzers(){ return duringRTAnalyzers; }
    public ArrayList<FTAnalyzer> getFTAnalyzers(){ return FTAnalyzers; }
}
