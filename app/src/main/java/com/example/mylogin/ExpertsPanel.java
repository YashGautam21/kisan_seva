package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mylogin.experts.ListAdapter;
import com.example.mylogin.experts.User;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.example.mylogin.databinding.ActivityExpertsPanelBinding;
import com.example.mylogin.experts.UserActivity;

import java.util.ArrayList;

public class ExpertsPanel extends AppCompatActivity {
    ActivityExpertsPanelBinding binding;
    private String disease, pred_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpertsPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        disease = intent.getStringExtra("modelOutput");
        pred_value = intent.getStringExtra("predictionValue");
        int[] imageId = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,
                R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i};
        String[] name = {"Dr. Banawari Mishra","Dr. Yogananda S B","Dr. Krishna Pratap Singh","Dr. Vijai Pal Singh","Dr. Deepak Pental  ","Dr. Rasappa Viswanathan","Dr. Sollepura Nanjegowda Swamygowda","Dr. ANILKUMAR DANDEKAR","\n" +
                "Dr. Om Prakash Chaturvedi"};
        String[] lastMessage = {"AgroExpert","AgroExpert","AgroExpert","AgroExpert","AgroExpert",
                "AgroExpert","AgroExpert","AgroExpert","AgroExpert"};
        String[] lastmsgTime = {"8:45 pm","9:00 am","7:34 pm","6:32 am","5:76 am",
                "5:00 am","7:34 pm","2:32 am","7:76 am"};
        String[] phoneNo = {"7656610000","9999043232","7834354323","9876543211","7454215896",
                "9439043232","7534354323","6545543211","7654432343"};
        String[] country = {"UttarPradesh","New Delhi","Karnataka","Tamil Nadu","Madhya Pradesh","Maharashtra","New Delhi","Haryana","Goa"};

        ArrayList<User> userArrayList = new ArrayList<>();

        for(int i = 0;i< imageId.length;i++){

            User user = new User(name[i],lastMessage[i],lastmsgTime[i],phoneNo[i],country[i],imageId[i]);
            userArrayList.add(user);

        }


        ListAdapter listAdapter = new ListAdapter(ExpertsPanel.this,userArrayList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ExpertsPanel.this, UserActivity.class);
                i.putExtra("name",name[position]);
                i.putExtra("phone",phoneNo[position]);
                i.putExtra("state",country[position]);
                i.putExtra("imageid",imageId[position]);
                startActivity(i);

            }
        });
    }
    @Override
    public void onBackPressed() {
        // your code.
        Intent intent = new Intent(ExpertsPanel.this, Remedy_Page.class);
        intent.putExtra("modelOutput", disease);
        intent.putExtra("predictionValue", pred_value);
        startActivity(intent);
        finish();
    }
}