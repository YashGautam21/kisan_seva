package com.example.mylogin.model;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import org.tensorflow.lite.Interpreter;

import org.tensorflow.lite.support.common.FileUtil;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Classifier {
    private Interpreter INTERPRETER;
    private List<String> LABEL_LIST;
    private final int INPUT_SIZE;
    private final int PIXEL_SIZE = 3;
    private final float IMAGE_MEAN = 0.0f;
    private final float IMAGE_STD = 255.0f;
    private final int MAX_RESULTS = 3;
    private final float THRESHOLD = 0.4f;

    public static class Recognition {
        public String id = "";
        public String title = "";
        public float confidence = 0;

        public Recognition(String id, String title, float confidence) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
        }

        @Override
        public String toString() {
            return "Title = " + title + ", Confidence = " + confidence;
        }
    }

    public Classifier(AssetManager assetManager, String modelPath, String labelPath, int inputSize) {
        INTERPRETER = new Interpreter(loadModelFile(assetManager, modelPath));
        LABEL_LIST = loadLabelList(assetManager, labelPath);
        INPUT_SIZE = inputSize;
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) {
        try {
//            FileInputStream inputStream = new FileInputStream(modelPath);
//            FileChannel fileChannel = inputStream.getChannel();
//            long startOffset = fileChannel.position();
//            long declaredLength = fileChannel.size();
//            return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, declaredLength);
            AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> loadLabelList(AssetManager assetManager, String labelPath) {
        try {
            List<String> labelList = new ArrayList<>();
            Scanner scanner = new Scanner(assetManager.open(labelPath));
            while (scanner.hasNextLine()) {
                labelList.add(scanner.nextLine());
            }
            scanner.close();
            return labelList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Recognition> recognizeImage(Bitmap bitmap) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
        ByteBuffer byteBuffer = bitmapToByteBuffer(scaledBitmap);
        float[][] result = new float[1][LABEL_LIST.size()];
        INTERPRETER.run(byteBuffer, result);
        return getSortedResult(result);
    }

    private ByteBuffer bitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[INPUT_SIZE * INPUT_SIZE];

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < INPUT_SIZE; i++) {
            for (int j = 0; j < INPUT_SIZE; j++) {
                int val = intValues[pixel++];
                byteBuffer.putFloat((((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                byteBuffer.putFloat((((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                byteBuffer.putFloat(((val & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
            }
        }
        return byteBuffer;
    }

    private List<Recognition> getSortedResult(float[][] labelProbArray) {
        PriorityQueue<Recognition> pq = new PriorityQueue<>(MAX_RESULTS, new Comparator<Recognition>() {
            @Override
            public int compare(Recognition r1, Recognition r2) {
                return Float.compare(r2.confidence, r1.confidence);
            }
        });

        for (int i = 0; i < LABEL_LIST.size(); i++) {
            float confidence = labelProbArray[0][i];
            if (confidence >= THRESHOLD) {
                pq.add(new Recognition(String.valueOf(i),
                        (LABEL_LIST.size() > i) ? LABEL_LIST.get(i) : "Unknown",
                        confidence));
            }
        }

        List<Recognition> recognitions = new ArrayList<>();
        int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < recognitionsSize; i++) {
            recognitions.add(pq.poll());
        }
        return recognitions;
    }
}
