package com.sdsb8432.camera.Controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Build;

/**
 * Created by sonseongbin on 2017. 4. 3..
 */

public class MyCameraAPI21 {

    private CameraManager cameraManager;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyCameraAPI21(Context context) {
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    private void init() {

    }
}
