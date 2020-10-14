package com.example.simon_says_app;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
public class UpdateScoreBackGroundTask extends AsyncTask {

    private WeakReference<TextView> m_ScoreTextView;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener m_authStateListener;
    private DatabaseReference m_DatabaseREFF;


    public UpdateScoreBackGroundTask(TextView i_ScoreTextView, int i_Score)
    {
        m_ScoreTextView=new WeakReference<>(i_ScoreTextView);


    }


    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
