package com.example.simon_says_app;

public class Chord
{
    private int PauseTimeAfterCord;
    private  int chordNumber;
    private  ImageButtonWrapper m_Button;

    public Chord(int i_PauseTimeAfterCord,int i_ChordNumber)
    {
        PauseTimeAfterCord=i_PauseTimeAfterCord;
        chordNumber=i_ChordNumber;

    }

    public int getPauseTimeAfterCord() {
        return PauseTimeAfterCord;
    }



    public int getChordNumber() {
        return chordNumber;
    }


    public ImageButtonWrapper getButtonWrapper() {
        return m_Button;
    }

    public void setButtonID(ImageButtonWrapper ButtonWraper) {
        this.m_Button = ButtonWraper;
    }
}
