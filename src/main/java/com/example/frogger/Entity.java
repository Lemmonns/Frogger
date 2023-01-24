package com.example.frogger;

public interface Entity {

    public void move();

    public float getX();

    public float getY();

    public boolean isOnScreen();
}
