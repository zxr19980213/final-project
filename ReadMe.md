# Java Final Project

161220187

朱向荣

1756245467zxr@gmail.com

## 设计思路

1. 总的模块划分沿袭前几次的思路：设计`Creature`类作为所有生物的基类；设计`Controller`作为战场的管理者与裁判人。

   - `Creature`分为守序阵营`JusticeUnion`和混乱阵营`EvilUnion`，用`Creature`中的成员变量`boolean justice`标记阵营。
   - 葫芦娃抽象为`Calabash`，小喽啰抽象为`Monster`减少冗余的操作，用序号标记同类不同个体。

2. 将葫芦娃抽象为地图上独立行动的线程，为保证线程同步的安全问题，采用较为极端的同步方式，每个线程申请到共享的`controller`后加锁，此时其余线程实际上没有机会在地图上移动，但是由于处理时间较短，可以做到视觉上的“敌进我退，敌退我打”。

3. `Controller`类是整个程序中最重要的部分，由`Main`声明并初始化，作为程序的入口与管理者，也是联系图形界面和内部实现的纽带，其中代码较多，分功能模块介绍：

   - 初始化游戏

     包括`initializeGame`(负责初始化战斗人员)和`initializeFormation`(负责将战斗人员摆放到逻辑地图，将战斗人员图片放置到视图中)和`startGame`(负责将各线程投入线程池)。

   - 管理游戏进程

     包括`gameOver`(告知战斗人员游戏进程)和`detectAround`(帮助战斗人员侦察周边敌情)和`randomMove`(接受战斗人员移动位置的请求并帮助其更新位置)和`attackEnemy`(接受战斗人员大杀四方的请求并处理战斗)和`removeFromMap`(负责处死烈士尸体)。

   - 负责人机交互

     `Controller`成员中包括界面布局`BorderPane`，其中不仅显示并回放战斗过程，还添加了以文字解说战斗进程的`TextArea`和监听用户选项的`ButtonBar`,`Controller`通过监听方式改变程序状态（战斗/回放）。

   - 负责数据/文件管理

     由`Button`进入相应处理函数后调用`saveGame`或`loadGame`或`startGame`。`Controller`中保有一个`historyList`记录每个葫芦娃的每一步行动（移动或死亡），`startGame`程序会同步增加`historyList`记录，`saveGame`即将该记录保存到相应文件，`loadGame`即从相应文件读取全部记录。

## 设计思想

1. 体现了什么？

   - 抽象

     ```java
     public abstract class Creature implements Runnable{
         public enum CreatureName{
             Calabash1("老大"),Calabash2("老二"),Calabash3("老三"),Calabash4("老四"),
             Calabash5("老五"),Calabash6("老六"),Calabash7("老七"),Grandpa("爷爷"),
             Scorpion("蝎精"),Snake("蛇精"),Monster1("喽啰"),Monster2("喽啰"),Monster3("喽啰")
             ,Monster4("喽啰"),Monster5("喽啰");
             private String creaturename;
             CreatureName(String name){
                 creaturename = name;
             }
         }
     }
     ```

     为方便使用，将`CreatureName`抽象为`Creature`的内部类。

   - 封装

     ```java
     public class Controller {
         private BorderPane pane;
         private Stage primaryStage;
     
         private boolean inGame,inBack;
         private int justiceNum,evilNum;
         private ArrayList<Creature> justiceUnion = new ArrayList<Creature>();
         private ArrayList<Creature> evilUnion = new ArrayList<Creature>();
         private ExecutorService threadPool;
         private Creature[][] map;
     }
     ```

     为保证安全，将无需向外界提供修改权限的变量私有化，需要被外界访问的变量采用`getXXX`形式提供接口，将数据与方法分离。

   - 继承

     ```java
     public abstract class Creature implements Runnable;
     class Calabash extends Creature;
     class Monster extends Creature;
     ```

     用抽象类`Creature`，所有生物继承该类，就继承了程序中生物共有的特性。

   - 组合模式设计

     基于`ButtonBar`本身的特性，在其上聚集了许多`Button`，分别定义了不同的处理函数。

     ```java
     	ButtonBar bthbar = new ButtonBar();
         Button startButton = new Button();
         startButton.setText("start");
         startButton.setOnMouseClicked(new EventHandler<MouseEvent>() e1);
         bthbar.getButtons().add(startButton);
     
         Button saveButton = new Button();
         saveButton.setText("save");
         saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() e2);
         bthbar.getButtons().add(saveButton);
     
         Button loadButton = new Button();
         loadButton.setText("load");
         loadButton.setOnMouseClicked(new EventHandler<MouseEvent>() e3);
         bthbar.getButtons().add(loadButton);
     
         pane.setBottom(bthbar);
     ```

   - 装饰器设计模式

     以对象为参数生成新对象以进行功能上的拓展。

     ```java
     freader = new BufferedReader(new FileReader(file));
     ```

   - lambda表达式

     事件响应程序多用匿名函数

     ```java
     startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
             if(inGame==false) {
                 startGame();
             }
         }
     });
     ```

2. 没能做到什么？

   - SOLID设计原则没能很好地体现，由于个人能力问题，写出的代码偏向过程，耦合度较高，难以解耦，这一弊端直接导致单元测试的难度大大提高。高耦合度主要体现在模块划分不够细致，以函数划分模块而非类型划分；采用对象的成员变量传递消息而非参数和返回值传递消息。
   - 泛型程序设计思想没能体现。没能抽象出各个类型共同的行为，所以没有设计泛型函数和泛型类型。

## 程序展示

1. 打开程序


2. 开始游戏


3. 再次点击开始游戏


4. 打开游戏文件


## 游戏规则概述

对于每一个正在运行的生物体线程，若其九宫格范围内有敌人，则选择一个敌人攻击，否则随机移动到地图上一个九宫格范围内有生物体的位置；

主动发起战斗的生物有三分之二的概率获得胜利。