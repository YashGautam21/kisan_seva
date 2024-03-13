package com.example.mylogin;

import com.example.mylogin.utilclasses.MapFiller;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.HashMap;

public class Remedy_Page extends AppCompatActivity {
    private String disease,pred_value;
    Map<String, String> symptoms,remedies;
    TextView diseaseName, symptomBox, remedyBox;
    FloatingActionButton expertConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedy_page);
        symptoms = new HashMap<>();
        remedies = new HashMap<>();
        MapFiller.fillMap(symptoms,remedies);
        Intent intent = getIntent();
        disease = intent.getStringExtra("modelOutput");
        pred_value = intent.getStringExtra("predictionValue");
        diseaseName = findViewById(R.id.diseaseName);
        symptomBox = findViewById(R.id.symptomBox);
        remedyBox = findViewById(R.id.remedyBox);
        expertConnect = findViewById(R.id.expertConnect);
        diseaseName.setText(disease + "/n" + pred_value);
        symptomBox.setText(symptoms.getOrDefault(disease,"Disease Not found"));
        remedyBox.setText(remedies.getOrDefault(disease, "Remedy Not found"));
        expertConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openExpertPanel();
            }
        });
    }

    private void openExpertPanel() {
        // Logic
        Intent intent = new Intent(Remedy_Page.this, ExpertsPanel.class);
        intent.putExtra("modelOutput", disease);
        intent.putExtra("predictionValue", pred_value);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // your code.
        Intent intent = new Intent(Remedy_Page.this, CameraActivity.class);
        startActivity(intent);
        finish();
    }


}