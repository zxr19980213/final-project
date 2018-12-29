package com.nju.zxr;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public enum CreaturePicture {
    Calabash1("/hulu1.png"),
    Calabash2("/hulu2.png"),
    Calabash3("/hulu3.png"),
    Calabash4("/hulu4.png"),
    Calabash5("/hulu5.png"),
    Calabash6("/hulu6.png"),
    Calabash7("/hulu7.png"),
    GrandPa("/grandpa.png"),
    Scorpion("/scorp.png"),
    Snake("/snake.png"),
    Monster1("/monster.png"),
    Monster2("/monster.png"),
    Monster3("/monster.png"),
    Monster4("/monster.png"),
    Monster5("/monster.png");

    CreaturePicture(String filePath){
        Image image = new Image(filePath);
        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
    }
    public ImageView imageView;
}
