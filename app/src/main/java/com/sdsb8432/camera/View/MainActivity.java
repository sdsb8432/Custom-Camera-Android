package com.sdsb8432.camera.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import com.sdsb8432.camera.Controller.MyCamera;
import com.sdsb8432.camera.Controller.MyCameraAPI21;
import com.sdsb8432.camera.Model.Size;
import com.sdsb8432.camera.R;
import com.sdsb8432.camera.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.ShutterCallback{

    private final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mainBinding;

    private MyCameraAPI21 myCameraAPI21;
    private MyCamera myCamera;

    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        else {
            init();
        }

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setCamera();
        }
        else {
            setCameraAPI21();
        }*/

        mainBinding.textViewProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PropertyActivity.class).putExtra("cameraParameters", myCamera.getCameraParameters()));
            }
        });
    }

    private void init() {
        surfaceHolder = mainBinding.surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        mainBinding.buttonShutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCamera.takePicture();
            }
        });
    }

    private void setCamera() {
        myCamera = new MyCamera(this);
        myCamera.setPictureSize(new Size(5312, 2988));

        //셔터 callback 처리와 동시에 셔터음을 발생시킬 수 있다.
        myCamera.setShutterCallback(this);
        myCamera.start(mainBinding.surfaceView);

    }

    private void setCameraAPI21() {
        myCameraAPI21 = new MyCameraAPI21(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        setCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        myCamera.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                setCamera();
            }
            else {
                finish();
            }
        }
    }

    @Override
    public void onShutter() {
        //처리
    }
}
