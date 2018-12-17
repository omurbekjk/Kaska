package io.jachoteam.kaska.models;

import java.util.ArrayList;

/**
 * Created by User on 16.12.2018.
 */

public class Likes {
    int count;
    boolean isYourLike;

    ArrayList<String> keys;


    public Likes() {
    }

    public Likes(int count, boolean isYourLike, ArrayList<String> keys) {
        this.count = count;
        this.isYourLike = isYourLike;
        this.keys = keys;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isYourLike() {
        return isYourLike;
    }

    public void setYourLike(boolean yourLike) {
        isYourLike = yourLike;
    }

    public Likes(int count, boolean isYourLike) {

        this.count = count;
        this.isYourLike = isYourLike;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<String> keys) {
        this.keys = keys;
    }
}
