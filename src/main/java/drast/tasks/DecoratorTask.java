package drast.tasks;

import drast.api.DrASTAPI;

/**
 * Created by gda10jth on 11/20/15.
 * A decorator task is a task that recives an DrASTAPI object and does something with the data afterwards.
 */
public abstract class DecoratorTask implements DrASTTask{
    protected DrASTAPI api;

    public DecoratorTask(DrASTAPI api){
        this.api = api;
    }

    public DecoratorTask(Object root){
        api = new DrASTAPI(root);
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
        api = new DrASTAPI(root);
        api.setFilterPath(filterPath);
        onNewRoot();
    }

    @Override
    public DrASTAPI getAPI() {
        return api;
    }

    protected abstract void runThisTask();
    protected abstract void onNewRoot();

    @Override
    public void printMessage(String type, String message){}
}
