/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 *
 * @author Maciek
 */
public class PongFX extends Application {

    Timeline timer;
    int screenH = 800;
    int screenW = 900;
    double ballR = 12;
    double batW = 15;
    double batH = 100;
    double lineW = 2;
    int MAXSCORE=3;

    boolean running = true;
    Ball ball;

    @Override
    public void start(Stage primaryStage) {

        MediaPlayer bounce = new MediaPlayer(new Media(new File("Sounds/bounce.wav").toURI().toString()));
        MediaPlayer pong = new MediaPlayer(new Media(new File("Sounds/ping.wav").toURI().toString()));
        MediaPlayer win = new MediaPlayer(new Media(new File("Sounds/win.wav").toURI().toString()));
        MediaPlayer lose = new MediaPlayer(new Media(new File("Sounds/lose.wav").toURI().toString()));

        ball = new Ball(ballR, screenW, screenH);

        Ball bf1 = new Ball(ballR - 2, screenW, screenH);
        bf1.setFill(Color.rgb(255, 255, 255, 0.5));

        Ball bf2 = new Ball(ballR - 4, screenW, screenH);
        bf2.setFill(Color.rgb(255, 255, 255, 0.2));

        Bat playerBat = new Bat(batW, batH, 0, screenH / 2 - batH / 2, false);
        Text pScore = new Text(Integer.toString(playerBat.score));
        pScore.setFill(Color.WHITE);
        pScore.setTranslateX(screenW / 2 - 100);
        pScore.setTranslateY(60);
        pScore.setFont(new Font(50));
        Bat CPUBat = new Bat(batW, batH, screenW - batW, screenH / 2 - batH / 2, true);
        Text cScore = new Text(Integer.toString(CPUBat.score));
        cScore.setFill(Color.WHITE);
        cScore.setTranslateX(screenW / 2 + 75);
        cScore.setTranslateY(60);
        cScore.setFont(new Font(50));

        Rectangle halfLine = new Rectangle(lineW, screenH);
        halfLine.setFill(Color.WHITE);
        halfLine.setTranslateX(screenW / 2 - lineW / 2);
        halfLine.setTranslateY(0);
        
        Text score=new Text();
        score.setFont(new Font(70));
        score.setTranslateX(screenW / 2 - 190);
        score.setTranslateY(screenH /2 - 70);
       

        KeyFrame frame = new KeyFrame(Duration.millis(16), event -> {
            if (!running) {
                return;
            }

            switch (playerBat.action) {
                case UP:
                    if (playerBat.getTranslateY() > 0) {
                        playerBat.setTranslateY(playerBat.getTranslateY() - playerBat.velocity);
                    }
                    break;
                case DOWN:
                    if (playerBat.getTranslateY() + batH < screenH) {
                        playerBat.setTranslateY(playerBat.getTranslateY() + playerBat.velocity);
                    }
                    break;
                case NONE:
                    break;
            }

            CPUBat.makeAction(ball);

            switch (CPUBat.action) {
                case UP:
                    if (CPUBat.getTranslateY() > 0) {
                        CPUBat.setTranslateY(CPUBat.getTranslateY() - CPUBat.velocity);
                    }
                    break;
                case DOWN:
                    if (CPUBat.getTranslateY() + batH < screenH) {
                        CPUBat.setTranslateY(CPUBat.getTranslateY() + CPUBat.velocity);
                    }
                    break;
                case NONE:
                    break;
            }

            ball.setTranslateX(ball.getTranslateX() + (ball.goingLeft ? -ball.velocityX : ball.velocityX));
            ball.setTranslateY(ball.getTranslateY() + (ball.goingUp ? -ball.velocityY : ball.velocityY));
            if (ball.goingLeft && ball.goingUp) {
                for (int i=0; i<ball.fade.length;i++) {
                    ball.fade[i].setTranslateX(ball.getTranslateX() + (i*0.5) * ball.velocityX);
                    ball.fade[i].setTranslateY(ball.getTranslateY() + (i*0.5) * ball.velocityY);
                }
            } else if (!ball.goingLeft && ball.goingUp) {
                for (int i=0; i<ball.fade.length;i++) {
                    ball.fade[i].setTranslateX(ball.getTranslateX() - (i*0.5) * ball.velocityX);
                    ball.fade[i].setTranslateY(ball.getTranslateY() + (i*0.5) * ball.velocityY);
                }
            } else if ((ball.goingLeft && !ball.goingUp)) {
                for (int i=0; i<ball.fade.length;i++) {
                    ball.fade[i].setTranslateX(ball.getTranslateX() + (i*0.5) * ball.velocityX);
                    ball.fade[i].setTranslateY(ball.getTranslateY() - (i*0.5) * ball.velocityY);
                }

            } else {
                for (int i=0; i<ball.fade.length;i++) {
                    ball.fade[i].setTranslateX(ball.getTranslateX() - (i*0.5) * ball.velocityX);
                    ball.fade[i].setTranslateY(ball.getTranslateY() - (i*0.5) * ball.velocityY);
                }
            }

            if (ball.isBouncing()) {
                ball.goingUp = !ball.goingUp;
                bounce.play();
                bounce.seek(Duration.ZERO);
            }

            if (playerBat.isHitting(ball)) {
                ball.goingLeft = false;
                switch (playerBat.action) {
                    case UP:
                        ball.goingUp = true;
                        break;
                    case DOWN:
                        ball.goingUp = false;
                        break;
                    case NONE:
                        break;
                }
                ball.velocityX++;
                pong.play();
                pong.seek(Duration.ZERO);
            } else if (CPUBat.isHitting(ball)) {
                ball.goingLeft = true;
                switch (CPUBat.action) {
                    case UP:
                        ball.goingUp = true;
                        ball.velocityY++;
                        break;
                    case DOWN:
                        ball.goingUp = false;
                        ball.velocityY++;
                        break;
                    case NONE:
                        break;
                }
                ball.velocityX++;
                pong.play();
                pong.seek(Duration.ZERO);
            }

            if (ball.getTranslateX() < 0) {
                CPUBat.score++;
                cScore.setText(Integer.toString(CPUBat.score));
                lose.play();
                lose.seek(Duration.ZERO);
                restart();
            } else if (ball.getTranslateX() > screenW) {
                playerBat.score++;
                pScore.setText(Integer.toString(playerBat.score));
                win.play();
                win.seek(Duration.ZERO);
                restart();
            }
            
            if(playerBat.score==MAXSCORE){
                score.setText("WYGRAŁEŚ");
                score.setFill(Color.GREEN);
                timer.stop();
            }else if(CPUBat.score==MAXSCORE){
                score.setText("GAME OVER");
                score.setFill(Color.RED);
                timer.stop();
            }
        });

        timer = new Timeline();
        timer.getKeyFrames().add(frame);
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.setDelay(Duration.seconds(2));
        timer.play();

        Pane root = new Pane();
        root.getChildren().addAll(ball, playerBat, CPUBat, halfLine, pScore, cScore, score);
        for (Circle c : ball.fade) {
            root.getChildren().add(c);
        }

        Scene scene = new Scene(root, screenW, screenH);
        scene.setFill(Color.BLACK);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    playerBat.goUp();
                    break;
                case S:
                    playerBat.goDown();
                    break;
            }
        });
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case W:
                    playerBat.stop();
                    break;
                case S:
                    playerBat.stop();
                    break;
            }
        });

        primaryStage.setTitle("PONG");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void restart() {
        running = false;
        timer.stop();
        timer.setDelay(Duration.seconds(1));
        timer.play();
        ball.reset();
        running = true;
    }

}
