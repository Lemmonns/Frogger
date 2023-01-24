//@Author Matt Dobaj
package com.example.frogger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application {
    private final int VIEW_HEIGHT = 480; //The initial height of the viewport
    private final int VIEW_WIDTH = 720; //The initial width of the viewport
    final int GAME_WIDTH = 15;
    final int GAME_HEIGHT = 10;
    int level = 1;
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setTitle("Not Frogger");
        stage.setScene(scene);

        //Initialize the canvas, or the games drawing area
        Canvas canvas = new Canvas(VIEW_WIDTH, VIEW_HEIGHT);
        root.getChildren().add(canvas);
        //Initializes the gameEnvironment, responsible for determine what rows are grass, road, or water.
        Environment gameEnvironment = new Environment(GAME_HEIGHT);
        //Initialize gameView, the game's rendering class.
        View gameView = new View(canvas, scene, gameEnvironment, GAME_HEIGHT, GAME_WIDTH);
        //Initialize the obstacle factory
        Factory obstacleFactory = new Factory(gameEnvironment, gameView);
        //Initialize the player
        Frog frogger = new Frog(GAME_WIDTH, GAME_HEIGHT);
        //Initialize the player controls
        Control control = new Control(scene);
        control.frogControlInstantiate(frogger);
        //Initializes the score counter
        Score score = new Score(GAME_HEIGHT-1);

        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame frames = new KeyFrame(
                Duration.seconds(0.01667), //60 FPS
                new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event){
                        gameView.refreshView();
                        if (frogger.getAliveState()) {
                            if (didFrogWin(frogger)) {
                                frogger.setY(GAME_HEIGHT-1);
                                increaseLevel(obstacleFactory, gameView, score, gameEnvironment);
                            }
                            gameView.adjustScaling();
                            gameView.drawEnvironment();
                            obstacleFactory.update();
                            gameView.drawFrog(frogger.getX(), frogger.getY(), frogger.getSprite());
                            frogIntersectLogic(frogger, obstacleFactory, gameEnvironment);
                            score.updateScore((int)frogger.getY());
                            gameView.viewLevelText(level);
                            gameView.viewUI(score.toString(), Integer.toString(level));
                        } else {
                            gameView.drawDeathText();
                            control.setHaltFrogControl(true);
                            resetLevel(obstacleFactory, score,gameEnvironment);
                        }
                    }
                }
        );
        gameLoop.getKeyFrames().add(frames);
        gameLoop.play();
        stage.show();
    }

    /**
     * Checks if the frog intersects or does not intersect with any of the obstacles within a gap of 0.25 from its center.
     * Also checks if the frog is on screen or not.
     * The frog's x position is skewed to see if the collisions occur relative to the middle of its body.
     * If the frog is on top of a log, then it moves at the log velocity.
     * If the frog collides with a car, or moves off-screen, or is in the water but not on top of a log, it dies.
     * @param frog the frog object.
     * @param obstacleFactory the obstacle factory containing the obstacles.
     * @param gameEnvironment the game's environment object. Used to determine what type of terrain the frog is on.
     */
    public void frogIntersectLogic(Frog frog, Factory obstacleFactory, Environment gameEnvironment) {
        float frogX = (float) (frog.getX()+0.5); //used to get the middle of the frog
        float frogY = frog.getY();
        //first checks if frog is on a log, if yes it moves the frog by the log velocity
        if (obstacleFactory.doesLogIntersect(frogX, frogY, (float) 0.25)) {
            frog.move(obstacleFactory.getLogXVelocity((int)frogY));
        }
        if (obstacleFactory.doesCarIntersect(frogX, frogY, (float)0.4) || !frog.isOnScreen() || (gameEnvironment.isWater((int)frogY) && !obstacleFactory.doesLogIntersect(frogX, frogY, (float)0.5))) {
            frog.setAlive(false);
        }
    }

    //Returns whether the frog has reached the top of the level.
    public boolean didFrogWin(Frog frog) {
        return frog.getY()==0;
    }

    /**
     * Increments the level by one and also increases the speedMultiplier for all the obstacles.
     * This tells the environment to randomize.
     * Also deletes all the obstacle entries and calls factory.populate() to spawn them back in the new environment.
     * This increases the spawnrate of the car obstacles.
     * Increases the score multiplier.
     * Resets the score's maxYPos.
     * @param factory the obstacle factory containing the obstacles.
     * @param gameView the game's renderer used to flash the new level text.
     * @param score the score counter.
     * @param environment the game's environment object. Told to randomize.
     */
    public void increaseLevel(Factory factory, View gameView, Score score, Environment environment) {
        environment.randomizeRows();
        factory.removeAllEntities();
        factory.populate();
        factory.increaseSpeedMultiplier(0.5);
        score.increaseScoreMultiplier(2);
        score.setMaxYPos(GAME_HEIGHT-1);
        gameView.showLevelText();
        level++;
    }

    /**
     * Resets the level back to 1.
     * In doing so, it also resets the environment to its default pattern.
     * Also deletes all the obstacle entries and calls factory.populate() to spawn them back.
     * Also resets the obstacles' speed multiplier and the score multiplier back to 1.
     * Also resets the score and the score's maxYPos.
     * @param factory the obstacle factory containing the obstacles.
     * @param score the score counter.
     * @param environment the game's environment object.
     */
    public void resetLevel(Factory factory, Score score, Environment environment) {
        environment.populateRowsDefault();
        factory.removeAllEntities();
        factory.populate();
        factory.setSpeedMultiplier(1);
        score.setScoreMultiplier(1);
        score.setScore(0);
        score.setMaxYPos(GAME_HEIGHT-1);
        level = 1;
    }

    public static void main(String[] args) {
        launch();
    }
}