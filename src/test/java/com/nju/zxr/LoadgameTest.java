package com.nju.zxr;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoadgameTest {
    @Test
    public void shouldAnswerWithTrue2()
    {
        Controller testCtrl = new Controller();
        //System.out.println(testCtrl.testFormationInterface(4,3).getName().toString());
        //assertTrue( testCtrl.testLoadGame("D://Intellij_idea_WorkSpace/CalaVSMons/src/test/resources/1.txt")=='M');
        assertTrue(testCtrl.testLoadGame("src/test/resources/1.txt",0)=='M');
    }

    @Test
    public void shouldAnswerWithTrue3()
    {
        Controller testCtrl = new Controller();
        //System.out.println(testCtrl.testFormationInterface(4,3).getName().toString());
        //assertTrue( testCtrl.testLoadGame("D://Intellij_idea_WorkSpace/CalaVSMons/src/test/resources/1.txt")=='M');
        assertTrue(testCtrl.testLoadGame("src/test/resources/2.txt",2)=='K');
    }
}