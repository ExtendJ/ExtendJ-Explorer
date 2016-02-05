package drast.views;

import drast.model.DrAST;

/**
 * Created by gda10jth on 11/20/15.
 * A decorator task is a task that recives an DrASTAPI object and does something with the data afterwards.
 */
public abstract class DecoratorView implements DrASTView {
    protected DrAST api;

    public DecoratorView(DrAST api){
        this.api = api;
    }

    public DecoratorView(Object root){
        api = new DrAST(root);
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
        api = new DrAST(root);
        api.setFilterPath(filterPath);
        onNewRoot();
    }

    @Override
    public DrAST getAPI() {
        return api;
    }

    protected abstract void runThisTask();
    protected abstract void onNewRoot();

    @Override
    public void printMessage(int type, String message){}
}
