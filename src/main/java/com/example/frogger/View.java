//@Author Matt Dobaj
package com.example.frogger;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.File;


public class View {
    final int GAME_HEIGHT;
    final int GAME_WIDTH;
    int scaleFactor;
    Scene scene;
    Canvas canvas;
    GraphicsContext gc;
    Environment environment;
    Image[] car = new Image[7];
    Image log = new Image("file:src"+ File.separator + "main" + File.separator + "resources" + File.separator + "com" + File.separator + "example" + File.separator + "froggerfinal" + File.separator + "log.png");
    String fontFilePath = "file:src"+ File.separator + "main" + File.separator + "resources" + File.separator + "com" + File.separator + "example" + File.separator + "froggerfinal" + File.separator + "Retro Gaming.ttf";
    int levelViewFrameHold = 120; //The duration of the new level pop-up

    /**
     * Creates a new View object, and passes through the game's core elements.
     * @param canvas The game's canvas for the graphics context.
     * @param scene The game's window.
     * @param environment The game environment.
     */
    View(Canvas canvas, Scene scene, Environment environment, int GAME_HEIGHT, int GAME_WIDTH) {
        this.scene = scene;
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.environment = environment;
        this.GAME_HEIGHT = GAME_HEIGHT;
        this.GAME_WIDTH = GAME_WIDTH;
        this.scaleFactor = (int)(canvas.getHeight()/GAME_HEIGHT);
        initializeCarImages();
    }

    //Instantiates all the variants of car sprites and puts them in an array
    public void initializeCarImages() {
        for (int i = 0; i < car.length; i++) {
            car[i] = new Image("file:src"+ File.separator + "main" + File.separator + "resources" + File.separator + "com" + File.separator + "example" + File.separator + "froggerfinal" + File.separator + "car"+i+".png");
        }
    }

    /**
     * Loops through the rows of the environment and appropriately colors the rows depending on whether they are water,
     * road, or otherwise, where we will assume it is grass.
     * If the row is a road, then markers are also drawn on the road at an offset from each other.
     */
    public void drawEnvironment() {
        for(int i = 0; i < GAME_HEIGHT; i++) {
            if (environment.isWater(i)){
                gc.setFill(Color.BLUE);
                gc.fillRect(0, scaleFactor*i, scene.getWidth(), scaleFactor);
            }
            else if (environment.isRoad(i)){
                gc.setFill(Color.rgb(70,73,70));
                gc.fillRect(0, scaleFactor*i, scene.getWidth(), scaleFactor);
                gc.setFill(Color.WHITE);
                double roadOffset = (i*0.25);
                for (int j = 0; j < GAME_WIDTH+roadOffset; j+=1.5) {
                    gc.fillRect((j+0.25-roadOffset)*scaleFactor, (i+0.375)*scaleFactor, (float) scaleFactor/2, (float) scaleFactor/4);
                }
            }
            else {
                gc.setFill(Color.GREEN);
                gc.fillRect(0, scaleFactor*i, scene.getWidth(), scaleFactor);
            }
        }
    }

    //Renders the log sprite at the given position
    public void drawLog(float x, float y) {
        gc.drawImage(log, (x* scaleFactor), (y* scaleFactor), scaleFactor *3, scaleFactor);
    }
    //Renders the given frog sprite at the given position
    public void drawFrog(float x,float y, Image sprite) {
        gc.drawImage(sprite, (int) (x* scaleFactor), (int) (y* scaleFactor), scaleFactor, scaleFactor);
    }
    //Renders the car sprite, dependent on the given variant of the car, at the given position
    public void drawCar(float x, float y, int carVariant) {
        gc.drawImage(car[carVariant], (x* scaleFactor), (y* scaleFactor), scaleFactor, scaleFactor);

    }
    //Displays the death screen in the center of the screen
    public void drawDeathText() {
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.loadFont(fontFilePath, scaleFactor));
        gc.fillText("YOU DIED", canvas.getWidth()/2, canvas.getHeight()/2);
        gc.setFont(Font.loadFont(fontFilePath, scaleFactor *0.75));
        gc.fillText("Press Space to Replay", canvas.getWidth()/2, canvas.getHeight()/2+ scaleFactor);
    }

    //Flashes the given level on the screen for 120 frames.
    public void viewLevelText(int level) {
        if (levelViewFrameHold >= 120) {return;}
            gc.setFill(Color.BLACK);
            gc.setFont(Font.loadFont(fontFilePath, scaleFactor *2));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            gc.fillText("Level: " + level, canvas.getWidth()/2, canvas.getHeight()/2);
            gc.strokeText("Level: " + level, canvas.getWidth()/2,canvas.getHeight()/2);
            levelViewFrameHold++;
    }

    //Resets the levelViewFrameHold, which allows viewLevelText to display the level.
    public void showLevelText() {
        levelViewFrameHold = 0;
    }

    //Displays the current score and level in the top left and top right of the screen respectively
    public void viewUI(String score, String level) {
        gc.setFill(Color.BLACK);
        gc.setFont(Font.loadFont(fontFilePath, scaleFactor *0.75));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("SCORE: " + score, (float) scaleFactor /5, scaleFactor *0.8);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("LEVEL: " + level, canvas.getWidth()-(float) scaleFactor /5, scaleFactor *0.8);

    }

    /**
     * Adjusts the global scaleFactor of the View object in the case of a window resize.
     * First calculates each dimension's scaleFactor  and sets the global scaleFactor to the smaller one.
     * If the x-axis is bigger than the y-axis relative to the game's size, then the y-axis is chosen and vice versa.
     * If the y-axis' scaleFactor is chosen, the canvas' width is cropped in order to hide off-screen elements.
     */
    public void adjustScaling() {
        canvas.setHeight(scene.getHeight());
        canvas.setWidth(scene.getWidth());
        int scaleFactorX = (int)scene.getWidth()/GAME_WIDTH;
        int scaleFactorY = (int)scene.getHeight()/GAME_HEIGHT;
        scaleFactor = Math.min(scaleFactorX, scaleFactorY);
        canvas.setWidth(GAME_WIDTH* scaleFactor);
    }

    //Clears the scene
    public void refreshView() {
        gc.clearRect(0,0, scene.getWidth(), scene.getHeight());
    }
}
