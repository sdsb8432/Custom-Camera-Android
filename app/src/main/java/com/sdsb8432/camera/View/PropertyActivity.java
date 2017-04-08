package com.sdsb8432.camera.View;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sdsb8432.camera.Model.CameraParameter;
import com.sdsb8432.camera.Model.Size;
import com.sdsb8432.camera.R;
import com.sdsb8432.camera.databinding.ActivityPropertyBinding;

public class PropertyActivity extends AppCompatActivity {

    private ActivityPropertyBinding propertyBinding;
    private CameraParameter cameraParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        propertyBinding = DataBindingUtil.setContentView(this, R.layout.activity_property);

        cameraParameter = (CameraParameter) getIntent().getSerializableExtra("cameraParameters");

        for(Size size : cameraParameter.getSupportedPreviewSizes()) {
            propertyBinding.textViewSupportedPreviewSize.setText(propertyBinding.textViewSupportedPreviewSize.getText().toString() +
                    String.valueOf(size.width) + "*" + String.valueOf(size.height) + "\n");
        }

        for(Size size : cameraParameter.getSupportedPictureSizes()) {
            propertyBinding.textViewSupportedPictureSize.setText(propertyBinding.textViewSupportedPictureSize.getText().toString() +
                    String.valueOf(size.width) + "*" + String.valueOf(size.height) + "\n");
        }


        for(Size size : cameraParameter.getSupportedVideoSizes()) {
            propertyBinding.textViewSupportedVideoSize.setText(propertyBinding.textViewSupportedVideoSize.getText().toString() +
                    String.valueOf(size.width) + "*" + String.valueOf(size.height) + "\n");
        }

    }
}
