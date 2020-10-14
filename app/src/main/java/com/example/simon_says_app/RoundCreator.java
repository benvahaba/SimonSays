package com.example.simon_says_app;

class RoundCreator
{
    //TODO add a callback to the save the chords[] and extend Timer

    private SongCreator songCreator;
    private int m_numOfButtonsPresses;
    private ImageButtonWrapper[] m_buttons;
    private int m_DefaultTimeToRestBeforeWeStart=2000;
    private int TimeToRestBeforeWeStart=2000;
    private ButtonLooper BL;





    RoundCreator(ImageButtonWrapper[] i_buttons, int i_GameDifficulty)
    {
        m_buttons=i_buttons;
        songCreator=new SongCreator(i_buttons.length,i_GameDifficulty);

    }

    public Chord[] round(int i_numOfButtonsPresses)
    {
        try {
            Thread.sleep(TimeToRestBeforeWeStart);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m_numOfButtonsPresses=i_numOfButtonsPresses;
        Chord[] ComputerChors=songCreator.CreateSong(m_numOfButtonsPresses);

        playSong(ComputerChors);
        return ComputerChors;
    }
    private void playSong(Chord[] i_ComputerChors)
    {
        for(int i=0;i<i_ComputerChors.length;i++)
        {
            i_ComputerChors[i].setButtonID(m_buttons[i_ComputerChors[i].getChordNumber()]);

        }
        try {
           BL=new ButtonLooper(i_ComputerChors);

            BL.startSong();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    public int getTimeToRestBeforeWeStart() {
        return TimeToRestBeforeWeStart;
    }

    public void setTimeToRestBeforeWeStart(int timeToRestBeforeWeStart) {
        TimeToRestBeforeWeStart = timeToRestBeforeWeStart;
    }
}
