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
public class DrASTSetup {
    private String taskName;
    private DrASTView task;
    private String jarPath;
    private String[] args;
    private String filterPath;

    public DrASTSetup(String taskName, String jarPath, String filterPath, String[] args) {
        this.taskName = taskName;
        this.task = null;
        init(jarPath, filterPath, args);
    }

    public DrASTSetup(DrASTView task, String jarPath, String filterPath, String[] args) {
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

    public void run(){
        Object root = null;
        boolean success = false;
        String defaultDir = "";
        try {
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
            Object main = cl.newInstance();

            // remove some java security, find the method we are looking for and invoke the method to get the new root.
            SystemExitControl.forbidSystemExitCall();
            long time = System.currentTimeMillis();
            Method mainMethod = cl.getMethod("main", String[].class);
            mainMethod.invoke(main, new Object[]{args});
            Field rootField = cl.getField("DrAST_root_node");
            rootField.setAccessible(true);
            root = rootField.get(main);
            time = System.currentTimeMillis() - time;
            System.out.println("Time for compiler : " + time);
            SystemExitControl.enableSystemExitCall();

            success = true;
        } catch (NoSuchFieldException e) {
            print("Could not find field : DrAST_root_node in the main class. \n" +
                "Make sure this is implemented correctly check the README file for instructions");
            //e.printStackTrace();
        } catch (NoSuchMethodException e) {
            print("Could not find the compiler's main method");
            //e.printStackTrace();
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
        } catch (InvocationTargetException e) {
            if(e.getTargetException().getClass() != SystemExitControl.ExitTrappedException.class) {
                e.printStackTrace();
                print("compiler error : " + (e.getMessage() != null ? e.getMessage() : e.getCause()));
            }
        }

        if(success){
            // If we got the root without crashing sent the root a task.(success)
            if (task == null) {
                switch (taskName) {
                    case "DrASTGUI":
                        this.task = new DrASTGUI();
                        break;
                    case "DrASTXML":
                        this.task = new DrASTXML(root);
                        break;
                }
            }
            task.setRoot(root, filterPath, defaultDir, true);
        }
    }
}

class SystemExitControl {

    public static class ExitTrappedException extends SecurityException {
    }

    public static void forbidSystemExitCall() {
        final SecurityManager securityManager = new SecurityManager() {
            @Override
            public void checkPermission(Permission permission) {
                if (permission.getName().contains("exitVM")) {
                    throw new ExitTrappedException();
                }
            }
        };
        System.setSecurityManager(securityManager);
    }

    public static void enableSystemExitCall() {
        System.setSecurityManager(null);
    }
}