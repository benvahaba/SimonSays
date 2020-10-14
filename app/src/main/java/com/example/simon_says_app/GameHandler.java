package com.example.simon_says_app;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

public class GameHandler  extends TimerTask {
    private RoundCreator m_roundCreator;
    private int m_buttonPresses;
    ImageButtonWrapper[] m_buttons;
    HashMap<Integer, ImageButtonWrapper> m_ButtonsHashmap;
    final int m_DefaultStartingButtenPresses=2;

    private IGameCallBacks m_GameCallback;

    private int playerMovesCounter;
    private int score=0;
    public static boolean PlayerTurn=false;
    private boolean m_PlayerLost;
    private int roundCounter;




    public  GameHandler(IGameCallBacks i_GameCallback,ImageButtonWrapper[] i_buttons, HashMap<Integer, ImageButtonWrapper> i_ButtonsHashmap, int i_GameDifficulty) {
        Log.d("GH","Ctor");
        m_GameCallback=i_GameCallback;
        m_roundCreator = new RoundCreator(i_buttons, i_GameDifficulty);
        m_ButtonsHashmap = i_ButtonsHashmap;

        m_buttonPresses = m_DefaultStartingButtenPresses;

        m_PlayerLost=false;
        m_buttons = i_buttons;

        //setClassAsTheButtonsListener();
        UserButtonPresses.SetNewBlockingQueue(m_buttonPresses);
    }

    @Override
    public void run() {
        Chord[] LastChordsPlayedByComputer;

        while(m_PlayerLost==false)
        {
            UserButtonPresses.SetNewBlockingQueue(m_buttonPresses);

            disableGameButtons();
            LastChordsPlayedByComputer = m_roundCreator.round(m_buttonPresses);
            enableGameButtons();
            playerMovesCounter=0;

            while(playerMovesCounter<m_buttonPresses) {

                if (UserButtonPresses.GetSize()>0) {
                    try {
                        if (UserButtonPresses.Remove() == LastChordsPlayedByComputer[playerMovesCounter].getChordNumber())
                        {
                            playerMovesCounter++;
                            score++;
                            m_GameCallback.UpdateScore(score);
                        }
                        else {
                            disableGameButtons();
                            m_PlayerLost=true;
                            break;
                        }
                    }
                    catch (Exception Ex)
                    {
                     Ex.printStackTrace();

                    }
                }
            }
            playerMovesCounter=0;
            m_buttonPresses++;




        }
        //game over in any case. sorry Natan. it's about the road and not the destination
        m_GameCallback.GameOver();
    }

    private void disableGameButtons()
    {
        for (int i = 0; i < m_buttons.length; i++)
        {
            m_buttons[i].m_ImageButton.setClickable(false);
        }
    }
    private void enableGameButtons()
    {
        for (int i = 0; i < m_buttons.length; i++)
        {
            m_buttons[i].m_ImageButton.setClickable(true);
        }

    }


    public int getScore() {
        return score;
    }
}
