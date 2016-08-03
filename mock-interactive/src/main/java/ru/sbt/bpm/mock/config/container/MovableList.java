package ru.sbt.bpm.mock.config.container;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by sbt-zhdanov-an on 03.08.2016.
 */
public class MovableList<E> extends LinkedList<E> {

    public void moveFirst(int index) {
        E buf = this.get(index);
        this.remove(index);
        this.addFirst(buf);
    }

    public void moveFirst(E obj) {
        int index = this.indexOf(obj);
        moveFirst(index);
    }

    public void moveLast(int index) {
        E buf = this.get(index);
        this.remove(index);
        this.addLast(buf);
    }

    public void moveLast(E obj) {
        int index = this.indexOf(obj);
        moveLast(index);
    }


    public void moveUp(int swapFrom) {
        int swapTo = swapFrom - 1;
        if (swapTo < 0) {
            swapTo = 0;
        }
        Collections.swap(this, swapTo, swapFrom);
    }

    public void moveUp(E obj) {
        int index = this.indexOf(obj);
        moveUp(index);
    }


    public void moveDown(int index) {
        int swapFrom = index;
        int swapTo = index + 1;
        if (swapTo > this.size() - 1) {
            swapTo = this.size() - 1;
        }
        Collections.swap(this, swapFrom, swapTo);
    }

    public void moveDown(E obj) {
        int index = this.indexOf(obj);
        moveDown(index);
    }
}

