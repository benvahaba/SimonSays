package com.example.simon_says_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import java.util.HashMap;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;


public class GameViewActivity extends AppCompatActivity implements IGameCallBacks {

    private ImageButtonWrapper m_RedButton;
    private ImageButtonWrapper m_BlueButton;
    private ImageButtonWrapper m_YelloBlueButton;
    private ImageButtonWrapper m_GreenBlueButton;
    final int NumberOfGamesButtons=4;
    int m_difficultLevel;
    private GameHandler m_gameHandler;
    private int m_GameScore=0;
    Thread m_GameHandlerThread;
    private TextView scoreTV;
    private String m_CurrentScoreSTR;


    Timer timer;



    HashMap<Integer, ImageButtonWrapper> m_ButtonsHashmap;
    ImageButtonWrapper[] m_ImageButtonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

        m_CurrentScoreSTR=getResources().getString(R.string.current_score);
        initButtonsAndHashMap();

        Bundle extras = getIntent().getExtras();

        /*
        * in the activity that call this activity do
        *
        *         Intent i = new Intent(this, GameViewActivity.class);
        i.putExtra("difficultLevel",TheDifficultylevel);
        startActivity(i);
        *
        *
        * Notice! TheDifficultylevel= 1 or 5 or 10. the amount of time that it takes to a new button to be pressed
        * by the computer is divided by that Integer so dont give numbers that are too high
        * */

        m_difficultLevel=extras.getInt("difficultLevel");//remember this string to pass from the main activity as the key
        scoreTV=findViewById(R.id.scoreLabelTextView);
        m_GameScore=0;
        UpdateScore(m_GameScore);

        //runs the GameHandler in a new Thread so it wont run on the main thread.
        //game handler is used to handle the entire game and uses a deligate to change the score and end the game

        timer = new Timer();
        m_gameHandler=new GameHandler(this,m_ImageButtonArray,m_ButtonsHashmap,m_difficultLevel);

       timer.schedule(m_gameHandler,1000);

    }





    public void OnGameBottonClicked(View view)
    {
        ImageButtonWrapper Button=m_ButtonsHashmap.get(view.getId());

/*        Thread thread=new Thread(Button);//runs our special button
        thread.start();*/
        Button.run();
        if(m_gameHandler.PlayerTurn=true)// checks who's turn is it. if its the players turn it adds the button id to a queue that indicates which buttons was pressed by the player
        {
            try
            {
                UserButtonPresses.Add(Button.GetButtonNumber());
            }
            catch (Exception Ex)
            {
                Ex.printStackTrace();
                //TODO
            }

        }
    }
    private void initButtonsAndHashMap() {

        //Create your Game Button Music here
        final MediaPlayer MP1=MediaPlayer.create(this,R.raw.chord1);
        MP1.setVolume(0.8f,0.8f);
        final MediaPlayer MP2=MediaPlayer.create(this,R.raw.chord2);
        MP2.setVolume(0.8f,0.8f);
        final MediaPlayer MP3=MediaPlayer.create(this,R.raw.chord3);
        MP3.setVolume(0.8f,0.8f);
        final MediaPlayer MP4=MediaPlayer.create(this,R.raw.chord4);
        MP4.setVolume(0.8f,0.8f);
        //

        int buttonNumber=0;
        m_BlueButton = new ImageButtonWrapper(MP1,findViewById(R.id.BlueIB), R.drawable.upleftblue, R.drawable.upleftpressedblue,buttonNumber++);
        m_GreenBlueButton = new ImageButtonWrapper(MP2,findViewById(R.id.GreenIB), R.drawable.uprightgreen, R.drawable.uprightpressedgreen,buttonNumber++);
        m_YelloBlueButton = new ImageButtonWrapper(MP3,findViewById(R.id.YellowIB), R.drawable.downrightyellow, R.drawable.downrightpressedyellow,buttonNumber++);
        m_RedButton = new ImageButtonWrapper(MP4,findViewById(R.id.RedIB), R.drawable.downleftredbutton, R.drawable.downleftpressedredbutton,buttonNumber++);


        m_ButtonsHashmap=new HashMap<>(NumberOfGamesButtons);
        m_ImageButtonArray = new ImageButtonWrapper[NumberOfGamesButtons];


        m_ButtonsHashmap.put((Integer)m_BlueButton.GetImageButtonID(), m_BlueButton);
        m_ImageButtonArray[m_BlueButton.GetButtonNumber()]=m_BlueButton;

        m_ButtonsHashmap.put((Integer)m_GreenBlueButton.GetImageButtonID(), m_GreenBlueButton);
        m_ImageButtonArray[m_GreenBlueButton.GetButtonNumber()]=m_GreenBlueButton;

        m_ButtonsHashmap.put((Integer)m_YelloBlueButton.GetImageButtonID(), m_YelloBlueButton);
        m_ImageButtonArray[m_YelloBlueButton.GetButtonNumber()]=m_YelloBlueButton;

        m_ButtonsHashmap.put((Integer)m_RedButton.GetImageButtonID(), m_RedButton);
        m_ImageButtonArray[m_RedButton.GetButtonNumber()]=m_RedButton;
    }

    @Override
    public void GameOver() {
        Intent PlayActivityIntent=new Intent(this,PlayActivity.class);
        PlayActivityIntent.putExtra("score",m_GameScore);
        setResult(4,PlayActivityIntent);


        Intent FinalScoreActivityIntent=new Intent(this,FinalScoreActivity.class);
        FinalScoreActivityIntent.putExtra("score",m_GameScore);

        startActivity(FinalScoreActivityIntent);

        finish();


    }
    @Override
    public void UpdateScore(int i_score) {
        m_GameScore = i_score;
        StringBuilder tempScoreSB=new StringBuilder();
        tempScoreSB.append(m_CurrentScoreSTR+" "+m_GameScore);
        Thread thread = new Thread(){
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scoreTV.setText(tempScoreSB.toString());

                    }
                });

            }
        };

        thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
/*        if(m_GameHandlerThread.isAlive())
        {
            try {
                m_GameHandlerThread.wait();
            }
            catch (InterruptedException itrpEx) {itrpEx.printStackTrace(); }
        }*/


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(m_GameHandlerThread.getState()== Thread.State.WAITING)
        {
           // m_GameHandlerThread.notify();

        }

    }

/*    @Override
   protected void onStart() {
        super.onStart();
        if (m_GameHandlerThread.getState() == Thread.State.WAITING) {
            m_GameHandlerThread.interrupt();

        }
    }*/
}
