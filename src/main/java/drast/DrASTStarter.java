package drast;

import drast.model.AlertMessage;
import drast.model.CompilerClassLoader;
import drast.views.DrASTView;
import drast.views.DrASTXML;
import drast.views.gui.DrASTGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;
import java.util.ArrayList;
import java.util.jar.JarFile;

/**
 * Created by gda10jth on 1/15/16.
 */
public class DrASTStarter {
    private String taskName;
    private DrASTView task;
    private String jarPath;
    private String[] args;
    private String filterPath;
    private Object root;
    private long compilerTime;

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

    private void print(String message){
        if(task != null)
            task.printMessage(AlertMessage.SETUP_FAILURE, message);
    }

    public long getCompilerTime(){ return compilerTime; }


    public Object getRoot(){return root; }

    public void run(){
        boolean success = false;
        String defaultDir = "";
        Class cl;
        Object main;
        long time;
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
            cl = Class.forName(mainClassName, true, urlClassLoader);
            main = cl.newInstance();

            // remove some java security, find the method we are looking for and invoke the method to get the new root.
            SystemExitControl.forbidSystemExitCall();
            time = System.currentTimeMillis();

            try {
                Method mainMethod = cl.getMethod("main", String[].class);
                mainMethod.invoke(main, new Object[]{args});
                SystemExitControl.enableSystemExitCall();
                fetchRootAndStartView(cl, main, defaultDir, time);
            }catch (NoSuchMethodException e) {
                print("Could not find the compiler's main method");
                //e.printStackTrace();
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (InvocationTargetException e) {
                if(e.getTargetException().getClass() != SystemExitControl.ExitTrappedException.class) {
                    e.printStackTrace();
                    print("compiler error : " + (e.getMessage() != null ? e.getMessage() : e.getCause()));
                }else {
                    fetchRootAndStartView(cl, main, defaultDir, time);
                }
            }
            SystemExitControl.enableSystemExitCall();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            print("Could not find jar file, check path");
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchRootAndStartView(Class cl, Object main, String defaultDir, long time){
        boolean success = false;
        try {
            Field rootField = cl.getField("DrAST_root_node");
            rootField.setAccessible(true);
            root = rootField.get(main);
            compilerTime = System.currentTimeMillis() - time;
            print("Compiler finished after : " + compilerTime + " ms");

            success = true;
        } catch (NoSuchFieldException e) {
            print("Could not find field : DrAST_root_node in the main class. \n" +
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
            if(task != null)
                task.setRoot(root, filterPath, defaultDir, true);
        }
    }

}

