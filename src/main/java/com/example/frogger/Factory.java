//@Author Matt Dobaj
package com.example.frogger;
import java.util.ArrayList;

public class Factory {
    ArrayList<Log> logList = new ArrayList<>();
    ArrayList<Car> carList = new ArrayList<>();
    private final double LOG_SPAWNRATE = 0.01;
    private final double CAR_SPAWNRATE = 0.01;
    private final double LOG_X_VELOCITY = 0.0167;
    private final double CAR_X_VELOCITY = 0.032;
    private final int GAME_WIDTH;
    private final int GAME_HEIGHT;
    private double speedMultiplier = 1;
    Environment environment;
    View gameView;

    /**
     * Creates an obstacle factory object and passes through
     * the game's core environment and renderer objects.
     *
     * @param environment the game's environment
     * @param gameView the game's renderer
     */
    Factory(Environment environment, View gameView) {
        this.environment = environment;
        this.gameView = gameView;
        this.GAME_WIDTH = gameView.GAME_WIDTH;
        this.GAME_HEIGHT = gameView.GAME_HEIGHT;
        populate();
    }

    /**
     * Checks if any log or car entities have left the screen and if so, remove them.
     */
    public void removeDeadEntities() {
        for (int i = 0; i < logList.size(); i++) {
            if (!logList.get(i).isOnScreen()) {
                logList.remove(i);
                i--; //Because element at index is now the next element
            }
        }
        for (int i = 0; i < carList.size(); i++) {
            if (!carList.get(i).isOnScreen()) {
                carList.remove(i);
                i--; //Because element at index is now the next element
            }
        }
    }

    /**
     * Builds logs by first finding the rows where logs can spawn in environment (water),
     * and then checks if they intersect with any current logs. The chance a
     * log will spawn in a given row is dictated by LOG_SPAWNRATE. If the y
     * position is a multiple of 2, they will have a positive velocity, otherwise,
     * a negative one.
     */
    public void buildLog() {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            if (environment.isWater(i)) {
                if (Math.random() < LOG_SPAWNRATE) {
                    if (i % 2 == 0 && !doesLogIntersect(-3, i, 3)) { //check 0 to account for width of log
                        logList.add(new Log(-3, i,LOG_X_VELOCITY, speedMultiplier));}
                    else if (!doesLogIntersect(GAME_WIDTH, i, 3) && i % 2 != 0){
                        logList.add(new Log(GAME_WIDTH, i, -LOG_X_VELOCITY, speedMultiplier));
                    }
                }
            }

        }
    }

    /**
     * Goes through all log objects and makes sure none of them
     * intersect with a given position. There is a buffer region
     * defined by the gap param. Calls upon the objects' doesIntersect().
     * @param x the x position to check.
     * @param y the y position to check.
     * @param gap The maximum distance away the position can be from
     *            a log and still be considered an intersection.
     * @return true if a log is found within the gap surrounding the position.
     */
    public boolean doesLogIntersect(float x, float y, float gap) {
        for (Log l : logList) {
                if (l.doesIntersect(x,y, 3, gap)) {
                    return true;}
        }
        return false;
    }

    /**
     * Goes through all car objects and makes sure none of them
     * intersect with a given x and y position. Provides lee-way
     * with the gap param. Calls upon the objects' doesIntersect().
     * @param x the x coordinate to check
     * @param y the y coordinate to check
     * @param gap The maximum distance away the position can be from
     *            a car to still be considered an intersection.
     * @return true if any car is found within the gap surrounding the position.
     */
    public boolean doesCarIntersect(float x, float y, float gap) {
        for (Car c: carList) {
            if (c.doesIntersect(x,y, 1, gap)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds cars by first finding the rows where cars can spawn in the environment (road),
     * and then checks if they intersect with any current cars. There is a random
     * chance a car will spawn in a given row, dictated by CAR_SPAWNRATE.
     * Spawnrate increases with speedMultiplier.
     */
    public void buildCar() {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            if (environment.isRoad(i)) {
                if (Math.random() < CAR_SPAWNRATE*speedMultiplier) {
                    if (!doesCarIntersect(16, i, 1)){
                        carList.add(new Car(16, i, -CAR_X_VELOCITY, speedMultiplier));
                    }
                }
            }
        }
    }

    //Returns the logs X Velocity at a given row.
    //Used for frog movement
    public double getLogXVelocity(int y) {
        if (y % 2 == 0) {
        return LOG_X_VELOCITY*speedMultiplier;}
        else {
            return -LOG_X_VELOCITY*speedMultiplier;
        }
    }

    /**Increases the speed multiplier which affects the speed of both the logs and cars.
     * The multiplier also affects the cars' spawnrate to increase difficulty.
     * Alongside increasing the global speed multiplier, it also does it for the individual obstacles.
     * @param deltaMultiplier the amount to increase the speed multiplier by.
     */

    public void increaseSpeedMultiplier(double deltaMultiplier) {
        speedMultiplier += deltaMultiplier;
        for(Log l: logList) {
            l.setSpeedMultiplier(speedMultiplier);
        }
        for(Car c: carList) {
            c.setSpeedMultiplier(speedMultiplier);
        }
    }

    /**
     * Rather than increasing the speed multiplier, it explicitly sets the multiplier.
     * This effects log and car speed along with car spawn rate.
     * Alongside setting the global speed multiplier, it sets it for each individual obstacle.
     * @param multiplier the new speed multiplier
     */
    public void setSpeedMultiplier(double multiplier) {
        speedMultiplier = multiplier;
        for(Log l: logList) {
            l.setSpeedMultiplier(multiplier);
        }
        for(Car c: carList) {
            c.setSpeedMultiplier(multiplier);
        }
    }

    /**Emulates running the game through 600 frames of gameplay
     * in order to have obstacles populated throughout the screen of the game.
     * Moves each obstacle and generates new ones.
     * Very similar to update(), except there is no need for garbage collection
     * at this stage of the game, and no need to draw the obstacles either.
     */
    public void populate() {
        for (int i = 0; i < 600; i++) {
            for(Log l: logList) {
                l.move();}
            for(Car c: carList) {
                c.move();}
            buildLog();
            buildCar();
        }
    }

    /**
     * Removes all car and log objects in logList and carList respectively. Used to clear the screen of obstacles.
     */
    public void removeAllEntities() {
        logList.clear();
        carList.clear();
    }

    /**
     * Moves each obstacle and generates new ones on either side of the screen.
     * Calls gameView to draw the obstacles on screen.
     * Also performs some garbage collection by checking if the obstacles are off-screen
     * and deleting them if so.
     */

    public void update() {
        removeDeadEntities();
        for(Log l: logList) {
            l.move();
            gameView.drawLog(l.getX(), l.getY());
        }
        for (Car c: carList) {
            c.move();
            gameView.drawCar(c.getX(), c.getY(), c.getVariant());
        }
        buildLog();
        buildCar();
    }
}
