//@Author Matt Dobaj
package com.example.frogger;

public class Car extends Obstacle {
    private int variant; //the variant, causes a different coloured sprite to be displayed

    /**
     * Creates a new Car object by calling the super Obstacle constructor.
     * @param x the initial x pos of the Car.
     * @param y the initial y pos of the Car.
     * @param xVelocity the velocity in the x direction of the Car.
     * @param speedMultiplier the amount to multiply the velocity by during movement.
     * @param variant one of the 7 variants the Car can be. Expects an input between (0-6).
     */
    Car(float x,float y, double xVelocity, double speedMultiplier, int variant) {
        super(x,y,xVelocity,speedMultiplier);
        this.variant = variant;
    }

    /**
     * Creates a new Car object by calling the super Obstacle constructor.
     * Creates a random variant (0-6) rather than setting one.
     * @param x the initial x pos of the Car.
     * @param y the initial y pos of the Car.
     * @param xVelocity the x velocity of the Car.
     * @param speedMultiplier the amount to multiply the velocity by during movement.
     */

    Car(float x, float y, double xVelocity, double speedMultiplier) {
        super(x,y,xVelocity,speedMultiplier);
        variant = (int) Math.round(Math.random()*6);
    }

    //returns the variant of the Car for display purposes.
    public int getVariant() {
        return variant;
    }

}
