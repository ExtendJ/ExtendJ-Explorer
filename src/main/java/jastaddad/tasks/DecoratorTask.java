package jastaddad.tasks;

import jastaddad.api.JastAddAdAPI;

/**
 * Created by gda10jth on 11/20/15.
 * A decorator task is a task that recives an JastAddAdAPI object and does something with the data afterwards.
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

    @Override
    public void setRoot(Object root, String filterPath, String defaultDir, boolean opened) {
        api = new JastAddAdAPI(root);
        api.setFilterPath(filterPath);
        onNewRoot();
    }

    @Override
    public JastAddAdAPI getAPI() {
        return api;
    }

    protected abstract void runThisTask();
    protected abstract void onNewRoot();
}
