//@Author Matt Dobaj
package com.example.frogger;

public class Environment {
    final int GAME_HEIGHT;
    private enum levelRows { WATER, ROAD, GRASS}
    private levelRows[] rows;

    /**
     * Creates a new environment with the appropriate amount of rows and populates it
     * @param gameHeight the height of the game or the amount of rows in the environment
     */
    public Environment(int gameHeight) {
       this.GAME_HEIGHT = gameHeight;
       rows = new levelRows[GAME_HEIGHT];
       populateRowsDefault();
    }

    //populates the rows with the levelRows types grass, road, or water in their default pattern.
    public void populateRowsDefault() {
        for(int i =0; i < rows.length; i++) {
            rows[i]= levelRows.GRASS;
        }
        for(int i = 1; i < 5; i++) {
            rows[i] = levelRows.WATER;
        }
        for(int i = 6; i < 9; i++) {
            rows[i] = levelRows.ROAD;
        }
    }

    /**
     * populates the rows with either the levelRows type grass, road, or water. This is similar to the
     * populateRowsDefault() function, except this randomly assigns those types to the rows excluding the
     * bottom and top of the environment, which are always grass. It also prevents grass from repeating twice in a row.
     */
    public void randomizeRows() {
        rows[0] = levelRows.GRASS;
        rows[rows.length-1] = levelRows.GRASS;
        for (int i = 1; i < rows.length-1; i++) {
            int rowType = (int)Math.round(Math.random()*3);
            switch (rowType) {
                case 0:
                    if (rows[i-1]==levelRows.GRASS || i == rows.length-2){ //accounts for the row after the start position
                        rows[i] = levelRows.WATER;}
                    else {
                    rows[i] = levelRows.GRASS;}
                    break;
                case 1:
                case 2:
                    rows[i] = levelRows.ROAD;
                    break;
                case 3:
                    rows[i] = levelRows.WATER;
            }
        }
    }

    //Returns true if the given row is water
    public boolean isWater(int c) {
        if (c >= 0 && c <= rows.length) {
        return rows[c] == levelRows.WATER;}
        else {return false;}
    }

    //Returns true if the given row is road
    public boolean isRoad(int c) {
        if (c >= 0 && c <= rows.length) {
        return rows[c] == levelRows.ROAD;}
        else {return false;}
    }
}
