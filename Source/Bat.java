/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Maciek
 */
public class Bat extends Rectangle {

    void makeAction(Ball b) {

        if(b.getTranslateY()<getTranslateY()+getWidth()/2){
            action=Actions.UP;
        }else if(b.getTranslateY()>getTranslateY()+getWidth()/2){
            action=Actions.DOWN;
        }else{
            action=Actions.NONE;
        }
    }

    enum Actions {
        NONE, UP, DOWN
    }

    public Actions action;
    public double velocity;
    public int score;
    boolean isCPU;

    public Bat(double w, double h, double x, double y, boolean c) {
        super(w, h);
        this.setFill(Color.WHITE);
        this.setTranslateX(x);
        this.setTranslateY(y);
        action = Actions.NONE;
        score = 0;
        isCPU=c;
        velocity = 6;
        
    }
    
    boolean isHitting(Ball b){
        double x=b.getTranslateX();
        double y=b.getTranslateY();
        double r=b.getRadius();
        if(!isCPU){
            return (y+r>=getTranslateY() && y-r<=getTranslateY()+getHeight() && x-r<getWidth());
        }else{
            return (y+r>=getTranslateY() && y-r<=getTranslateY()+getHeight() && x+r>getTranslateX()-getWidth());
        }
    }

    void goUp() {
        action = Actions.UP;
    }

    void goDown() {
        action = Actions.DOWN;
    }

    void stop() {
        action = Actions.NONE;
    }
}
