package drast;

import drast.model.DrAST;
import drast.starter.DrASTStarter;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by gda10jli on 2/11/16.
 */
public class EvaluatorTime {

    private int ITER = 10;
    public EvaluatorTime(){}

    public void run(){
        startCalcASM();
        startExtended();
    }

    private ArrayList<String[]> getArgs(String fileName){
        ArrayList<String[]> args = new ArrayList<>();
        try{
            File file = new File("/home/gda10jli/Documents/jastadddebugger-exjobb/" + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    args.add(line.split(" "));
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return args;
    }

    private void startExtended(){
        try{
            String jarPath = "/home/gda10jli/Documents/extendj/extendj.jar";
            String filterPath = "/home/gda10jli/Documents/jastadddebugger-exjobb/filter.fcl";
            File f = new File("/home/gda10jli/Documents/jastadddebugger-exjobb/extendedJModelTimeResults.txt");
            PrintWriter writer = new PrintWriter(f, "UTF-8");
            start(writer, jarPath, filterPath, getArgs("extendedJFilePaths.txt"));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void startCalcASM(){
        try{
            String jarPath = "/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/compiler.jar";
            String filterPath = "/home/gda10jli/Documents/jastadddebugger-exjobb/filter.fcl";
            File f = new File("/home/gda10jli/Documents/jastadddebugger-exjobb/calcASMModelTimeResults.txt");
            System.out.println(f.toString());
            PrintWriter writer = new PrintWriter(f, "UTF-8");
            start(writer, jarPath, filterPath, getArgs("calcASMFilePaths.txt"));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void start(PrintWriter writer, String jarPath, String filterPath, ArrayList<String[]> args2){
        for (int i = 0; i < args2.size(); i++) {
            String[] arg = args2.get(i);
            DrASTStarter setup = new DrASTStarter(jarPath, filterPath, arg);
            setup.run();
            iter(writer, i , arg[arg.length - 1], setup.getRoot());
        }
    }

    private void iter(PrintWriter writer, int fileNbr , String file, Object root){
        if(root == null){
            System.out.println("ROOT IS NULL");
            return;
        }
        System.out.println("Processing  " + (fileNbr + 1) +  " : " + root);
        String[] timeRef = new String[ITER];
        String[] firstFilterTimes = new String[ITER];
        String[] filterTimes = new String[ITER ];
        String[] totalTime = new String[ITER ];
        DrAST drast = null;
        for(int i = 0; i < ITER; i ++){
            drast = new DrAST(root);
            drast.run();
            timeRef[i] = "" + drast.getBrain().getReflectedTreeTime();
            firstFilterTimes[i] =  "" + drast.getBrain().getFilteredTreeTime();
            totalTime[i] = "" + (drast.getBrain().getFilteredTreeTime() + drast.getBrain().getReflectedTreeTime());
            if(i != 0)
                continue;
            for (int j = 0; j < ITER; j ++){
                drast.getBrain().reApplyFilter();
                filterTimes[j] = "" + drast.getBrain().getFilteredTreeTime();
            }
        }

        writer.print("File " + (fileNbr + 1) + " : " + file.substring(file.lastIndexOf("/") + 1, file.length()) + "\n");
        writer.print("AST Size :\n" + drast.getBrain().getASTSize() + "\n");
        print(writer, "TTime :", totalTime);
        print(writer, "RTime :", timeRef);
        print(writer, "FFTime :", firstFilterTimes);
        print(writer, "FTime :", filterTimes);
    }


    private void print(PrintWriter writer, String name,  String[] times){
        writer.print(name + "\n");
        for (String t : times){
            writer.print(t + "\n");
        }
    }
}
