package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class UserType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_farmer:
                if (checked) {
                    Toast.makeText(this, "Checked_Farmer_Radio", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserType.this, CameraActivity.class);
                    startActivity(intent);
                }
                    break;
            case R.id.radio_agro:
                if (checked)
                    Toast.makeText(this, "Checked_Agro_Radio", Toast.LENGTH_SHORT).show();
                    break;
        }
    }
}