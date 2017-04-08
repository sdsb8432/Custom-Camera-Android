package com.sdsb8432.camera.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sonseongbin on 2017. 4. 8..
 */

public class CameraParameter implements Serializable {
    private List<Size> supportedPreviewSizes;
    private List<Size> supportedPictureSizes;
    private List<Size> supportedVideoSizes;

    public void setSupportedPreviewSizes(List<Size> supportedPreviewSizes) {
        this.supportedPreviewSizes = supportedPreviewSizes;
    }

    public void setSupportedPictureSizes(List<Size> supportedPictureSizes) {
        this.supportedPictureSizes = supportedPictureSizes;
    }

    public void setSupportedVideoSizes(List<Size> supportedVideoSizes) {
        this.supportedVideoSizes = supportedVideoSizes;
    }

    public List<Size> getSupportedPreviewSizes() {
        return supportedPreviewSizes;
    }

    public List<Size> getSupportedPictureSizes() {
        return supportedPictureSizes;
    }

    public List<Size> getSupportedVideoSizes() {
        return supportedVideoSizes;
    }

}
