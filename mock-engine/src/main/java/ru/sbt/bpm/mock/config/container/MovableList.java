/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

