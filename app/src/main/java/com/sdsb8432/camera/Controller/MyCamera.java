package com.sdsb8432.camera.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sonseongbin on 2017. 4. 3..
 */

public class MyCamera implements View.OnTouchListener, Camera.AutoFocusCallback {

    private final String TAG = MyCamera.class.getSimpleName();

    private Context context;
    private Camera camera;

    public MyCamera(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        camera = Camera.open();

    }

    public void start(SurfaceView surfaceView) {
        try {
            camera.setPreviewDisplay(surfaceView.getHolder());

            Camera.Parameters parameters = camera.getParameters();

            for(Camera.Size size : parameters.getSupportedPreviewSizes()) {
                Log.d(TAG, "PreviewSize = width : " + String.valueOf(size.width) + ", height : " + String.valueOf(size.height));
            }

            for(Camera.Size size : parameters.getSupportedPictureSizes()) {
                Log.d(TAG, "PictureSize = width : " + String.valueOf(size.width) + ", height : " + String.valueOf(size.height));
            }


            for(Camera.Size size : parameters.getSupportedVideoSizes()) {
                Log.d(TAG, "VideoSize = width : " + String.valueOf(size.width) + ", height : " + String.valueOf(size.height));
            }

            camera.startPreview();
            surfaceView.setOnTouchListener(this);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        camera.stopPreview();
    }

    public void takePicture() {
        camera.autoFocus(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                camera.autoFocus(null);
                break;
        }

        return true;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        Log.d(TAG, "onAutoFocus");
        camera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        }, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    FileOutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/Download/1.jpg"));

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                    outputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
