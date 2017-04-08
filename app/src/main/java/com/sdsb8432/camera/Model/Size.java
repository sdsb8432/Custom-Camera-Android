package com.sdsb8432.camera.Model;

import java.io.Serializable;

/**
 * Created by sonseongbin on 2017. 4. 8..
 */

public class Size implements Serializable{
    public int width;
    public int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
