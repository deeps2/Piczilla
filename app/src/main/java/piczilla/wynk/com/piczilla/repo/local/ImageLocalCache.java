package piczilla.wynk.com.piczilla.repo.local;

import android.graphics.Bitmap;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImageLocalCache {

    private static ImageLocalCache sInstance;

    private LruCache<String, Bitmap> imageCache;

    public static ImageLocalCache getInstance() {
        if (sInstance == null)
            sInstance = new ImageLocalCache();

        return sInstance;
    }

    private ImageLocalCache() {
        initCache();
    }

    private void initCache() {
        int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int imageCacheSize = MAX_MEMORY / 8;

        imageCache = new LruCache<String, Bitmap>(imageCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addImageToCache(@NonNull String key, @NonNull Bitmap value) {
        if (getImageFromCache(key) == null) {
            imageCache.put(key, value);
        }
    }

    @Nullable
    public Bitmap getImageFromCache(@Nullable String key) {
        if (key == null)
            return null;

        return imageCache.get(key);
    }

    public void clearCache() {
        if (imageCache != null) {
            imageCache.evictAll();
        }
    }
}
