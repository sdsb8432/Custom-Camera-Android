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

import com.sdsb8432.camera.Model.CameraParameter;
import com.sdsb8432.camera.Model.Size;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sonseongbin on 2017. 4. 3..
 */

public class MyCamera implements View.OnTouchListener, Camera.AutoFocusCallback {

    private final String TAG = MyCamera.class.getSimpleName();

    private Context context;
    private Camera camera;

    private Camera.ShutterCallback shutterCallback;

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
            camera.startPreview();
            surfaceView.setOnTouchListener(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        camera.stopPreview();
    }

    public void setShutterCallback(Camera.ShutterCallback shutterCallback) {
        this.shutterCallback = shutterCallback;
    }

    public CameraParameter getCameraParameters() {

        Camera.Parameters parameters = camera.getParameters();

        List<Size> sizeList = new ArrayList<>();
        CameraParameter cameraParameter = new CameraParameter();

        for(Camera.Size size : parameters.getSupportedPreviewSizes()) {
            sizeList.add(new Size(size.width, size.height));
        }

        cameraParameter.setSupportedPreviewSizes(sizeList);
        sizeList = new ArrayList<>();

        for(Camera.Size size : parameters.getSupportedPictureSizes()) {
            sizeList.add(new Size(size.width, size.height));
        }

        cameraParameter.setSupportedPictureSizes(sizeList);
        sizeList = new ArrayList<>();

        for(Camera.Size size : parameters.getSupportedVideoSizes()) {
            sizeList.add(new Size(size.width, size.height));
        }

        cameraParameter.setSupportedVideoSizes(sizeList);

        return cameraParameter;
    }

    public void setPictureSize(Size size) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureSize(size.width, size.height);
        camera.setParameters(parameters);
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
        camera.takePicture(shutterCallback, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    FileOutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/Download/"+
                            new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg"));

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                    outputStream.close();

                    camera.startPreview();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
