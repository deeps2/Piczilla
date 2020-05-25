package piczilla.wynk.com.piczilla.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import piczilla.wynk.com.piczilla.R;
import piczilla.wynk.com.piczilla.viewmodel.ImagesViewModel;

public class MainActivity extends AppCompatActivity {
    private ImageView flickerImage;
    private Button previous;
    private Button next;
    private ImagesViewModel imagesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        initialise();
        addObservers();
        imagesViewModel.fetchImageMetaAndSetFirstImage();
    }

    private void initialise() {
        //1) initialise views
        flickerImage = findViewById(R.id.flicker_image);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        //2) add click listeners
        previous.setOnClickListener(v -> onPreviousButtonClicked());
        next.setOnClickListener(v -> onNextButtonClicked());

        //3) init View Model
        imagesViewModel = new ViewModelProvider(this, new ImagesViewModel.Factory()).get(ImagesViewModel.class);
    }

    //add observer to listen to LiveData changes
    private void addObservers() {
        imagesViewModel.getCurrentPositionLiveData().observe(this, position -> {
            if (position == 0) {
                previous.setClickable(false);
            } else if (imagesViewModel.isLastImageInList(position)) {
                next.setClickable(false);
            } else {
                previous.setClickable(true);
                next.setClickable(true);
            }
        });

        imagesViewModel.getPhotoBitmapMetaLiveData().observe(this, photoBitmapMeta -> {
            if (imagesViewModel.isPositionCorrect(photoBitmapMeta.getPositionOfPhotoWhichWasFetched()))
                flickerImage.setImageBitmap(photoBitmapMeta.getBitmap());
        });

        imagesViewModel.isImageMetaFetchedMedLiveData().observe(this, aBoolean -> {
        });
    }

    private void onPreviousButtonClicked() {
        imagesViewModel.onPreviousClicked();
    }

    private void onNextButtonClicked() {
        imagesViewModel.onNextClicked();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imagesViewModel.clearFields();
    }
}
