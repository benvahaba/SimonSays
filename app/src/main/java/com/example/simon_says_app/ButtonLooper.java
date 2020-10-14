package com.example.simon_says_app;

import android.widget.ImageButton;

public class ButtonLooper  {

    private final Chord[] m_chords;
    //    private ScheduledExecutorService SongExe;


    public ButtonLooper( Chord[] i_chords)throws Exception
    {
        if(i_chords==null)
            throw new Exception("Chords list cant be null");
        m_chords=i_chords;
        //SongExe= Executors.newSingleThreadScheduledExecutor();
    }

    public void startSong()
    {
        for (int i = 0; i <m_chords.length ; i++) {
//TODO ive tyied to use a ScheduledExecutorService but it didnt worked
            //SongExe.schedule(m_chords[i].getButtonWrapper(),m_chords[i].getPauseTimeAfterCord()+m_chords[i].getButtonWrapper().SongDurationInMilliSec, TimeUnit.MILLISECONDS);


            m_chords[i].getButtonWrapper().run();
            //use sqechualed thread pool
            try {
                Thread.sleep(m_chords[i].getPauseTimeAfterCord());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
