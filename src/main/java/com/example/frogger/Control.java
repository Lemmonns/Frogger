//@Author Matt Dobaj
package com.example.frogger;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class Control {
    Scene scene;
    private boolean processKeyHalt = false; //Used to prevent holding down the movement buttons
    private boolean haltFrogControl = false; //Used to prevent the frog from moving during the end screen

    //Has the scene in order to add an event handler
    Control(Scene scene) {this.scene = scene;}

    /**
     * Instantiates a new EventHandler that checks for keyboard input.
     * Checks the arrow keys and sets the delta position for the frog appropriately.
     * Sets the frog's rotation used for manipulating the sprite.
     * Only processes frog input if the processKeyHalt and haltFrogControl halts are both false.
     * dontProcessKey is set true after every input, and is only set false if a key is released.
     * Also checks if the space key is pressed for the purpose of exiting the death screen and resetting the frog.
     * @param frog the frog that is being controlled
     */
    public void frogControlInstantiate(Frog frog) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent key) -> {
            int deltaX = 0;
            int deltaY = 0;
            switch(key.getCode()) {
                case UP:
                    deltaY = -1;
                    frog.setRotation(0);
                    break;
                case DOWN:
                    deltaY = 1;
                    frog.setRotation(180);
                    break;
                case LEFT:
                    deltaX = -1;
                    frog.setRotation(270);
                    break;
                case RIGHT:
                    deltaX = 1;
                    frog.setRotation(90);
                    break;
                case SPACE:
                    exitDeathScreen(frog);
            }
            if (!processKeyHalt && !haltFrogControl) {
                frog.move(deltaX,deltaY);
                processKeyHalt = true;
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent key) -> {processKeyHalt = false;});
    }

    /**
     * Exits the deathScreen and revives the frog. This resets the position of the frog.
     * Also removes the halt preventing the frog from being controlled during the death screen.
     * @param frog the frog that gets revived.
     */
    public void exitDeathScreen(Frog frog) {
        if (frog.getAliveState()) {return;}
        frog.setAlive(true);
        setHaltFrogControl(false);
    }

    /**
     * Sets the halt on the frog which prevents it from being controlled.
     * @param newValue the new state of the halt.
     */
    public void setHaltFrogControl(boolean newValue) {
        haltFrogControl = newValue;
    }

}
