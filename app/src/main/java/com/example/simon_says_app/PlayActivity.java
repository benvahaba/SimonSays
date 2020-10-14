package com.example.simon_says_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlayActivity extends AppCompatActivity
{

    private Button playBTN;
    Dialog myDialog;
    private int m_score;
    private String m_nickname;
    private TextView HighestScoreTV;
    private String m_UserBestScoreStartingText;
    private TextView m_NickNameTV;
    private String m_NickNameStartingText;
    private Button m_EasyBTN;
    private Button m_MidBTN;
    private Button m_HardBTN;
    private Button m_HallOfFameBTN;
    private boolean v_PlayIsPressed;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener m_authStateListener;
    private DatabaseReference m_DatabaseREFF;
    private DatabaseReference m_userScoreReff;
    private DatabaseReference m_userNNReff;
    private String UserID;
    private FirebaseUser user;
    private UserInformation UInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        m_UserBestScoreStartingText=getResources().getString(R.string.UserBestScoreStartingText);
        m_NickNameStartingText=getResources().getString(R.string.NickNameStartingText);
        m_HallOfFameBTN=findViewById(R.id.halloffameBTN);
        HighestScoreTV=findViewById(R.id.HighestScorePlayAcTV);
        m_NickNameTV=findViewById(R.id.UserNickNameInPlayAcTV);
        m_EasyBTN=findViewById(R.id.easyBTN);
        m_MidBTN=findViewById(R.id.midBTN);
        m_HardBTN=findViewById(R.id.hardBTN);
        playBTN=findViewById(R.id.playBTN);
        m_score=0;

        playBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v_PlayIsPressed==false) {
                    m_HallOfFameBTN.setVisibility(View.INVISIBLE);
                    ShowDiffLVLs();
                }
                else
                {
                    m_HallOfFameBTN.setVisibility(View.VISIBLE);
                    DontShowDiffLVLs();
                }
                v_PlayIsPressed=!v_PlayIsPressed;

            }
        });


        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        user=mAuth.getCurrentUser();
        UserID=user.getUid();
        m_DatabaseREFF=firebaseDatabase.getReference("Users"+"/"+UserID);

        m_userScoreReff=m_DatabaseREFF.child("score");
        m_userNNReff=m_DatabaseREFF.child("nickname");


        m_userNNReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                m_nickname = dataSnapshot.getValue(String.class);
                updateNickNameText(m_nickname);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        m_userScoreReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                m_score=dataSnapshot.getValue(Integer.class);
                updateScoreText(m_score);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        myDialog =new Dialog(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        v_PlayIsPressed=false;
        m_HallOfFameBTN.setVisibility(View.VISIBLE);
        DontShowDiffLVLs();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4) {
            Bundle extras = data.getExtras();
            int tempScore = extras.getInt("score");
            if (tempScore > m_score) {
                // update database
                m_score = tempScore;
                m_userScoreReff.setValue(m_score);
                updateScoreText(m_score);

            }
        }
    }

    public void ShowDiffLVLs()
    {
        m_EasyBTN.setVisibility(View.VISIBLE);
        m_MidBTN.setVisibility(View.VISIBLE);
        m_HardBTN.setVisibility(View.VISIBLE);
    }
    public void DontShowDiffLVLs()
    {
        m_EasyBTN.setVisibility(View.INVISIBLE);
        m_MidBTN.setVisibility(View.INVISIBLE);
        m_HardBTN.setVisibility(View.INVISIBLE);

    }

    public void showInfo(View V)
    {
        TextView textView;
        myDialog.setContentView(R.layout.infopopup);
        textView= (TextView) myDialog.findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    public void oneasyBTNclick(View view) { startGameWithSetDifficulty(1);}
    public void onmidBTNclick(View view) {startGameWithSetDifficulty(7); }
    public void onhardBTNclick(View view) { startGameWithSetDifficulty(15); }
    public void startGameWithSetDifficulty(int TheDifficultylevel)
    {
        Intent i = new Intent(this, GameViewActivity.class);
        i.putExtra("difficultLevel",TheDifficultylevel);
        startActivityForResult(i,4);
    }

    private void updateNickNameText(String m_nickname) {
        StringBuilder nickNameSB=new StringBuilder();
        nickNameSB.append(nickNameSB.toString());

        m_NickNameTV.setText(m_NickNameStartingText+m_nickname);
        m_NickNameTV.invalidate();
    }

    public void updateScoreText(final int i_score) {
        StringBuilder scoreSB=new StringBuilder();
        scoreSB.append(m_UserBestScoreStartingText+" "+i_score);
        HighestScoreTV.setText(scoreSB.toString());
        HighestScoreTV.invalidate();

    }



    @Override
    protected void onStart() {
        super.onStart();
        m_authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 user=firebaseAuth.getCurrentUser();
            }
        };

    }

    public void onHOFBTNclicked(View view)
    {
        Intent HOFIntent=new Intent(this,HallOfFameActivity.class);
        startActivity(HOFIntent);

    }
}

