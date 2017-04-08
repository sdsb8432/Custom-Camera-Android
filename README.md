# Custom-Camera-Android
앱을 개발하면 카메라를 종종 사용할 일이 있다. 카메라를 사용하는 건 두 가지의 방법이 존재한다. 첫번째 방법은 Intent 객체를 활용해 내장 카메라 앱으로  캡쳐된 이미지 혹은 영상을 가져오는 것이다. 심플하게 필요한 기능을 통해 원하는 이미지 혹은 영상을 가져올 수 있는 장점이 있지만 이를 커스텀하게 사용은 불가능하다. 두번 째 방법인 Camera 객체를 사용해서 카메라를 조금 더 내가 원하는대로 커스텀하게 사용할 수 있는 방법을 알아볼 것 이다.

## 1. Camera

Camera class는 아래와 같은 객체 선언을 통해 사용이 가능하다.

```
Camera camera = Camera.open();
```

## 2. SurfaceView 
Camera를 SurfaceView를 통해 앱 화면에 표시할 수 있다.
아래 코드를 통해 카메라가 내 앱의 화면에 표시된다.

```
SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceVew);
camera.setPreviewDisplay(surfaceView.getHolder());
camera.startPreview();
```


## 3. Camera.Parameters
안드로이드 기기는 카메라의  사양, 해상도마다 지원하는 지원하는 이미지 혹은 영상 사이즈가 모두 다르다.
그렇기 때문에 당연히 앱이 설치된 기기의 카메라 사이즈 값을 가져와 셋팅이 되게끔 해야 한다.


### 1. 지원하는 사이즈 구하기
해당 코드는 PreviewSize(카메라를 통해 보이는 화면의 해상도), PictureSize(저장되는 이미지), VideoSize(저장되는 영상)를 통해 사이즈를 파악할 수 있다.

```
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
```
Dialog, Fragment 등을 통해 사용자가 직접 선택할 수 있도록 하는 것이 매우 좋다.

### 2. 원하는 사이즈 적용하기

```
Camera.Parameters parameters = camera.getParameters();

parameters.setPictureSize(pictureWidth, pictureHeight);
parameters.setPreviewSize(previewWidth, previewHeight);

camera.setParameters(parameters);
```

영상 사이즈의 경우는 MediaRecorder 객체를 사용하기 때문에 해당 클래스 내의 `setVideoSize(int width, int height)` 메소드를 사용하면 된다.


## 4. options, listener

### 1. zoom

value 인자의 값은 `View.OnTouchListener`를 통해 구해내는 게 일반적이다.

```
camera.setZoomChangeListener(new Camera.OnZoomChangeListener() {
    @Override
    public void onZoomChange(int zoomValue, boolean stopped, Camera camera) {
        //처리
    }
});
camera.startSmoothZoom(value);
```

### 2. auto focus

```
camera.autoFocus(null); //초점 조절 후의 Callback이 필요 없는 경우 (ex. 화면 터치 시 )

//초점 조절 후의 Callback이 필요 있는 경우 (ex. 셔터 클릭 시)
camera.autoFocus(new Camera.AutoFocusCallback() {
    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        camera.takePicture();
    }
});

```

## 5. takePicture
카메라를 표시하고 여러 리스너 혹은 옵션 값을 셋팅했으면 사진을 찍어봐야 한다.
Camera.ShtterCallback 인자 위치에 null 값이 들어가면 카메라가 찍히는 소리가 나지 않는다.
아래는 간단히 사진을 원하는 위치에 저장해보는 코드이다.
파일을 저장하는 코드이므로 꼭 `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`를 사용하도록 하자.

```
camera.takePicture(shutterCallback, null, new Camera.PictureCallback() {
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            FileOutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/Download/"+
        new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg"));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            outputStream.close();
            camera.startPreview(); //셔터와 동시에 찍힌 이미지로 화면이 멈추어 있기 때문에 이를 사용해 다시 재표시를 해야 한다.

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
});
```


## 6. 프로젝트
해당 프로젝트는 MyCamera class를 통해 카메라 객체에 대해 보다 이해하기 쉽도록 만들어두었다.
참고하여 나만의 카메라 앱을 만들어보자.