/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Maciek
 */
public class Ball extends Circle {

    Random rand=new Random();
    double velocityX;
    double velocityY;
    boolean goingUp;
    boolean goingLeft;
    int W;
    int H;
    Circle fade[]=new Circle[10];
    
    public Ball(double r,int w, int h) {
        super(r);
        this.setFill(Color.WHITE);
        W=w;
        H=h;
        reset();
        for(int i=0;i<fade.length;i++){
            fade[i]=new Circle(r-i);
            fade[i].setFill(Color.rgb(255, 255, 255, 1-i*0.1));
            fade[i].setTranslateX(W/2);
            fade[i].setTranslateY(H/2);
        }
    }
    void reset(){
        setTranslateX(W/2);
        setTranslateY(H/2);
        goingUp=rand.nextBoolean();
        goingLeft=rand.nextBoolean();
        velocityY=0;
        velocityX=3;
    }
    public boolean isBouncing(){
        return getTranslateY()-getRadius()<=0 || getTranslateY()+getRadius()>=H;
    }
    
}
