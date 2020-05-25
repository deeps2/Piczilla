package piczilla.wynk.com.piczilla.repo.remote;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import piczilla.wynk.com.piczilla.model.Photos;
import piczilla.wynk.com.piczilla.utils.Utility;

public class FetchImageMetaAPI extends AsyncTask<String, Void, List<Photos>> {

    private final String API_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1&tags=kitten&per_page=10&page=1";
    private OnImageMetaListener callbackHandler;
    private List<Photos> photosList;

    public FetchImageMetaAPI(OnImageMetaListener callbackHandler) {
        this.callbackHandler = callbackHandler;
        this.photosList = new ArrayList<>();
    }

    public int getPhotosListSize() {
        return photosList.size();
    }

    public boolean isPositionValid(int pos) {
        return pos < getPhotosListSize();
    }

    public Photos getPhotoAtPosition(int position) {
        return photosList.get(position);
    }

    @Override
    protected List<Photos> doInBackground(String... strings) {
        String responseString = Utility.getResponse(API_URL);
        List<Photos> photos = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(responseString);
            JSONObject photoJson = jsonResponse.getJSONObject("photos");
            JSONArray photoArray = photoJson.getJSONArray("photo");
            for (int i = 0; i < photoArray.length(); i++) {
                JSONObject photoObj = photoArray.getJSONObject(i);
                Photos photo = Photos.getObject(photoObj.optString("farm"),
                        photoObj.optString("server"),
                        photoObj.optString("id"),
                        photoObj.optString("secret"));

                photos.add(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photos;
    }

    @Override
    protected void onPostExecute(List<Photos> photos) {
        photosList = photos;
        callbackHandler.onImageMetaFetched(photos);
    }

    public interface OnImageMetaListener {
        void onImageMetaFetched(List<Photos> photos);
    }

}

