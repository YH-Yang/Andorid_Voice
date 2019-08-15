package com.yyh.utils;

public class CameraUtils {

    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
    private AutoFitTextureView texture_front;
    private AutoFitTextureView texture_back;

//    private void configureTransform(boolean isBack,int viewWidth, int viewHeight) {
//        Activity activity = getActivity();
//        if (null ==getTexture(isBack) || null == mPreviewSize || null == activity) {
//            return;
//        }
//        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        Matrix matrix = new Matrix();
//        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
//        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
//        float centerX = viewRect.centerX();
//        float centerY = viewRect.centerY();
//        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
//            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
//            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
//            float scale = Math.max(
//                    (float) viewHeight / mPreviewSize.getHeight(),
//                    (float) viewWidth / mPreviewSize.getWidth());
//            matrix.postScale(scale, scale, centerX, centerY);
//            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
//        }
//        getTexture(isBack).setTransform(matrix);
//    }

    private AutoFitTextureView getTexture(boolean isBack){
        if (isBack){
            return texture_back;
        }else {
            return texture_front;
        }
    }

}
