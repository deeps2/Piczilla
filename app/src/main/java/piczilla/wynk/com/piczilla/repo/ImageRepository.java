package piczilla.wynk.com.piczilla.repo;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import piczilla.wynk.com.piczilla.model.PhotoBitmapMeta;
import piczilla.wynk.com.piczilla.model.Photos;
import piczilla.wynk.com.piczilla.repo.local.ImageLocalCache;
import piczilla.wynk.com.piczilla.repo.remote.DownloadPhotoAPI;
import piczilla.wynk.com.piczilla.repo.remote.FetchImageMetaAPI;
import piczilla.wynk.com.piczilla.utils.Utility;

//Repository will fetch ImageBitmap either from Local LRU cache or by making API call
public class ImageRepository implements FetchImageMetaAPI.OnImageMetaListener, DownloadPhotoAPI.DownloadPhotoListener {
    private static ImageRepository sInstance;

    private FetchImageMetaAPI fetchImageMetaAPI;

    private MutableLiveData<Boolean> isImageMetaFetched;
    private MutableLiveData<PhotoBitmapMeta> photoBitmapMeta;

    public static ImageRepository getInstance() {
        if (sInstance == null)
            sInstance = new ImageRepository();

        return sInstance;
    }

    private ImageRepository() {
        fetchImageMetaAPI = new FetchImageMetaAPI(this);

        isImageMetaFetched = new MutableLiveData<>();
        photoBitmapMeta = new MutableLiveData<>();
    }

    public void clear() {
        fetchImageMetaAPI = null;
        ImageLocalCache.getInstance().clearCache();
        sInstance = null;
    }

    public LiveData<Boolean> isImageMetaFetchedLiveData() {
        return isImageMetaFetched;
    }

    public MutableLiveData<PhotoBitmapMeta> getPhotoBitmapMetaLiveData() {
        return photoBitmapMeta;
    }

    public int getTotalPhotosCount() {
        return fetchImageMetaAPI.getPhotosListSize();
    }

    public void fetchImagesMetadata() {
        fetchImageMetaAPI = new FetchImageMetaAPI(this);
        Utility.execute(fetchImageMetaAPI);
    }

    @Override
    public void onImageMetaFetched(List<Photos> photos) {
        isImageMetaFetched.setValue(true);
    }

    public void getPhotoForPosition(int currentPos) {
        if (!fetchImageMetaAPI.isPositionValid(currentPos))
            return;

        Photos photo = fetchImageMetaAPI.getPhotoAtPosition(currentPos);
        String completeImageUrl = DownloadPhotoAPI.getCompleteImageUrl(photo);

        Bitmap bitmap = ImageLocalCache.getInstance().getImageFromCache(completeImageUrl);
        if (bitmap != null) {
            photoBitmapMeta.setValue(getPhotoBitmapMeta(bitmap, currentPos));
        } else {
            fetchImageForPosition(currentPos, photo);
        }
    }

    private void fetchImageForPosition(int currentPosition, Photos photo) {
        DownloadPhotoAPI downloadPhotoAPI = new DownloadPhotoAPI(currentPosition, photo, this);
        Utility.execute(downloadPhotoAPI);
    }

    @Override
    public void onPhotoBitmapDownloaded(int positionOfPhotoWhichWasFetched, String completeImageUrl, Bitmap photoBitmap) {
        ImageLocalCache.getInstance().addImageToCache(completeImageUrl, photoBitmap);
        photoBitmapMeta.setValue(getPhotoBitmapMeta(photoBitmap, positionOfPhotoWhichWasFetched));
    }

    private PhotoBitmapMeta getPhotoBitmapMeta(Bitmap bitmap, int pos) {
        PhotoBitmapMeta obj = photoBitmapMeta.getValue();
        if (obj == null)
            obj = new PhotoBitmapMeta();

        obj.setBitmap(bitmap);
        obj.setPositionOfPhotoWhichWasFetched(pos);

        return obj;
    }
}
