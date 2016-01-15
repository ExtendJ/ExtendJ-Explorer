package jastaddad;

import jastaddad.api.JastAddAdAPI;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.nodeinfo.NodeInfo;
import jastaddad.tasks.JastAddAdTask;
import jastaddad.tasks.JastAddAdXML;
import jastaddad.ui.JastAddAdUI;
import jastaddad.ui.UIMonitor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Permission;
import java.util.jar.JarFile;

/**
 * Created by gda10jth on 1/15/16.
 */
public class JastAddAdSetup {
    private String taskName;
    private JastAddAdTask task;
    private String jarPath;
    private String[] args;
    private String filterPath;

    public JastAddAdSetup(String taskName, String jarPath, String filterPath, String[] args) {
        this.taskName = taskName;
        this.task = null;
        init(jarPath, filterPath, args);
    }

    public JastAddAdSetup(JastAddAdTask task, String jarPath, String filterPath, String[] args) {
        this.task = task;
        this.taskName = "";
        init(jarPath, filterPath, args);
    }

    private void init(String jarPath, String filterPath, String[] args){
        this.jarPath = jarPath;
        this.args = args;
        this.filterPath = filterPath;
    }

    public void run(){
        Object root = null;
        boolean success = false;
        try {
            // Add the jar to the classpath with reflection.
            URL url = new URL("file:" + jarPath);
            URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Class urlClass = URLClassLoader.class;
            Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(urlClassLoader, new Object[]{url});

            // Find and instantiate the main java file in the jar.
            JarFile j =  new JarFile(new File(jarPath));
            String mainClassName = j.getManifest().getMainAttributes().getValue("Main-Class");
            URLClassLoader loader = new URLClassLoader (new URL[]{url}, this.getClass().getClassLoader());
            Class cl = Class.forName(mainClassName, true, loader);
            Object main = cl.newInstance();

            // remove some java security, find the method we are looking for and invoke the method to get the new root.
            SystemExitControl.forbidSystemExitCall();
            for(Method m : main.getClass().getMethods()){
                System.out.println(m.getName());
                if(m.getName().equals("runDebugger")){
                    root = m.invoke(main, new Object[]{args});
                    System.out.println(root);
                    success = true;
                }
            }

            SystemExitControl.enableSystemExitCall();

            loader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
                System.exit(1);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            success = false;
        }

        if(success){
            // If we got the root without crashing sent the root a task.(success)
            if (task == null) {
                switch (taskName) {
                    case "JastAddAdUI":
                        this.task = new JastAddAdUI(root);
                        break;
                    case "JastAddAdXML":
                        this.task = new JastAddAdXML(root);
                        break;
                }
            }
            task.setRoot(root);
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