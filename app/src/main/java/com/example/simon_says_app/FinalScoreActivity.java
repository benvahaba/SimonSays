package com.example.simon_says_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FinalScoreActivity extends AppCompatActivity {
    TextView m_Score;
    int m_ScoreSTR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);
        m_Score=findViewById(R.id.scoreTV);
        Bundle extras = getIntent().getExtras();
        m_ScoreSTR=extras.getInt("score");
        m_Score.setText(Integer.toString(m_ScoreSTR));
    }

    public void onContinueClicked(View view) {
        finish();
    }
}
