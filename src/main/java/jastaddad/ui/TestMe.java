package jastaddad.ui;

import configAST.ASTNodeAnnotation;

public class TestMe{
    public TestMe child1;
    public TestMe child2;


    public TestMe(){
        child1 = null;
        child2 = null;
    }

    public TestMe(TestMe child1, TestMe child2){
        this.child1 = child1;
        this.child2 = child2;

    }

    @ASTNodeAnnotation.Child(name="child")
    public TestMe getChild1() {
        return  child1;
    }

    @ASTNodeAnnotation.Child(name="child")
    public TestMe getChild2() {
        return  child2;
    }
}