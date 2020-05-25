package piczilla.wynk.com.piczilla.model;

import android.graphics.Bitmap;

public class PhotoBitmapMeta {
    private Bitmap bitmap;
    private int positionOfPhotoWhichWasFetched;

    public PhotoBitmapMeta() {
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setPositionOfPhotoWhichWasFetched(int positionOfPhotoWhichWasFetched) {
        this.positionOfPhotoWhichWasFetched = positionOfPhotoWhichWasFetched;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getPositionOfPhotoWhichWasFetched() {
        return positionOfPhotoWhichWasFetched;
    }
}
