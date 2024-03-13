package com.example.mylogin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylogin.model.Classifier;
import com.example.mylogin.model.ImageCaptureMain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class GalleryActivity extends AppCompatActivity {
    private static int PICK_IMAGE_REQUEST = 1;
    private Classifier mClassifier;
    private Bitmap mBitmap;

    private final int mCameraRequestCode = 0;
    //mInputSize_resnet_tflite = 200, CNN_model.tflite = 224, wheat_model = 224
    private final int mInputSize = 224; //200
    private final String mModelPath = "wheat_leaf_model.tflite";
    private final String mLabelPath = "wheat_label.txt";
//    FloatingActionButton mCameraButton;
//    ImageView mPhotoImageView;
//    TextView mResultTextView;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mClassifier = new Classifier(getAssets(), mModelPath, mLabelPath, mInputSize);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mPhotoImageView = findViewById(R.id.PhotoImageView);
//        mResultTextView = findViewById(R.id.ResultTextView);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {
                    mBitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                mPhotoImageView.setImageBitmap(mBitmap);
                Classifier.Recognition modelOutput = mClassifier.recognizeImage(scaleImage(mBitmap)).stream().findFirst().orElse(null);
                if (modelOutput != null) {
//                    mResultTextView.setText(modelOutput.title + "\n" + modelOutput.confidence);
                    Toast.makeText(this, modelOutput.title + "\n" + modelOutput.confidence, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GalleryActivity.this, Remedy_Page.class);
                    intent.putExtra("modelOutput", modelOutput.title+"");
                    intent.putExtra("predictionValue", modelOutput.confidence+"");
                    startActivity(intent);
                    Log.d("Model_Output", modelOutput.title + "\n" + modelOutput.confidence);
                }
            }
        }
    }

    private Bitmap scaleImage(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaledWidth = (float) mInputSize / width;
        float scaledHeight = (float) mInputSize / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaledWidth, scaledHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
    @Override
    public void onBackPressed() {
        // your code.
        Intent intent = new Intent(GalleryActivity.this, CameraActivity.class);
        startActivity(intent);
        finish();
    }
}