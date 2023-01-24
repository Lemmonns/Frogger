//@Author Matt Dobaj
package com.example.frogger;

public abstract class Obstacle implements Entity {
    private float x;
    private float y;
    private final double X_VELOCITY;
    private double speedMultiplier;

    /**
     * Creates the obstacle objects with a fixed velocity.
     *
     * @param x the initial x position of the obstacle
     * @param y the initial y position of the obstacle
     * @param xVelocity the velocity of the obstacle. Since
     *                  they only move in x-axis, velocity is
     *                  also only in x-axis. Is a constant
     * @param speedMultiplier the multiplier for the xVelocity
     */
    Obstacle(float x, float y, double xVelocity, double speedMultiplier) {
        this.x = x;
        this.y = y;
        this.X_VELOCITY = xVelocity;
        this.speedMultiplier = speedMultiplier;
    }

    //returns the x pos of the obstacle
    @Override
    public float getX() {
        return x;
    }

    //returns the y pos of the obstacle
    @Override
    public float getY() {
        return y;
    }

    /**
     * moves the obstacle by its defined velocity.
     * limited movement to the x-axis.
     */
    @Override
    public void move() {
        x += X_VELOCITY*speedMultiplier;
        }

    /**
     * Determines whether the obstacle is still visible on screen.
     * Has a buffer to account for log width.
     *
     * @return false if the obstacle should be despawned,
     *         true if the obstacle is still visible
     */
    @Override
    public boolean isOnScreen() {return (x > -4 && x < 17);}

    /**
     * Checks if a given position is within a certain gap
     * from the obstacle.
     *
     * @param x The x coordinate to check against the obstacle pos.
     * @param y The y coordinate to check against the obstacle pos.
     * @param gap The distance from the obstacle where a position is
     *            still considered intersecting.
     * @return whether the given pos intersects with obstacle.
     */
    public boolean doesIntersect(float x, float y, int width, float gap) {
        return (this.y == y && ((this.x - gap) <= x && (this.x + width + gap) >= x));
    }

    public void setSpeedMultiplier(double x) {
        speedMultiplier = x;
    }
}
