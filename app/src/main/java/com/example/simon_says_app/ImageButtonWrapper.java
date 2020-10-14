package com.example.simon_says_app;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;

public class ImageButtonWrapper implements Runnable
{
    //the button shouldnt be changed but the pics are;
    private final int m_DefaultButtonCounter;
    public ImageButton m_ImageButton;
    public int ImageResButtonOff;
    public int ImageResButtonOn;
    public long SongDurationInMilliSec=700;

     private volatile MediaPlayer m_MP;



    public int GetImageButtonID()
    {
        return m_ImageButton.getId();
    }

    public ImageButtonWrapper(MediaPlayer i_MP, View i_ImageButton, int i_ImageResButtonOff, int i_ImageResButtonOn,int i_DefaultButtonCounter )
    {


        m_MP=i_MP;

        m_ImageButton= (ImageButton) i_ImageButton;
        ImageResButtonOff=i_ImageResButtonOff;
        ImageResButtonOn=i_ImageResButtonOn;
        m_DefaultButtonCounter=i_DefaultButtonCounter;

    }
    public int GetButtonNumber(){return m_DefaultButtonCounter;}



    @Override
    public synchronized  void run()
    {

        m_ImageButton.setImageResource(ImageResButtonOn);
        m_MP.start();
        try {
            Thread.sleep(700);
        }

        catch (Exception ex)
        {}
        finally { m_ImageButton.setImageResource(ImageResButtonOff); }
    }
}
