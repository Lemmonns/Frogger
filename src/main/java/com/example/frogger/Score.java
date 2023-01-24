package com.example.frogger;

public class Score {
    int score;
    double scoreMultiplier = 1;
    int maxYPosition; //used to find score

    Score(int maxYPosition) {
        this.maxYPosition = maxYPosition;
    }

    //Increases the score by 100 times the scoreMultiplier.
    public void increaseScore() {
        score+= 100*scoreMultiplier;
    }

    //Sets the score to the given value.
    public void setScore(int score) {
        this.score = score;
    }

    //Multiplies the scoreMultiplier by the given value.
    public void increaseScoreMultiplier(double deltaMultiplier) {
        scoreMultiplier*=deltaMultiplier;
    }

    //Sets the scoreMultiplier to the given value.
    public void setScoreMultiplier(double multiplier) {
        scoreMultiplier = multiplier;
    }

    /**
     * Checks whether the frog's yPos has crossed the maximum Y position that it has reached this level.
     * If it has, the new maximum Y position is the frog's yPos and the score is incremented.
     * This is to prevent the frog from farming points by moving back and forth.
     * @param yPos the y position of the frog.
     */
    public void updateScore(int yPos) {
        if (yPos < maxYPosition) {
            maxYPosition = yPos;
            increaseScore();
        }
    }

    //Sets the maximum y position of the frog this level.
    public void setMaxYPos(int y) {
        maxYPosition = y;
    }

    //Returns the score in string format
    public String toString() {
        return Integer.toString(score);
    }
}
