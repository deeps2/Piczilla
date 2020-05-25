package piczilla.wynk.com.piczilla.repo.remote;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import piczilla.wynk.com.piczilla.model.Photos;
import piczilla.wynk.com.piczilla.utils.Utility;

public class DownloadPhotoAPI extends AsyncTask<String, Void, Bitmap> {
    private int positionOfPhotoToBeFetched;
    private String completeImageUrl;
    private DownloadPhotoListener callbackHandler;

    public DownloadPhotoAPI(int positionOfPhotoToBeFetched, Photos photo, DownloadPhotoListener callbackHandler) {
        this.positionOfPhotoToBeFetched = positionOfPhotoToBeFetched;
        this.completeImageUrl = getCompleteImageUrl(photo);
        this.callbackHandler = callbackHandler;
    }

    public static String getCompleteImageUrl(Photos photo) {
        return "https://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + "_q.jpg";
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return Utility.getBitmapFromURL(completeImageUrl);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null)
            callbackHandler.onPhotoBitmapDownloaded(positionOfPhotoToBeFetched, completeImageUrl, result);
    }

    public interface DownloadPhotoListener {
        void onPhotoBitmapDownloaded(int positionOfPhotoWhichWasFetched, String completeImageUrl, Bitmap photoBitmap);
    }

}
