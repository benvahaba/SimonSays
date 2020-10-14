package com.example.simon_says_app;

import java.util.Random;

public  class ChordCreator
{

    private  int m_NumberOfGameButtons;
    private  int m_GameDifficulty;
    private  Random rand;



    public ChordCreator(int i_NumberOfGameButtons, int i_GameDifficulty)throws Exception
    {
        NumberOfGameButtons(i_NumberOfGameButtons);
        SetGameDifficulty(i_GameDifficulty);
    }
    public Chord create()
    {
        rand=new Random();
        Chord chord=new Chord((1000+rand.nextInt(1001))/m_GameDifficulty,rand.nextInt(m_NumberOfGameButtons));

        return chord;
    }
    public  void NumberOfGameButtons(int i_NumberOfGameButtons) throws Exception
    {
        if(i_NumberOfGameButtons>0)
        {
            m_NumberOfGameButtons=i_NumberOfGameButtons;
        }
        else
        {
            throw new Exception("must have at least 1 Button in game");
        }
    }
    public  void SetGameDifficulty(int i_GameDifficulty) throws Exception
    {
        if(i_GameDifficulty>0)
        {
            m_GameDifficulty=i_GameDifficulty;
        }
        else
        {
            throw new Exception(" Game Difficulty must be atleast 1 (the easiest)");
        }
    }

}
