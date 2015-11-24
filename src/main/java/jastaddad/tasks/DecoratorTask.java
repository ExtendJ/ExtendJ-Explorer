package jastaddad.tasks;

import jastaddad.api.JastAddAdAPI;

/**
 * Created by gda10jth on 11/20/15.
 */
public abstract class DecoratorTask implements JastAddAdTask{
    protected JastAddAdAPI api;

    public DecoratorTask(JastAddAdAPI api){
        this.api = api;
    }

    public DecoratorTask(Object root){
        api = new JastAddAdAPI(root);
    }

    public void run(){
        loadAPI();
        runThisTask();
    }

    protected void loadAPI(){
        if(!api.hasRun())
            api.run();
    }

    protected abstract void runThisTask();
}
