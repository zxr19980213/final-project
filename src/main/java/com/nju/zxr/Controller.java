package com.nju.zxr;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@MyClassAnno(ClassM="like a god to control")
public class Controller {
    public BorderPane pane;
    private Stage primaryStage;

    private boolean inGame,inBack,inTest;
    private int justiceNum,evilNum;
    private ArrayList<Creature> justiceUnion = new ArrayList<Creature>();
    private ArrayList<Creature> evilUnion = new ArrayList<Creature>();
    private ExecutorService threadPool;
    private Creature[][] map;
    ArrayList<String> historylist = new ArrayList<String>();
    Timeline bt = new Timeline();

    Controller(){
        this.inGame = false;
        this.inBack = false;
        this.inTest = false;
        this.justiceNum = 0;
        this.evilNum = 0;
        this.threadPool = null;
    }


    @MyMethodAnno(MethodF = "interface to test formation",WTime = "181228")
    Creature testFormationInterface(int i,int j){
        this.inTest = true;
        initializeGame();
        initializeFormation();
        return map[j][i];
    }
    @MyMethodAnno(MethodF = "interface to test back",WTime = "181228")
    char testLoadGame(String filename,int pos){
        File testFile = new File(filename);
        if(testFile!=null) {
            historylist.clear();
            BufferedReader freader = null;
            try{
                freader = new BufferedReader(new FileReader(testFile));
                String historylog;
                while((historylog=freader.readLine())!=null){
                    historylist.add(historylog);
                }
                freader.close();
                //System.out.println(historylist.get(0).charAt(0));
                return historylist.get(pos).charAt(0);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("read error");
            }
        }
        return '0';
    }

    @MyMethodAnno(MethodF = "put creature on map and show in view",WTime = "181220")
    //[0-19]*[0,14]-800*600
    private void putCreature(Creature c,int x,int y){
        map[y][x] = c;
        if(inTest!=true) {
            ImageView cImageView = getImageView(c);
            cImageView.setOpacity(1);
            cImageView.setRotate(0);
            cImageView.setX(x * 40);
            cImageView.setY(y * 40);
            if (!inBack) {
                if(!pane.getChildren().contains(cImageView))
                pane.getChildren().add(cImageView);
            }
        }
    }

    @MyMethodAnno(MethodF = "initialize justice on map,call putcreature",WTime = "181220")
    private void initializeJusticeUnion(){
        for(int i=0 ; i<8 ; ++i){
            putCreature(justiceUnion.get(i),4,3+i);
            justiceUnion.get(i).initializePosition(4, 3+i);
        }
    }
    @MyMethodAnno(MethodF = "initialize evil on map,call putcreature",WTime = "181220")
    private void initializeEvilUnion(){
        putCreature(evilUnion.get(0),10,7);
        evilUnion.get(0).initializePosition(10,7);
        for(int i=1;i<4;++i){
            //System.out.println(evilUnion.get(i).getName());
            putCreature(evilUnion.get(i),10+i,7-i);
            evilUnion.get(i).initializePosition(10+i,7-i);
        }
        for(int i=4;i<7;++i){
            putCreature(evilUnion.get(i),7+i,4+i);
            evilUnion.get(i).initializePosition(7+i,4+i);
        }
    }
    @MyMethodAnno(MethodF = "call initializeJustice and initializeEvil",WTime = "181220")
    public void initializeFormation(){
        map = new Creature[15][20];
        for(int i=0 ; i<15 ; ++i){
            for(int j=0 ; j<20 ; ++j){
                map[i][j] = null;
            }
        }
        initializeJusticeUnion();
        initializeEvilUnion();
    }

    @MyMethodAnno(MethodF = "Toolbar with buttons")
    public Pane initializeGUI(){
        pane = new BorderPane();

        ButtonBar bthbar = new ButtonBar();

        Button startButton = new Button();
        startButton.setText("start");
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(inGame==false) {
                    startGame();
                }
            }
        });
        bthbar.getButtons().add(startButton);


        Button saveButton = new Button();
        saveButton.setText("save");
        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(inGame==false) {
                    saveGame();
                }
            }
        });
        bthbar.getButtons().add(saveButton);

        Button loadButton = new Button();
        loadButton.setText("load");
        loadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(inGame==false){
                    inBack=true;
                    loadGame();
                }
            }
        });
        bthbar.getButtons().add(loadButton);

        pane.setBottom(bthbar);

        TextArea gameText = new TextArea();
        gameText.setEditable(false);
        gameText.setPrefHeight(600);
        gameText.setPrefColumnCount(20);
        //pane.setRight(gameText);
        gameText.setText("");
        //for(int i=0 ; i<10 ; ++i) {
        //    gameText.appendText("hello world\r\n");
        //}
        pane.setRight(gameText);


/*
        startButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if(inGame==false){
                    System.out.println(e.getCode());
                    if(e.getCode() == KeyCode.SPACE){
                        startGame();
                    }
                }
            }
        });
*/
        pane.setPrefSize(800,600);
        this.setBackground();
        return pane;
    }
    @MyMethodAnno(MethodF = "prepare data for game")
    public void initializeGame(){
        justiceNum = 8;
        evilNum = 7;
        justiceUnion.add(new GrandPa());
        for(int i=0 ; i<7 ; ++i){
            justiceUnion.add(new Calabash(i));
        }

        evilUnion.add(new Scorpion());
        evilUnion.add(new Snake());
        for(int i=0 ; i<5 ; ++i){
            evilUnion.add(new Monster(i));
        }
        Creature.myController = this;
    }
    private void setBackground(){
        Image backgroundImage = new Image("/background.jpg");
        ImageView backgroundImageView = new ImageView();
        backgroundImageView.setImage(backgroundImage);
        backgroundImageView.setFitWidth(800);
        backgroundImageView.setFitHeight(600);
        /*
        backgroundImageView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if(inGame==false){
                    System.out.println(e.getCode());
                    if(e.getCode() == KeyCode.SPACE){
                        startGame();
                    }
                }
            }
        });
        */
        pane.setCenter(backgroundImageView);
    }

    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        initializeFormation();
        //keyboardListener();
    }
    private void startGame(){
        ArrayList<Creature> fighters = new ArrayList<Creature>();
        fighters.addAll(justiceUnion);
        fighters.addAll(evilUnion);
        for(int i=0 ; i<fighters.size() ; ++i){
            fighters.get(i).reGame();
        }
        justiceNum=8;
        evilNum=7;
        initializeFormation();
        historylist.clear();
        TextArea tempT = (TextArea)pane.getRight();
        tempT.setText("");
        //System.out.println("no");
        inGame = true;
        inBack = false;
        inTest = false;
        threadPool = Executors.newCachedThreadPool();
        /*
        for(int i=0 ; i<justiceUnion.size() ; ++i){
            System.out.println(i);
            //threadPool.execute(justiceUnion.get(i));
            threadPool.submit(justiceUnion.get(i));
        }
        for(int i=0 ; i<evilUnion.size() ; ++i){
            System.out.println(i);
            //threadPool.execute(evilUnion.get(i));
            threadPool.submit(evilUnion.get(i));
        }
        */
        for(int i=0 ; i<fighters.size();++i){
            threadPool.submit(fighters.get(i));
        }
        threadPool.shutdown();
    }
    private void saveGame(){
        FileChooser filesave = new FileChooser();
        filesave.setTitle("save file");
        filesave.getExtensionFilters().add(new FileChooser.ExtensionFilter("game files", "*.txt"));
        File file = filesave.showSaveDialog(primaryStage);
        if(file!=null){
            try {
                FileWriter fwriter = new FileWriter(file);
                for(String s:historylist){
                    fwriter.write(s);
                    fwriter.write("\n");
                }
                fwriter.flush();
                fwriter.close();
            }catch(Exception e){
                System.out.println("save error");
                e.printStackTrace();
            }
        }
    }
    private void loadGame(){
        initializeFormation();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("load game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("game files", "*.txt"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if(file!=null) {
            TextArea loadT = (TextArea)pane.getRight();
            loadT.setText("");
            historylist.clear();
            BufferedReader freader = null;
            try{
                freader = new BufferedReader(new FileReader(file));
                String historylog;
                while((historylog=freader.readLine())!=null){
                    historylist.add(historylog);
                }
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("read error");
            }
            for(int i=0 ; i<historylist.size() ; ++i){
                playbackAction(historylist.get(i),i);
            }
            Platform.runLater(new Runnable(){
                public void run(){
                    bt.play();
                }
            });
        }
    }


    public boolean detectAround(Creature c){
        int x = c.getX();
        int y = c.getY();
        for(int i=y-1 ; i<=y+1 ; ++i){
            for(int j=x-1 ; j<=x+1 ; ++j){
                if(0<=i&&i<=14&&0<=j&&j<=19){
                    if(map[i][j]!=null&&map[i][j].getUnion()!=c.getUnion()){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean attackEnemy(Creature c){
        Creature e = null;
        boolean found = false;
        for(int i=c.getY()-1 ; i<=c.getY()+1 ; ++i){
            for(int j=c.getX()-1 ; j<=c.getX()+1 ; ++j){
                if(found==false&&0<=i&&i<=14&&0<=j&&j<=19){
                    if(map[i][j]!=null&&map[i][j].getUnion()!=c.getUnion()){
                        e = map[i][j];
                        found = true;
                    }
                }
            }
        }
        Random r = new Random(System.currentTimeMillis());
        int ran = r.nextInt(100)%3;
        if(ran==0){
            if(c.getUnion()==true){
                this.justiceNum--;
            }
            else{
                this.evilNum--;
            }
            c.alive = false;
            TextArea textArea = (TextArea)(pane.getRight());
            String text = e.getName().toString()+" killed "+c.getName().toString()+"\n";
            textArea.appendText(text);
            storeKill(c.getY(),c.getX());
            removeFromMap(c);
            map[c.getY()][c.getX()]=null;
            c.changePosition(-1,-1);
            return false;
        }
        else{
            if(e.getUnion()==true){
                this.justiceNum--;
            }
            else{
                this.evilNum--;
            }
            e.alive = false;
            TextArea textArea = (TextArea)(pane.getRight());
            String text = c.getName().toString()+" killed "+e.getName().toString()+"\n";
            textArea.appendText(text);
            storeKill(e.getY(),e.getX());
            removeFromMap(e);
            map[e.getY()][e.getX()]=null;
            e.changePosition(-1,-1);
            return true;
        }
    }
    public void randomMove(final Creature c){
        final int originI = c.getY();
        final int originJ = c.getX();
        Random r = new Random(System.currentTimeMillis());
        boolean found = false;
        int i=0,j=0;
        while(found == false){
            i=r.nextInt(15);
            j=r.nextInt(20);
            if(map[i][j]==null){
                for(int ii=i-1 ; ii<=i+1 ; ++ii){
                    for(int jj=j-1 ; jj<=j+1 ; ++jj){
                        if(0<=ii&&ii<=14&&0<=jj&&jj<=19&&map[ii][jj]!=null&&map[ii][jj]!=c){
                            found = true;
                        }
                    }
                }
            }
        }
        map[c.getY()][c.getX()]=null;
        map[i][j]=c;
        c.changePosition(i,j);
        if(originI==i&&originJ==j)return;
        final int newI = i;
        final int newJ = j;
        TextArea textArea = (TextArea)(pane.getRight());
        String text = c.getName().toString()+" move from("+Integer.toString(originI)+","+Integer.toString(originJ)+") to ("+Integer.toString(newI)+","+Integer.toString(newJ)+")\n";
        textArea.appendText(text);
        storeMove(originI,originJ,newI,newJ);
        Platform.runLater(new Runnable(){
            public void run(){
                ImageView cImageView = getImageView(c);
                Timeline t = new Timeline();
                t.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO,new KeyValue(cImageView.xProperty(),originJ*40)),
                        new KeyFrame(new Duration(500),new KeyValue(cImageView.xProperty(),newJ*40)),
                        new KeyFrame(Duration.ZERO,new KeyValue(cImageView.yProperty(),originI*40)),
                        new KeyFrame(new Duration(500),new KeyValue(cImageView.yProperty(),newI*40))
                );
                t.play();
                //t.stop();
                //t=new Timeline();
                cImageView.setX(newJ*40);
                cImageView.setY(newI*40);
            }
        });
    }
    public void removeFromMap(final Creature c){

        Platform.runLater(new Runnable(){
            public void run(){
                ImageView cImageView = getImageView(c);
                Timeline t = new Timeline();
                t.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO,new KeyValue(cImageView.rotateProperty(),0)),
                        new KeyFrame(new Duration(500),new KeyValue(cImageView.rotateProperty(),180)),
                        new KeyFrame(Duration.ZERO,new KeyValue(cImageView.opacityProperty(),1)),
                        new KeyFrame(new Duration(500),new KeyValue(cImageView.opacityProperty(),0.1))
                );
                t.play();
                //t.stop();
                //t=new Timeline();
            }
        });
        /*
        final FutureTask<String> query = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                ImageView cImageView = getImageView(c);
                Timeline t = new Timeline();
                t.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO,new KeyValue(cImageView.rotateProperty(),0)),
                        new KeyFrame(new Duration(100),new KeyValue(cImageView.rotateProperty(),180)),
                        new KeyFrame(Duration.ZERO,new KeyValue(cImageView.opacityProperty(),1)),
                        new KeyFrame(new Duration(100),new KeyValue(cImageView.opacityProperty(),0))
                );
                t.play();
                return "success";
            }
        });
        Platform.runLater(query);
        try {
            String vcode = query.get();
            System.out.println(vcode);
        }catch(Exception e){
            e.printStackTrace();
        }
        */
    }

    @MyMethodAnno(MethodF = "for playback")
    public void playbackAction(String s,int time){
        if(s.charAt(0)=='K'){
            int originI = Integer.parseInt(s.substring(1,3));
            int originJ = Integer.parseInt(s.substring(3,5));
            backKill(originI,originJ,time);
        }
        else if(s.charAt(0)=='M'){
            int originI = Integer.parseInt(s.substring(1,3));
            int originJ = Integer.parseInt(s.substring(3,5));
            int newI = Integer.parseInt(s.substring(5,7));
            int newJ = Integer.parseInt(s.substring(7,9));
            backMove(originI,originJ,newI,newJ,time);
        }
    }
    @MyMethodAnno(MethodF = "for playback")
    public void backKill(int i,int j,int time){
        if(map[i][j]!=null){
            map[i][j].changePosition(-1, -1);
            map[i][j].alive = false;
            Creature c = map[i][j];
            TextArea textArea = (TextArea)(pane.getRight());
            String text = c.getName().toString()+" got killed\n";
            textArea.appendText(text);
            //Platform.runLater(new Runnable(){
                //public void run(){
                    ImageView cImageView = getImageView(c);
                    //Timeline t = new Timeline();
                    bt.getKeyFrames().addAll(
                            new KeyFrame(new Duration(time*500),new KeyValue(cImageView.rotateProperty(),0)),
                            new KeyFrame(new Duration(400+time*500),new KeyValue(cImageView.rotateProperty(),180)),
                            new KeyFrame(new Duration(time*500),new KeyValue(cImageView.opacityProperty(),1)),
                            new KeyFrame(new Duration(400+time*500),new KeyValue(cImageView.opacityProperty(),0.1))
                    );
                    //t.play();
                    //System.out.println("k");
                    //t.stop();
                    //t=new Timeline();
                //}
            //});
            map[i][j] = null;
        }
    }
    @MyMethodAnno(MethodF = "for playback")
    public void backMove(int originI,int originJ,int newI,int newJ,int time){
        if(map[originI][originJ]!=null&&map[newI][newJ]==null){
            //System.out.println(map[originI][originJ].getName());
            Creature c = map[originI][originJ];
            map[originI][originJ]=null;
            map[newI][newJ] = c;
            c.changePosition(newI,newJ);
            TextArea textArea = (TextArea)(pane.getRight());
            String text = c.getName().toString()+" move from("+Integer.toString(originI)+","+Integer.toString(originJ)+") to ("+Integer.toString(newI)+","+Integer.toString(newJ)+")\n";
            textArea.appendText(text);

            //Platform.runLater(new Runnable(){
                //public void run(){
                    ImageView cImageView = getImageView(c);
                    //Timeline t = new Timeline();
                    bt.getKeyFrames().addAll(
                            new KeyFrame(new Duration(time*500), new KeyValue(cImageView.xProperty(), originJ * 40)),
                            new KeyFrame(new Duration(400+time*500), new KeyValue(cImageView.xProperty(), newJ * 40)),
                            new KeyFrame(new Duration(time*500), new KeyValue(cImageView.yProperty(), originI * 40)),
                            new KeyFrame(new Duration(400+time*500), new KeyValue(cImageView.yProperty(), newI * 40))
                    );
                    //t.play();
                    //System.out.println("m");
                    //t.stop();
                    //t=new Timeline();
                    //cImageView.setX(newJ * 40);
                    //cImageView.setY(newI * 40);
                //}
            //});
            /*
            final FutureTask<String> query = new FutureTask<String>(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    System.out.println("here");
                    ImageView cImageView = getImageView(c);
                    Timeline t = new Timeline();
                    t.getKeyFrames().addAll(
                            new KeyFrame(Duration.ZERO, new KeyValue(cImageView.xProperty(), originJ * 40)),
                            new KeyFrame(new Duration(100), new KeyValue(cImageView.xProperty(), newJ * 40)),
                            new KeyFrame(Duration.ZERO, new KeyValue(cImageView.yProperty(), originI * 40)),
                            new KeyFrame(new Duration(100), new KeyValue(cImageView.yProperty(), newI * 40))
                    );
                    t.play();
                    cImageView.setX(newJ * 40);
                    cImageView.setY(newI * 40);
                    return "success";
                }
            });
            Platform.runLater(query);
            try {
                String vcode = query.get();
                System.out.println(vcode);
            }catch(Exception e){
                e.printStackTrace();
            }
*/
        }
    }

    @MyMethodAnno(MethodF = "tell whether game is over during play")
    public boolean gameOver(Creature c){
        if(justiceNum==0||evilNum==0){
            if(justiceNum==0){
                TextArea textArea = (TextArea)(pane.getRight());
                String text = c.getName().toString()+":We got what we want\n";
                textArea.appendText(text);
            }
            else{
                TextArea textArea = (TextArea)(pane.getRight());
                String text = c.getName().toString()+":Justice will never be absent\n";
                textArea.appendText(text);
            }
            inGame = false;
            return true;
        }
        return false;
    }

    public void storeMove(int originI,int originJ,int newI,int newJ){
        String action = "M";
        String Soi = String.valueOf(originI);
        if(Soi.length()==1){
            Soi = '0'+Soi;
        }
        String Soj = String.valueOf(originJ);
        if(Soj.length()==1){
            Soj = '0'+Soj;
        }
        String Sni = String.valueOf(newI);
        if(Sni.length()==1){
            Sni = '0'+Sni;
        }
        String Snj = String.valueOf(newJ);
        if(Snj.length()==1){
            Snj = '0'+Snj;
        }
        historylist.add(action+Soi+Soj+Sni+Snj);
    }
    public void storeKill(int originI,int originJ){
        String action = "K";
        String Soi = String.valueOf(originI);
        if(Soi.length()==1){
            Soi = '0'+Soi;
        }
        String Soj = String.valueOf(originJ);
        if(Soj.length()==1){
            Soj = '0'+Soj;
        }
        historylist.add(action+Soi+Soj);
    }

    private ImageView getImageView(Creature c){
        ImageView cImageView = null;
        switch(c.getName()){
            case Snake: cImageView = CreaturePicture.Snake.imageView;break;
            case Grandpa:cImageView = CreaturePicture.GrandPa.imageView;break;
            case Monster1:cImageView = CreaturePicture.Monster1.imageView;break;
            case Monster2:cImageView = CreaturePicture.Monster2.imageView;break;
            case Monster3:cImageView = CreaturePicture.Monster3.imageView;break;
            case Monster4:cImageView = CreaturePicture.Monster4.imageView;break;
            case Monster5:cImageView = CreaturePicture.Monster5.imageView;break;
            case Scorpion:cImageView = CreaturePicture.Scorpion.imageView;break;
            case Calabash1:cImageView = CreaturePicture.Calabash1.imageView;break;
            case Calabash2:cImageView = CreaturePicture.Calabash2.imageView;break;
            case Calabash3:cImageView = CreaturePicture.Calabash3.imageView;break;
            case Calabash4:cImageView = CreaturePicture.Calabash4.imageView;break;
            case Calabash5:cImageView = CreaturePicture.Calabash5.imageView;break;
            case Calabash6:cImageView = CreaturePicture.Calabash6.imageView;break;
            case Calabash7:cImageView = CreaturePicture.Calabash7.imageView;break;
        }
        return cImageView;
    }
    /*
    public void keyboardListener(){
        pane.setFocusTraversable(true);
        pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if(inGame==false){
                    System.out.println(e.getCode());
                    if(e.getCode() == KeyCode.SPACE){
                        System.out.println("2");
                        startGame();
                    }
                }
            }
        });
    }
    */
}
