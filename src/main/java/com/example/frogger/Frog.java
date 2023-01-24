//@Author Matt Dobaj
package com.example.frogger;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.File;

public class Frog implements Entity{
    private double x; //the x position of the frog relative to gamewidth
    private double y; //the y position of the frog relative to gameheight
    private final int GAME_WIDTH;
    private final int GAME_HEIGHT;
    private Boolean isAlive = true;
    Image sprite = new Image("file:src"+ File.separator + "main" + File.separator + "resources" + File.separator + "com" + File.separator + "example" + File.separator + "froggerfinal" + File.separator + "frog.png");
    ImageView spriteView = new ImageView(sprite); //Used for manipulating the rotation of the sprite
    SnapshotParameters params = new SnapshotParameters(); //Used for converting imageView to image

    /**
     * Creates a new frog object at a random position at the bottom of the screen.
     * Also sets the params used for imageView.
     * @param gameWidth the width of the game; the frog cannot move past this part of the level
     * @param gameHeight the height of the game; the frog cannot move past this part of the level
     */
    Frog (int gameWidth, int gameHeight) {
        this.GAME_HEIGHT = gameHeight;
        this.GAME_WIDTH = gameWidth;
        params.setFill(Color.TRANSPARENT);
        resetFrogPos();
    }

    @Override
    public void move() {}

    /**
     * Moves the frog by the given values, rounding its position afterwards to the nearest whole number.
     * @param x the distance to move the frog in the x direction
     * @param y the distance to move the frog in the y direction
     */
    public void move(int x, int y) {
        if (this.x+x < GAME_WIDTH && this.x+x >= 0) {
        this.x = Math.floor(this.x+x);}
        if (this.y+y < GAME_HEIGHT) {
        this.y = Math.floor(this.y+y);}
    }

    /**
     * Moves the frog only in the x direction by the given value, this time not rounding the number.
     * This is used when the frog is on top of log
     * @param x the distance to move the frog in the x direction.
     */
    public void move(double x) {
        this.x += x;
    }

    @Override
    public float getX() {
        return (float)x;
    }

    public void setY(double y) {this.y = y;};

    @Override
    public float getY() {
        return (float)y;
    }

    //Returns whether the frog is on screen
    @Override
    public boolean isOnScreen() {
        return x >= 0 && x <= GAME_WIDTH;
    }

    //Sets the frog alive or not, if setting the frog to alive, reset its position.
    public void setAlive(boolean state) {
        isAlive = state;
        if (state) {
            resetFrogPos();
        }
    }

    //returns the state of the frog
    public boolean getAliveState() {
        return isAlive;
    }

    //Rotates the imageView with the sprite to the given angle.
    public void setRotation(int rotation) {
        spriteView.setRotate(rotation);
    }

    //Converts the imageView back into an image and returns it.
    //This way the sprite gets rotated to the desired direction.
    public Image getSprite() {
        return spriteView.snapshot(params, null);
    }

    //Moves the frog to a random position at the bottom of the screen.
    public void resetFrogPos() {
        x = Math.round(Math.random()*(GAME_WIDTH-1));
        y = GAME_HEIGHT-1;
    }
}
