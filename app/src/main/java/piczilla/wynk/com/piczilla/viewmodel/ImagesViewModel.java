package piczilla.wynk.com.piczilla.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import piczilla.wynk.com.piczilla.model.PhotoBitmapMeta;
import piczilla.wynk.com.piczilla.repo.ImageRepository;

public class ImagesViewModel extends ViewModel {
    private MutableLiveData<Integer> currentPosition;
    private ImageRepository repo;
    private LiveData<PhotoBitmapMeta> photoBitmapMeta;
    private MediatorLiveData<Boolean> isImageMetaFetched;

    private ImagesViewModel() {
        currentPosition = new MutableLiveData<>();
        currentPosition.setValue(0);

        repo = ImageRepository.getInstance();
        photoBitmapMeta = repo.getPhotoBitmapMetaLiveData();

        isImageMetaFetched = new MediatorLiveData<>();
        isImageMetaFetched.addSource(repo.isImageMetaFetchedLiveData(), Boolean -> {
            if (currentPosition != null && Objects.requireNonNull(currentPosition.getValue()) == 0)
                getImageForPosition(currentPosition.getValue());
        });

    }

    public void clearFields() {
        repo.clear();
        repo = null;
    }

    public MutableLiveData<Integer> getCurrentPositionLiveData() {
        return currentPosition;
    }

    public LiveData<PhotoBitmapMeta> getPhotoBitmapMetaLiveData() {
        return photoBitmapMeta;
    }

    public MediatorLiveData<Boolean> isImageMetaFetchedMedLiveData() {
        return isImageMetaFetched;
    }

    public boolean isPositionCorrect(int positionOfImageWhichWasFetched) {
        return positionOfImageWhichWasFetched == Objects.requireNonNull(currentPosition.getValue());
    }

    public boolean isLastImageInList(int position) {
        return position == repo.getTotalPhotosCount() - 1;
    }

    public void onPreviousClicked() {
        int currentPos = Objects.requireNonNull(currentPosition.getValue());
        if (currentPos != 0) {
            currentPosition.setValue(--currentPos);
            getImageForPosition(currentPos);
        }
    }

    public void onNextClicked() {
        int currentPos = Objects.requireNonNull(currentPosition.getValue());
        if (currentPos < repo.getTotalPhotosCount() - 1) {
            currentPosition.setValue(++currentPos);
            getImageForPosition(currentPos);
        }
    }

    public void fetchImageMetaAndSetFirstImage() {
        repo.fetchImagesMetadata();
    }

    private void getImageForPosition(int currentPosition) {
        repo.getPhotoForPosition(currentPosition);
    }

    public static class Factory implements ViewModelProvider.Factory {

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return Objects.requireNonNull(modelClass.cast(new ImagesViewModel()));
        }
    }
}
