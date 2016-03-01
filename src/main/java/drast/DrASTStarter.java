package drast;

import drast.model.AlertMessage;
import drast.model.CompilerClassLoader;
import drast.views.DrASTView;
import drast.views.DrASTXML;
import drast.views.gui.DrASTGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Observable;
import java.util.jar.JarFile;

/**
 * Created by gda10jth on 1/15/16.
 */
public class DrASTStarter extends Observable {
    private String taskName;
    private DrASTView task;
    private String jarPath;
    private String[] args;
    private String filterPath;
    private String defaultDir;
    private Object root;
    private boolean setRoot = true;

    public DrASTStarter(String jarPath, String filterPath, String[] args) {
        init(jarPath, filterPath, args);
    }

    public DrASTStarter(String taskName, String jarPath, String filterPath, String[] args) {
        this.taskName = taskName;
        init(jarPath, filterPath, args);
    }

    public DrASTStarter(DrASTView task, String jarPath, String filterPath, String[] args) {
        this.task = task;
        this.taskName = "";
        init(jarPath, filterPath, args);
    }

    private void init(String jarPath, String filterPath, String[] args){
        this.jarPath = jarPath;
        this.args = args;
        this.filterPath = filterPath;
    }

    public void setRootExecution(boolean setRoot){ this.setRoot = setRoot; }

    public void setRoot(){ task.setRoot(root, filterPath, defaultDir, true);}

    private void print(int type, String message){
        setChanged();
        notifyObservers(new AlertMessage(type, message));
    }

    public Object getRoot(){return root; }

    public boolean run(){
        try{
            URL url = new URL("file:" + jarPath);
            ArrayList<URL> urlList = new ArrayList<>();
            urlList.add(url);
            CompilerClassLoader urlClassLoader = new CompilerClassLoader(urlList);

            // Find and instantiate the main java file in the jar.
            File file = new File(jarPath);
            defaultDir = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(file.separator)) + file.separator;
            JarFile j =  new JarFile(file);
            String mainClassName = j.getManifest().getMainAttributes().getValue("Main-Class");
            Class cl = Class.forName(mainClassName, true, urlClassLoader);

            // remove some java security, find the method we are looking for and invoke the method to get the new root.
            SystemExitControl.forbidSystemExitCall();
            long time = System.currentTimeMillis();
            try {
                Method mainMethod = cl.getMethod("main", String[].class);
                mainMethod.invoke(null, new Object[]{args});
                SystemExitControl.enableSystemExitCall();
                return fetchRootAndStartView(cl, cl, defaultDir, time);
            }catch (NoSuchMethodException e) {
                print(AlertMessage.SETUP_FAILURE ,"Could not find the compiler's main method");
                //e.printStackTrace();
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (InvocationTargetException e) {
                if(e.getTargetException().getClass() != SystemExitControl.ExitTrappedException.class) {
                    e.printStackTrace();
                    print(AlertMessage.SETUP_FAILURE, "compiler error : " + (e.getMessage() != null ? e.getMessage() : e.getCause()));
                }else {
                    fetchRootAndStartView(cl, cl, defaultDir, time);
                }
            }
            SystemExitControl.enableSystemExitCall();

        } catch (MalformedURLException e) {
            System.out.println("asdasdas");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("asdasdas");
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            System.out.println("asdasdas");
            print(AlertMessage.SETUP_FAILURE, "Could not find jar file, check path");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("asdas4das");
            e.printStackTrace();
        } catch (Throwable e ){
            e.printStackTrace();
        }
        return false;
    }

    private boolean fetchRootAndStartView(Class cl, Object main, String defaultDir, long time){
        boolean success = false;
        try {
            Field rootField = cl.getField("DrAST_root_node");
            rootField.setAccessible(true);
            root = rootField.get(cl);
            print(AlertMessage.VIEW_MESSAGE , "Compiler finished after : " + (System.currentTimeMillis() - time) + " ms");

            success = true;
        } catch (NoSuchFieldException e) {
            print(AlertMessage.SETUP_FAILURE, "Could not find field : DrAST_root_node in the main class. \n" +
                    "Make sure this is implemented correctly check the README file for instructions");
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if(success){
            // If we got the root without crashing sent the root a task.(success)
            if (task == null && taskName != null) {
                switch (taskName) {
                    case "DrASTGUI":
                        this.task = new DrASTGUI();
                        break;
                    case "DrASTXML":
                        this.task = new DrASTXML(root);
                        break;
                }
            }
            if(setRoot && task != null)
                task.setRoot(root, filterPath, defaultDir, true);
        }

        return success;
    }

}

