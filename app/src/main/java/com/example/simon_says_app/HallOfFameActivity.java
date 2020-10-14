package com.example.simon_says_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HallOfFameActivity extends AppCompatActivity {
private ListView m_HallOfFameLV;
private FirebaseAuth m_Auth;
private FirebaseDatabase m_FirebaseDatabase;
private DatabaseReference m_DatabaseREFF;
private HashMap<String,Integer> ColHashmap;
private ArrayList m_ArrayList;
private ArrayAdapter m_ArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame);

        m_ArrayList=new ArrayList();

        m_HallOfFameLV=(ListView) findViewById(R.id.HOFlistView);
        m_FirebaseDatabase=FirebaseDatabase.getInstance();

        m_DatabaseREFF=m_FirebaseDatabase.getReference("Users");


        Query queryRef = m_DatabaseREFF.orderByChild("score").limitToLast(10);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserInformation UserInfo = new UserInformation();
                    UserInfo.setNickname(postSnapshot.child("nickname").getValue(String.class));
                    UserInfo.setScore(postSnapshot.child("score").getValue(Integer.class));



                    m_ArrayList.add(UserInfo);

                }
                Collections.reverse(m_ArrayList);
                m_HallOfFameLV.setAdapter(m_ArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        m_ArrayAdapter=new ArrayAdapter(this,R.layout.text_centered,R.id.text_item,m_ArrayList);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
