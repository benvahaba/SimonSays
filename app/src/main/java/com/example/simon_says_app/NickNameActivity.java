package com.example.simon_says_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NickNameActivity extends AppCompatActivity {
   private FirebaseDatabase firebaseDatabase;
   private FirebaseAuth mAuth;
   private FirebaseAuth.AuthStateListener m_authStateListener;
    private DatabaseReference m_DatabaseREFF;
    private String UserID;
    private EditText inputText;
    private Button chooseNickBTN;
    private String nickname;
    private UserInformation UInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);

        chooseNickBTN=findViewById(R.id.ChooseNickName);
        inputText=findViewById(R.id.editText);
        chooseNickBTN.setVisibility(View.INVISIBLE);
        inputText.setVisibility(View.INVISIBLE);


        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        FirebaseUser user=mAuth.getCurrentUser();
        UserID=user.getUid();
        m_DatabaseREFF=firebaseDatabase.getReference("Users"+"/"+UserID);



        m_authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
            }
        };

        m_DatabaseREFF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UInfo = new UserInformation();
                UInfo.setNickname(dataSnapshot.child("nickname").getValue(String.class));
                UInfo.setScore(dataSnapshot.child("score").getValue(Integer.class));
                if(UInfo.getNickname().isEmpty())
                {
                    showNickNameCreator();
                }
                else
                {
                    startPlaying();
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});


    }

    public void showNickNameCreator()
    {
        chooseNickBTN.setVisibility(View.VISIBLE);

        inputText.setVisibility(View.VISIBLE);

    }




    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(m_authStateListener);

    }
    @Override
    public void onStop()
    {
        super.onStop();
        if(m_authStateListener!=null)
        {
            mAuth.removeAuthStateListener(m_authStateListener);
        }


    }


    public void OnEditTextClick(View view)
    {

        nickname=inputText.getText().toString();
        if(nickname.length()>10||nickname.length()==0)
        {
            Toast.makeText(this, "Please enter a valid name(not more than 10 letters)", Toast.LENGTH_LONG).show();

            inputText.getText().clear();
        }
        else
        {

            m_DatabaseREFF.child("nickname").setValue(nickname);
            UInfo.setNickname(nickname);
            chooseNickBTN.setVisibility(View.INVISIBLE);
            inputText.setVisibility(View.INVISIBLE);
            startPlaying();

        }
    }
    private void startPlaying()
    {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);

        finish();

    }
}
