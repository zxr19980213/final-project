package com.nju.zxr;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormationTest {
    @Test
    public void shouldAnswerWithTrue()
    {
        Controller testCtrl = new Controller();
        //System.out.println(testCtrl.testFormationInterface(4,3).getName().toString());
        assertTrue( testCtrl.testFormationInterface(4,3).getName()== Creature.CreatureName.Grandpa);
    }

    @Test
    public void shouldAnswerWithTrue1()
    {
        Controller testCtrl = new Controller();
        //System.out.println(testCtrl.testFormationInterface(4,3).getName().toString());
        assertTrue( testCtrl.testFormationInterface(10,7).getName()== Creature.CreatureName.Scorpion);
    }
}