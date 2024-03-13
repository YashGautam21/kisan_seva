package com.example.mylogin.model;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mylogin.CameraActivity;
import com.example.mylogin.R;
import com.example.mylogin.Remedy_Page;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageCaptureMain extends AppCompatActivity {
    private Classifier mClassifier;
    private Bitmap mBitmap;

    private final int mCameraRequestCode = 0;
    //mInputSize_resnet_tflite = 200, CNN_model.tflite = 224, wheat_model = 224
    private final int mInputSize = 224; //200 //224
    private final String mModelPath = "wheat_leaf_model.tflite";
    private final String mLabelPath = "wheat_label.txt";
    FloatingActionButton mCameraButton;
    ImageView mPhotoImageView;
    TextView mResultTextView;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
//        mCameraButton = findViewById(R.id.CameraButton);
//        mPhotoImageView = findViewById(R.id.PhotoImageView);
//        mResultTextView = findViewById(R.id.ResultTextView);
        mClassifier = new Classifier(getAssets(), mModelPath, mLabelPath, mInputSize);

//        mCameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent callCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(callCameraIntent, mCameraRequestCode);
//            }
//        });

        Intent callCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(callCameraIntent, mCameraRequestCode);
    }

    public void openCam(View v) {
        Intent callCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(callCameraIntent, mCameraRequestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoImageView = findViewById(R.id.PhotoImageView);
        mResultTextView = findViewById(R.id.ResultTextView);
        if (requestCode == mCameraRequestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mBitmap = (Bitmap) data.getExtras().get("data");
//                mPhotoImageView.setImageBitmap(mBitmap);
                Classifier.Recognition modelOutput = mClassifier.recognizeImage(scaleImage(mBitmap)).stream().findFirst().orElse(null);
                if (modelOutput != null) {
//                    mResultTextView.setText(modelOutput.title + "\n" + modelOutput.confidence);
                    Toast.makeText(this, modelOutput.title + "\n" + modelOutput.confidence, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ImageCaptureMain.this, Remedy_Page.class);
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
        Intent intent = new Intent(ImageCaptureMain.this, CameraActivity.class);
        startActivity(intent);
        finish();
    }

}