package com.example.simon_says_app;

public  class SongCreator
{
    ChordCreator m_cord;
    public SongCreator(int i_NumberOfGameButtons,int i_GameDifficulty)
    {
        try {
            m_cord = new ChordCreator(i_NumberOfGameButtons, i_GameDifficulty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  Chord[] CreateSong(int numOfChords)
    {
        Chord[] SongArray=new Chord[numOfChords];
        for(int i=0;i<numOfChords;i++)
        {
            SongArray[i]=m_cord.create();
        }

        return SongArray;
    }
}
