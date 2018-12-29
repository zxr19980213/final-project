package com.nju.zxr;

@MyClassAnno(ClassM="base of all creatures")
public abstract class Creature implements Runnable{
    public enum CreatureName{
        Calabash1,Calabash2,Calabash3,Calabash4,
        Calabash5,Calabash6,Calabash7,Grandpa,
        Scorpion,Snake,Monster1,Monster2,Monster3,
        Monster4,Monster5;
    }

    protected CreatureName name;
    protected int x,y;
    protected boolean justice;
    protected boolean alive;

    static Controller myController;

    Creature(){
        name = null;
        x=-1;
        y=-1;
        justice=true;
        alive=true;
    }
    Creature(CreatureName name){
        this();
        this.name = name;
    }

    public CreatureName getName(){
        return this.name;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public boolean getUnion() {
        return this.justice;
    }
    public void changePosition(int i,int j){
        this.x = j;
        this.y = i;
    }


    public void initializePosition(int x,int y){
        this.x = x;
        this.y = y;
        this.alive=true;
    }

    public void reGame(){
        this.alive = true;
        this.initializePosition(-1,-1);
    }

    @MyMethodAnno(MethodF="tasks on tread")
    public void run(){
        try{
            while (alive == true&&myController.gameOver(this)==false) {
                synchronized (myController) {
                    Thread.sleep(300);
                    if(alive == true&&myController.gameOver(this)==false) {
                        boolean attack = myController.detectAround(this);
                        if (attack == true) {
                            if(!myController.attackEnemy(this)){
                                break;
                            }
                        } else {
                            myController.randomMove(this);
                        }
                    }
                    else{
                        break;
                    }
                }
                Thread.sleep(200);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(this.getName()+" break");
        }
    }
}

@MyClassAnno(ClassM="differ calas by number")
class Calabash extends Creature{
    private int number;

    Calabash(int number){
        super();
        this.number = number;
        switch(number){
            case 0:name = CreatureName.Calabash1;
                 break;
            case 1:name = CreatureName.Calabash2;
                 break;
            case 2:name = CreatureName.Calabash3;
                 break;
            case 3:name = CreatureName.Calabash4;
                 break;
            case 4:name = CreatureName.Calabash5;
                 break;
            case 5:name = CreatureName.Calabash6;
                 break;
            case 6:name = CreatureName.Calabash7;
                 break;
        }
        justice = true;
    }
}

class GrandPa extends Creature{
    GrandPa(){
        super(CreatureName.Grandpa);
        justice = true;
    }
}

class Snake extends Creature{
    Snake(){
        super(CreatureName.Snake);
        justice = false;
    }
}

class Scorpion extends Creature{
    Scorpion(){
        super(CreatureName.Scorpion);
        justice = false;
    }
}

@MyClassAnno(ClassM="differ monsters by number")
class Monster extends Creature{
    private int number;
    Monster(int number){
        super();
        this.number = number;
        switch(number){
            case 0:this.name = CreatureName.Monster1;break;
            case 1:this.name = CreatureName.Monster2;break;
            case 2:this.name = CreatureName.Monster3;break;
            case 3:this.name = CreatureName.Monster4;break;
            case 4:this.name = CreatureName.Monster5;break;
        }
        justice = false;
    }
}